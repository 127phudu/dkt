package vn.edu.vnu.uet.dkt.dto.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.common.exception.UnauthorizedException;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;
import vn.edu.vnu.uet.dkt.common.security.JwtTokenHelper;
import vn.edu.vnu.uet.dkt.common.validator.EmailValidator;
import vn.edu.vnu.uet.dkt.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dkt.dto.model.Student;
import vn.edu.vnu.uet.dkt.rest.model.auth.LoginRequest;
import vn.edu.vnu.uet.dkt.rest.model.auth.LoginResponse;

@Service
public class AuthenticationService {
    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    public LoginResponse login(LoginRequest request) {

        String username = request.getUsername();
        String password = request.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new UnauthorizedException("Tài khoản mật khẩu không chính xác");
        }

        Student student = this.getStudentUsernameOrEmail(username);

        boolean result = passwordEncoder.matches( request.getPassword(), student.getPassword());
        if (!result) {
            throw new UnauthorizedException("Tài khoản mật khẩu không chính xác");
        }

        DktStudent dktStudent = mapper.convertValue(student, DktStudent.class);
        String token = jwtTokenHelper.generateTokenStudent(dktStudent);
        return LoginResponse.builder()
                .token(token)
                .build();
    }

    private Student getStudentUsernameOrEmail(String username) {
        if (emailValidator.validateEmail(username)) {
            return studentDao.getByEmail(username);
        }
        return studentDao.getByUsername(username);
    }
}
