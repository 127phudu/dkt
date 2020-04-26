package vn.edu.vnu.uet.dkt.rest.model.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ListExamResponse {
    private List<ExamResponse> examResponses;

    public ListExamResponse(List<ExamResponse> examResponses) {
        this.examResponses = examResponses;
    }
}
