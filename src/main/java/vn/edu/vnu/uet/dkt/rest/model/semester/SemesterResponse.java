package vn.edu.vnu.uet.dkt.rest.model.semester;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SemesterResponse {
    private String semesterName;
    private String semesterCode;
    private String startDate;
    private String endDate;
    private Integer year;
    private Integer status;
}
