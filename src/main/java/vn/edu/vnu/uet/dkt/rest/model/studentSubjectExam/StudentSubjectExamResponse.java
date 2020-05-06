package vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSubjectExamResponse {
    @JsonProperty("ExamId")
    private Long examId;

    @JsonProperty("StudentSubjectId")
    private Long studentSubjectId;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("LocationId")
    private Long locationId;
}
