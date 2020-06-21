package vn.edu.vnu.uet.dkt.rest.model.semester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dkt.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListSemesterResponse {
    @JsonProperty(value = "SemesterResponse")
    List<SemesterResponse> semesterResponses;

    @JsonProperty(value = "Page")
    private PageResponse pageResponse;

    public ListSemesterResponse(List<SemesterResponse> semesterResponses, PageResponse pageResponse) {
        this.semesterResponses = semesterResponses;
        this.pageResponse = pageResponse;
    }
}
