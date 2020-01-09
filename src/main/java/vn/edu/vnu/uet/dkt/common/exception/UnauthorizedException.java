package vn.edu.vnu.uet.dkt.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.edu.vnu.uet.dkt.common.exception.BaseException;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "UNAUTHORIZED")
public class UnauthorizedException extends BaseException {
    private static final long serialVersionUID = 1L;
    private String message = "Đã có lỗi xảy ra";

    public UnauthorizedException() {

    }

    public UnauthorizedException(String message) {

    }

    @Override
    public String getMessage(){
        return this.message;
    }
}