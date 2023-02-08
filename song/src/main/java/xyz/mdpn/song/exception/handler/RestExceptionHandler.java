package xyz.mdpn.song.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import xyz.mdpn.song.exception.message.Message;
import xyz.mdpn.song.exception.message.MessageFactory;

import java.util.Date;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class RestExceptionHandler {

    private MessageFactory messageFactory;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Message> responseAnyStatusExceptionHandler(ResponseStatusException ex, WebRequest request) {
        return getResponseEntity((HttpStatus) ex.getStatusCode(), ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Message> globalExceptionHandler(Exception ex, WebRequest request) {
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Message> validationExceptionHandler(Exception ex, WebRequest request) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, ex, request);
    }

    private ResponseEntity<Message> getResponseEntity(HttpStatus httpStatus, Exception exception, WebRequest request) {


        var message = messageFactory.createMessage();
        log.debug("Exception handled", exception);

        message.setTimestamp(new Date());
        message.setStatusCode(httpStatus.value());
        message.setUri(request.getDescription(false));
        message.setException(exception);

        return new ResponseEntity<>(message, httpStatus);
    }

}