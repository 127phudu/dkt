package vn.edu.vnu.uet.dkt.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormValidateException extends BaseException {

    private static final long serialVersionUID = 1L;
    private int code;
    private String message;
    private Map<String, Object> data;
}