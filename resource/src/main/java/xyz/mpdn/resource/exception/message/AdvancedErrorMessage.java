package xyz.mpdn.resource.exception.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper=false)
public class AdvancedErrorMessage extends ErrorMessage {
    private List<String> trace;

    @Override
    public void setException(Exception exception) {
        super.setException(exception);
        setTrace(
                Arrays.stream(exception.getStackTrace())
                        .limit(10)
                        .map(Objects::toString)
                        .collect(Collectors.toList())
        );
    }
}
