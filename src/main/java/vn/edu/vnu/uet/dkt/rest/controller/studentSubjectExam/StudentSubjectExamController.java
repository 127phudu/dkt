package vn.edu.vnu.uet.dkt.rest.controller.studentSubjectExam;

import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dkt.common.exception.BaseException;
import vn.edu.vnu.uet.dkt.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dkt.dto.service.studentSubjectExam.StudentSubjectExamService;
import vn.edu.vnu.uet.dkt.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam.StudentSubjectExamRequest;
import vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam.StudentSubjectExamResponse;

@RestController
@RequestMapping("/student/student_subject_exams")
public class StudentSubjectExamController {
    private final StudentSubjectService studentSubjectService;
    private final StudentSubjectExamService studentSubjectExamService;

    public StudentSubjectExamController(StudentSubjectService studentSubjectService, StudentSubjectExamService studentSubjectExamService) {
        this.studentSubjectService = studentSubjectService;
        this.studentSubjectExamService = studentSubjectExamService;
    }

    @PostMapping
    public ApiDataResponse<StudentSubjectExamResponse> create(@RequestBody StudentSubjectExamRequest request) {
        try {
            return ApiDataResponse.ok(studentSubjectExamService.create(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }
}
