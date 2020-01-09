package vn.edu.vnu.uet.dkt.rest.controller.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dkt.common.exception.FormValidateException;
import vn.edu.vnu.uet.dkt.dto.service.AuthenticationService;
import vn.edu.vnu.uet.dkt.rest.controller.BaseController;
import vn.edu.vnu.uet.dkt.rest.model.login.LoginRequest;
import vn.edu.vnu.uet.dkt.rest.model.login.LoginResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class LoginController extends BaseController {


    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request, BindingResult result){

        if (result.hasErrors()) {
            throw new FormValidateException(result);
        }
        return authenticationService.login(request);
    }
}
