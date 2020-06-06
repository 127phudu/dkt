package vn.edu.vnu.uet.dkt.rest.controller.render;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;
import vn.edu.vnu.uet.dkt.common.security.JwtTokenHelper;
import vn.edu.vnu.uet.dkt.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dkt.dto.model.Student;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("render")
public class RenderToken {
    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @GetMapping
    public List<String> render() {
        List<Student> students = studentDao.getAll();
        List<String> response = new ArrayList<>();
        for (Student student : students) {
            DktStudent dktStudent = DktStudent.builder()
                    .id(student.getId())
                    .courses(student.getCourse())
                    .email(student.getEmail())
                    .role("Student")
                    .username(student.getUsername())
                    .build();
            String token = jwtTokenHelper.generateTokenStudent(dktStudent);
            response.add(token);
        }
        return response;
    }
}
