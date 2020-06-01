package vn.edu.vnu.uet.dkt.rest.model.exam;

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
    private List<ExamResponse> examResponses;
    private PageResponse pageBase;

    public ListExamResponse(List<ExamResponse> examResponses, PageResponse pageBase) {
        this.examResponses = examResponses;
        this.pageBase = pageBase;
    }
}
