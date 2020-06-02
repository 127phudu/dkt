package vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dkt.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListRegisterResultResponse {
    @JsonProperty(value = "RegisterResults")
    private List<RegisterResultResponse> resultResponses;

    @JsonProperty(value = "PageBase")
    private PageResponse pageBase;

    public ListRegisterResultResponse(List<RegisterResultResponse> resultResponses, PageResponse pageBase) {
        this.resultResponses = resultResponses;
        this.pageBase = pageBase;
    }
}
