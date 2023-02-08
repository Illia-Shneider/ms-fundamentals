package xyz.mdpn.song.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HTTPNotFoundException extends ResponseStatusException {
    public HTTPNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
