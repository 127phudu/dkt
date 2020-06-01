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

    @GetMapping("/all")
    public ApiDataResponse<ListExamResponse> getExamAll(@RequestParam Long semesterId) {
        try {
            return ApiDataResponse.ok(examService.getAll(semesterId));
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

    @PatchMapping(path = "redis_trans/{id}")
    public ApiDataResponse<ExamResponse> redisTrans(@PathVariable Long id) {
        try {
            long result = examService.redisTrans(id);
            if (result > 0) {
                return ApiDataResponse.ok(result);
            } else {
                return ApiDataResponse.error();
            }

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
