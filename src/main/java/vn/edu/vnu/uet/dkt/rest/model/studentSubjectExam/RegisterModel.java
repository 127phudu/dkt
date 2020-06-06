package vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterModel {
    @JsonProperty("LocationId")
    private Long locationId;

    @JsonProperty("SubjectSemesterId")
    private Long subjectSemesterId;

    @JsonProperty("StartTime")
    private String startTime;
}
