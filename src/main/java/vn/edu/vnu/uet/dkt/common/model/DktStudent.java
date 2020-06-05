package vn.edu.vnu.uet.dkt.common.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DktStudent {
    private Long id;
    private String username;
    private String email;
    private String courses;
    private String role;
    private String fullName;
}
