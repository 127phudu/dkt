package vn.edu.vnu.uet.dkt.rest.controller.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dkt.common.exception.FormValidateException;
import vn.edu.vnu.uet.dkt.common.validator.EmailValidator;
import vn.edu.vnu.uet.dkt.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dkt.dto.model.Student;
import vn.edu.vnu.uet.dkt.rest.controller.BaseController;
import vn.edu.vnu.uet.dkt.rest.model.account.AccountRequest;
import vn.edu.vnu.uet.dkt.rest.model.account.AccountResponse;

import javax.validation.Valid;

@RestController
public class AccountController extends BaseController {
    @Autowired
    private StudentDao studentDao;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper mapper = new ObjectMapper();

    @PostMapping
    public AccountResponse createAccount(@RequestBody @Valid AccountRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new FormValidateException(result);
        }
        String password = request.getPassword();
        if (!password.equals(request.getPasswordConfirm())) {
            throw new FormValidateException("password", "password confirm not equal password");
        }

        if (checkAccountExist(request)){
            throw new FormValidateException("username/email", "account đã tồn tại");
        }

        String passwordEncode = passwordEncoder.encode(password);
        Student student = new Student();
        student.setEmail(request.getEmail());
        student.setUsername(request.getUsername());
        student.setCourses(request.getCourses());
        student.setPassword(passwordEncode);
        Student saveStudent = studentDao.save(student);
        return AccountResponse.builder()
                .id(saveStudent.getId())
                .username(saveStudent.getUsername())
                .email(saveStudent.getEmail())
                .courses(saveStudent.getCourses())
                .build();
    }

    private boolean checkAccountExist(AccountRequest request) {
        Student student;
        String email = request.getEmail();
        String username = request.getUsername();
        if (emailValidator.validateEmail(email)) {
            student = studentDao.getByEmail(email);
        } else {
            student = studentDao.getByUsername(username);
        }
        if (student != null) return true;
        return false;

    }

}

