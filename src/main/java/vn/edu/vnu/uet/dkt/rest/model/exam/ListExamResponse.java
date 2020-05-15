package vn.edu.vnu.uet.dkt.rest.model.exam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ListExamResponse {
    private List<ExamResponse> examResponses;

    public ListExamResponse(List<ExamResponse> examResponses) {
        this.examResponses = examResponses;
    }
}
