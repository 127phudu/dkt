package vn.edu.vnu.uet.dkt.rest.controller.studentSubject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dkt.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dkt.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dkt.rest.model.studentSubject.StudentSubjectRequest;

@RestController
@RequestMapping("/student/student_subjects")
public class StudentSubjectController {
    private final StudentSubjectService studentSubjectService;

    public StudentSubjectController(StudentSubjectService studentSubjectService) {
        this.studentSubjectService = studentSubjectService;
    }

    @GetMapping
    public ApiDataResponse<StudentSubjectRequest> getBySemester(@RequestParam Long id ) {
        return null;
    }
}
