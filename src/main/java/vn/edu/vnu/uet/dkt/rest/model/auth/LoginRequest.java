package vn.edu.vnu.uet.dkt.rest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {
    @JsonProperty("Username")
    private String username;

    @JsonProperty("Password")
    private String password;
}
