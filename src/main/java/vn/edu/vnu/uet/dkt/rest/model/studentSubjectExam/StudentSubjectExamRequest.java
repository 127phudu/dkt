package vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSubjectExamRequest {
    @JsonProperty("LocationId")
    private Long locationId;

    @JsonProperty("StudentSubjectId")
    private Long studentSubjectId;
}