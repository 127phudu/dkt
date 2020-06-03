package vn.edu.vnu.uet.dkt.rest.model.semester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListSemesterResponse {
    @JsonProperty(value = "SemesterResponse")
    List<SemesterResponse> semesterResponses;

    public ListSemesterResponse(List<SemesterResponse> semesterResponses) {
        this.semesterResponses = semesterResponses;
    }
}
