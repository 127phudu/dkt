package vn.edu.vnu.uet.dkt.dto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.common.validator.EmailValidator;
import vn.edu.vnu.uet.dkt.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dkt.dto.model.Student;
import vn.edu.vnu.uet.dkt.rest.model.login.LoginRequest;

@Service
public class AuthenticationService {
    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(LoginRequest request) {


        String username = request.getUsername();
        Student student;
        if (emailValidator.validateEmail(username)) {
            student = studentDao.getByEmail(username);
        } else {
            student = studentDao.getByUsername(username);
        }
        Boolean result = passwordEncoder.matches( request.getPassword(), student.getPassword());
        System.out.println(student);
        return "success";
    }
}
