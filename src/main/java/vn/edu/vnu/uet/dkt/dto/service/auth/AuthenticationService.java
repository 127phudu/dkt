package vn.edu.vnu.uet.dkt.dto.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.common.exception.UnAuthorizeException;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;
import vn.edu.vnu.uet.dkt.common.security.JwtTokenHelper;
import vn.edu.vnu.uet.dkt.common.validator.EmailValidator;
import vn.edu.vnu.uet.dkt.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dkt.dto.model.Student;
import vn.edu.vnu.uet.dkt.rest.model.auth.LoginRequest;
import vn.edu.vnu.uet.dkt.rest.model.auth.LoginResponse;

@Service
public class AuthenticationService {
    private final EmailValidator emailValidator;
    private final StudentDao studentDao;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper mapper;
    private final JwtTokenHelper jwtTokenHelper;

    public AuthenticationService(EmailValidator emailValidator, StudentDao studentDao, PasswordEncoder passwordEncoder, ObjectMapper mapper, JwtTokenHelper jwtTokenHelper) {
        this.emailValidator = emailValidator;
        this.studentDao = studentDao;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    public LoginResponse login(LoginRequest request) {

        String username = request.getUsername();
        String password = request.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new UnAuthorizeException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        Student student = this.getStudentUsernameOrEmail(username);
        if (student == null) {
            throw new UnAuthorizeException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        boolean result = passwordEncoder.matches(request.getPassword(), student.getPassword());
        if (!result) {
            throw new UnAuthorizeException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
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
