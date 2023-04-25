package xyz.mpdn.resource.exception.handler;


import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({CallNotPermittedException.class, HttpServerErrorException.class})
    public void handleCallNotPermittedException(Exception ex) {
        log.debug("Exception handled", ex);
    }
}
