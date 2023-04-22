package xyz.mpdn.storage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.mpdn.storage.entity.Storage;
import xyz.mpdn.storage.exception.HTTPNotFoundException;
import xyz.mpdn.storage.repository.StorageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageRepository storageRepository;
    @Value("${spring.application.name}")
    private String serviceName;

    @GetMapping
    public Iterable<Storage> getServiceName() {
        return storageRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public Map<String, ?> deleteResource(
            //Parse CSV ids to string only for validation purpose
            @Length(max = 199, message = "Valid CSV length of ids should be less then 200 characters") @PathVariable("id") String id,
            @PathVariable("id") List<Storage> storages
    ) {
        var ids = storages
                .stream()
                .filter(Objects::nonNull)
                .map(Storage::getId)
                .toList();

        if (ids.size() < 1) throw new HTTPNotFoundException();

        storageRepository.deleteAllById(ids);

        return new HashMap<>() {{
            put("ids", ids);
        }};
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, ?> createStorage(@RequestBody List<Storage> storages) {

        var ids = storageRepository.saveAll(storages)
                .stream()
                .map(Storage::getId)
                .toList();

        return new HashMap<>() {{
            put("ids", ids);
        }};
    }
}
