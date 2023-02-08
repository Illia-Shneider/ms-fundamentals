package xyz.mdpn.song.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HTTPInternalServerErrorException extends ResponseStatusException {
    public HTTPInternalServerErrorException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HTTPInternalServerErrorException(Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, null, cause);
    }
}
