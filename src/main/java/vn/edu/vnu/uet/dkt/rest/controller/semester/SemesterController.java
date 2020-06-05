package vn.edu.vnu.uet.dkt.rest.controller.semester;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dkt.common.exception.BaseException;
import vn.edu.vnu.uet.dkt.common.security.AccountService;
import vn.edu.vnu.uet.dkt.dto.service.semester.SemesterService;
import vn.edu.vnu.uet.dkt.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dkt.rest.model.semester.ListSemesterResponse;
import vn.edu.vnu.uet.dkt.rest.model.semester.SemesterResponse;

@RestController
@RequestMapping("/student/semester")
public class SemesterController {
    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @GetMapping("/all")
    public ApiDataResponse<ListSemesterResponse> getAll() {
        try {
            return ApiDataResponse.ok(semesterService.getAll());
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/active")
    public ApiDataResponse<SemesterResponse> getActive() {
        try {
            return ApiDataResponse.ok(semesterService.getActive());
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }
}
