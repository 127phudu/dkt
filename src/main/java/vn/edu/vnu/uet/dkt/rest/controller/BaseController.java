package vn.edu.vnu.uet.dkt.rest.controller;

import com.google.common.base.CaseFormat;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.edu.vnu.uet.dkt.common.exception.FormValidateException;
import vn.edu.vnu.uet.dkt.common.validator.ValidateMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    @ExceptionHandler(FormValidateException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ValidateMessage handleFormValidateException(FormValidateException ex, HttpServletRequest request) {
        val exceptionMessage = new ValidateMessage();
        Map<String, Object> errors = new HashMap<>();
        exceptionMessage.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        if (ex.getBindingResult() != null) {
            for (val error : ex.getBindingResult().getFieldErrors()) {
                putFieldError(errors, error);
            }
        }
        if (ex.getMessageResult() != null) {
            for (val error : ex.getMessageResult().entrySet()) {
                val key = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, error.getKey());
                val value = error.getValue();
                if (!errors.containsKey(key)) {
                    errors.put(key, value);
                }
            }
        }
        exceptionMessage.setErrors(errors);
        return exceptionMessage;
    }

    private void putFieldError(Map<String, Object> errors, FieldError error) {
        val key = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, error.getField());
        val value = error.getDefaultMessage();
        if (!errors.containsKey(key)) {
            errors.put(key, value.toString());
        }
    }
}
