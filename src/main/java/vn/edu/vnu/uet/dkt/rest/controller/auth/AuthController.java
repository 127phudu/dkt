package vn.edu.vnu.uet.dkt.rest.controller.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dkt.common.exception.BaseException;
import vn.edu.vnu.uet.dkt.dto.service.auth.AuthenticationService;
import vn.edu.vnu.uet.dkt.dto.service.sendMail.ResetPassword;
import vn.edu.vnu.uet.dkt.rest.controller.BaseController;
import vn.edu.vnu.uet.dkt.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dkt.rest.model.auth.LoginRequest;
import vn.edu.vnu.uet.dkt.rest.model.auth.LoginResponse;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {

    private final AuthenticationService authenticationService;
    private final ResetPassword resetPassword;

    public AuthController(AuthenticationService authenticationService, ResetPassword resetPassword) {
        this.authenticationService = authenticationService;
        this.resetPassword = resetPassword;
    }

    @PostMapping("/login")
    public ApiDataResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            return ApiDataResponse.ok(authenticationService.login(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/forgot_password")
    public ApiDataResponse<String> forgotPassword(@RequestBody Map<String,String> request) throws MessagingException, MessagingException, javax.mail.MessagingException {
        String email = request.get("Email");
        resetPassword.resetPassword(email);
        return ApiDataResponse.ok("success");
    }
}
