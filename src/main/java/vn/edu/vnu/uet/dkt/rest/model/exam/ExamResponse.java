package vn.edu.vnu.uet.dkt.rest.model.exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExamResponse {
    @JsonProperty(value = "SubjectName")
    private String subjectName;

    @JsonProperty(value = "SubjectCode")
    private String subjectCode;

    @JsonProperty(value = "NumberOfStudent")
    private Integer numberOfStudent;

    @JsonProperty(value = "NumberOfStudentSubscribe")
    private Integer numberOfStudentSubscribe;

    @JsonProperty(value = "StartTime")
    private String startTime;

    @JsonProperty(value = "EndTime")
    private String endTime;

    @JsonProperty(value = "Date")
    private String date;

    @JsonProperty(value = "Location")
    private String location;
}
