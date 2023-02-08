package xyz.mpdn.resource.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class MimeTypeValidator implements ConstraintValidator<MimeType, MultipartFile> {

    private String mimeType;

    @Override
    public void initialize(MimeType constraintAnnotation) {
        mimeType = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

        log.debug("File type validation, file:{} ", multipartFile);
        log.debug("File type validation, name:{}, type:{} ", multipartFile.getName(), multipartFile.getContentType());

        var fileMimeType = multipartFile.getContentType();
        var valid = mimeType.equals(fileMimeType);

        if (!valid) {
            var message = String.format("Invalid file type, %s provided where a %s was expected", fileMimeType, mimeType);

            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        return valid;
    }
}
