package xyz.mdpn.song.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AfterDateValidator implements ConstraintValidator<AfterDate, Date> {

    private Date date;
    private boolean including;

    @Override
    public void initialize(AfterDate constraintAnnotation) {
        var formatter = new SimpleDateFormat(constraintAnnotation.format());
        try {
            date = formatter.parse(constraintAnnotation.value());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        including = constraintAnnotation.including();
    }

    @Override
    public boolean isValid(Date fieldDate, ConstraintValidatorContext constraintValidatorContext) {
        var diff = fieldDate.compareTo(date);
        if (diff > 0) {
            return true;
        } else return diff == 0 && including;
    }

}
