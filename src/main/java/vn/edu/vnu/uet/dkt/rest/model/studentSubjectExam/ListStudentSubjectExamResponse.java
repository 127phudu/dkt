package vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dkt.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListStudentSubjectExamResponse {
    @JsonProperty("StudentSubjectExams")
    List<StudentSubjectExamResponse> studentSubjectExamResponses;

    @JsonProperty("Page")
    PageResponse pageResponse;

    public ListStudentSubjectExamResponse(List<StudentSubjectExamResponse> studentSubjectExamResponses, PageResponse pageResponse) {
        this.studentSubjectExamResponses = studentSubjectExamResponses;
        this.pageResponse = pageResponse;
    }
}
