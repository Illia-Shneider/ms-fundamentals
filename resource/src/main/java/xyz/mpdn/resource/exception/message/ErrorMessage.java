package xyz.mpdn.resource.exception.message;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ErrorMessage implements Message {
    private int statusCode;
    private Date timestamp;
    private List<String> message = new ArrayList<>();
    private String uri;

    @Override
    public void setException(Exception exception) {
        if(exception instanceof ConstraintViolationException){
            message = ((ConstraintViolationException) exception)
                    .getConstraintViolations()
                    .stream()
                    .map(cv -> cv.getPropertyPath().toString() + ": " + cv.getMessage())
                    .collect(Collectors.toList());
        }else{
            message.add(exception.getMessage());
        }
    }
}
