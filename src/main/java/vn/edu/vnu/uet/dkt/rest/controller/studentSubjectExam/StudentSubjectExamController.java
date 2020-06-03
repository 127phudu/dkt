package vn.edu.vnu.uet.dkt.rest.controller.studentSubjectExam;

import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dkt.common.exception.BaseException;
import vn.edu.vnu.uet.dkt.common.utilities.PageUtil;
import vn.edu.vnu.uet.dkt.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dkt.dto.service.studentSubjectExam.StudentSubjectExamService;
import vn.edu.vnu.uet.dkt.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dkt.rest.model.PageBase;
import vn.edu.vnu.uet.dkt.rest.model.exam.ListExamResponse;
import vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam.*;

@RestController
@RequestMapping("/student/student_subject_exam")
public class StudentSubjectExamController {
    private final StudentSubjectService studentSubjectService;
    private final StudentSubjectExamService studentSubjectExamService;

    public StudentSubjectExamController(StudentSubjectService studentSubjectService, StudentSubjectExamService studentSubjectExamService) {
        this.studentSubjectService = studentSubjectService;
        this.studentSubjectExamService = studentSubjectExamService;
    }

    @PostMapping
    public ApiDataResponse<StudentSubjectExamResponse> create(@RequestBody RegisterModel request) {
        try {
            return ApiDataResponse.ok(studentSubjectExamService.create(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/register/semester/{id}")
    public ApiDataResponse<RegisterResponse> register(@RequestBody RegisterRequest registerRequest, @PathVariable Long id) {
        try {
            RegisterResponse registerResponse = studentSubjectExamService.register(registerRequest, id);
            return ApiDataResponse.ok(registerResponse);
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/registered/semester/{id}")
    public ApiDataResponse<ListExamResponse> getRegisterResult(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(studentSubjectExamService.getResult(id, pageBase));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping
    public ApiDataResponse<ListStudentSubjectExamResponse> getAll(
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(studentSubjectExamService.getAllByStudentId(pageBase));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/{id}")
    public ApiDataResponse<String> delete(@PathVariable Long id) {
        try {
            studentSubjectExamService.delete(id);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }
}
