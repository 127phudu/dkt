package vn.edu.vnu.uet.dkt.rest.model.exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.vnu.uet.dkt.rest.model.PageBase;
import vn.edu.vnu.uet.dkt.rest.model.PageResponse;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ListExamResponse {
    @JsonProperty(value = "Exams")
    private List<ExamResponse> examResponses;

    @JsonProperty(value = "Page")
    private PageResponse pageBase;

    public ListExamResponse(List<ExamResponse> examResponses, PageResponse pageBase) {
        this.examResponses = examResponses;
        this.pageBase = pageBase;
    }
}
