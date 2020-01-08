package vn.edu.vnu.uet.dkt.rest.model.login;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class LoginRequest {
    private String username;
    @Min(value = 6)
    private String password;
}
