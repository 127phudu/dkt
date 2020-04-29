package vn.edu.vnu.uet.dkt.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;

@Component
public class AccountService {

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public DktStudent getUserSession() {
        return (DktStudent) this.getAuthentication().getPrincipal();
    }
}
