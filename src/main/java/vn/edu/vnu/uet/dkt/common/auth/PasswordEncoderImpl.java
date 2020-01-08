package vn.edu.vnu.uet.dkt.common.auth;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderImpl implements PasswordEncoder {

    public String encode(CharSequence rawPassword) {
        return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(7)).replace("$2a$", "$2y$");
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String javaEncodedPassword = encodedPassword.replace("$2y$", "$2a$");
        return BCrypt.checkpw(rawPassword.toString(), javaEncodedPassword);
    }
}
