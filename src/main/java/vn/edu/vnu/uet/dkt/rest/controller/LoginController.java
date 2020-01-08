package vn.edu.vnu.uet.dkt.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dkt.common.EmailValidator;
import vn.edu.vnu.uet.dkt.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dkt.dto.model.Student;
import vn.edu.vnu.uet.dkt.rest.model.login.LoginRequest;

@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    private String login(@RequestBody LoginRequest request){
        String encode = passwordEncoder.encode("tuhv98");

        String username = request.getUsername();
        Student student = new Student();

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
