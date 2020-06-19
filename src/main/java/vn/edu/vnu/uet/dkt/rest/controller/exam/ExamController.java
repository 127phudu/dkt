package vn.edu.vnu.uet.dkt.rest.controller.exam;

import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dkt.common.exception.BaseException;
import vn.edu.vnu.uet.dkt.common.utilities.PageUtil;
import vn.edu.vnu.uet.dkt.dto.service.exam.ExamService;
import vn.edu.vnu.uet.dkt.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dkt.rest.model.PageBase;
import vn.edu.vnu.uet.dkt.rest.model.exam.ExamResponse;
import vn.edu.vnu.uet.dkt.rest.model.exam.ListExamResponse;

@RestController
@RequestMapping("/student/exam")
public class ExamController {
    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/all/semester/{id}")
    public ApiDataResponse<ListExamResponse> getExamAll(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(examService.getAll(id, pageBase));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping(path = "{id}")
    public ApiDataResponse<ExamResponse> getExam(@PathVariable Long id) {
        try {
            return ApiDataResponse.ok(examService.getExam(id));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/subjectSemester/{subjectSemesterId}")
    public ApiDataResponse getExamBySubjectId(@PathVariable Long subjectSemesterId) {
        try {
            return ApiDataResponse.ok(examService.getExamBySubjectSemesterId(subjectSemesterId));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    /*@GetMapping("/semester/{id}/search")
    public ApiDataResponse<ListExamResponse> search(
            @PathVariable Long id
    ) {
        try {
            //PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(examService.search(id));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }*/
}
