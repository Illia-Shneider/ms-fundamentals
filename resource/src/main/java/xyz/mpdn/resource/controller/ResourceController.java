package xyz.mpdn.resource.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.mpdn.resource.component.HTTPRequestToMultipartFileAdapter;
import xyz.mpdn.resource.entity.Resource;
import xyz.mpdn.resource.exception.HTTPInternalServerErrorException;
import xyz.mpdn.resource.exception.HTTPNotFoundException;
import xyz.mpdn.resource.repository.ResourceRepository;
import xyz.mpdn.resource.service.ResourceInfo;
import xyz.mpdn.resource.service.StorageService;
import xyz.mpdn.resource.validator.MimeType;

import java.io.IOException;
import java.util.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/resource")
public class ResourceController {
    private final ResourceRepository resourceRepository;
    private final StorageService storageService;

    @Value("${spring.application.name}")
    private String serviceName;

    public ResourceController(ResourceRepository resourceRepository, StorageService storageService) {
        this.resourceRepository = resourceRepository;
        this.storageService = storageService;
    }

    @GetMapping
    public Map<String, String> getServiceName() {
        return new HashMap<>() {{
            put("service", serviceName);
        }};
    }

    @DeleteMapping("/{id}")
    @SuppressWarnings("unused")
    public Map<String, ?> deleteResource(
            //Parse CSV ids to string only for validation purpose
            @Length(max = 199, message = "Valid CSV length of ids should be less then 200 characters") @PathVariable("id") String id,
            @PathVariable("id") List<Resource> resources
    ) {

        var uuids = new ArrayList<String>();

        var rs = resources
                .stream()
                .filter(Objects::nonNull)
                .peek(r -> uuids.add(r.getUuid()))
                .toList();

        if (rs.size() < 1) throw new HTTPNotFoundException();

        var deleted = storageService.delete(uuids);

        var ids = rs
                .stream()
                .filter(r -> deleted.contains(r.getUuid()))
                .map(Resource::getId)
                .toList();

        resourceRepository.deleteAllById(ids);

        return new HashMap<>() {{

            put("ids", ids);
        }};
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> getResource(@PathVariable("id") Optional<Resource> resource) {

        var uuid = resource
                .map(Resource::getUuid)
                .orElseThrow(HTTPNotFoundException::new);

        var size = storageService
                .info(uuid)
                .map(ResourceInfo::size)
                .orElseThrow(HTTPInternalServerErrorException::new);

        var inputStream = storageService.get(uuid)
                .map(InputStreamResource::new)
                .orElseThrow(HTTPInternalServerErrorException::new);

        return ResponseEntity
                .ok()
                .contentLength(size)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(inputStream);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, headers = {HttpHeaders.RANGE})
    public ResponseEntity<InputStreamResource> getResourcePartial(
            @PathVariable("id") Optional<Resource> resource,
            @RequestHeader HttpHeaders headers
    ) {

        var uuid = resource
                .map(Resource::getUuid)
                .orElseThrow(HTTPNotFoundException::new);

        var size = storageService
                .info(uuid)
                .map(ResourceInfo::size)
                .orElseThrow(HTTPInternalServerErrorException::new);

        var range = headers.getRange().get(0);
        var rangeStart = range.getRangeStart(size);
        var rangeEnd = range.getRangeEnd(size);

        var inputStream = storageService.get(uuid, rangeStart, rangeEnd)
                .map(InputStreamResource::new)
                .orElseThrow(HTTPInternalServerErrorException::new);

        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .contentLength(rangeEnd - rangeStart + 1)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_RANGE, String.format("bytes %d-%d/%d", rangeStart, rangeEnd, size))
                .body(inputStream);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, ?> uploadResource(@MimeType("audio/mpeg") @RequestPart("file") MultipartFile file, Resource resource) throws IOException {

        String uuid = storageService
                .store(file.getInputStream(), file.getSize())
                .orElseThrow(HTTPInternalServerErrorException::new);

        resource.setUuid(uuid);

        var id = resourceRepository
                .save(resource)
                .getId();

        return new HashMap<>() {{
            put("id", id);
        }};
    }

    @PostMapping(consumes = {"audio/mpeg"})
    public Map<String, ?> uploadResource(HttpServletRequest request, Resource resource) throws IOException {
        return uploadResource(new HTTPRequestToMultipartFileAdapter(request), resource);
    }
}
