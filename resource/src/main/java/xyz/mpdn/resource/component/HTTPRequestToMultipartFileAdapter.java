package xyz.mpdn.resource.component;

import jakarta.servlet.ServletRequest;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
public class HTTPRequestToMultipartFileAdapter implements MultipartFile {
    private final ServletRequest servletRequest;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return null;
    }

    @Override
    public String getContentType() {
        return servletRequest.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return servletRequest.getContentLengthLong();
    }

    @NotNull
    @Override
    public byte[] getBytes() throws IOException {
        return servletRequest.getInputStream().readAllBytes();
    }

    @NotNull
    @Override
    public InputStream getInputStream() throws IOException {
        return servletRequest.getInputStream();
    }

    @Override
    public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {

    }
}
