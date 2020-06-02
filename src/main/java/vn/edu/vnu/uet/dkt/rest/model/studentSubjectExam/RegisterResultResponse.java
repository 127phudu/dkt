package vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResultResponse {
    @JsonProperty(value = "StudentSubjectExamId")
    private Long studentSubjectExamId;

    @JsonProperty(value = "SubjectName")
    private String subjectName;

    @JsonProperty(value = "SubjectCode")
    private String subjectCode;

    @JsonProperty(value = "StartTime")
    private String startTime;

    @JsonProperty(value = "EndTime")
    private String endTime;

    @JsonProperty(value = "Date")
    private String date;

    @JsonProperty(value = "Location")
    private String location;

    @JsonProperty(value = "LocationId")
    private Long locationId;

    @JsonProperty(value = "StudentSubjectId")
    private Long studentSubjectId;

    @JsonProperty(value = "RoomName")
    private String roomName;
}
