package vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegisterRequest {
    @JsonProperty(value = "Register")
    private List<RegisterModel> registers;

    @JsonProperty(value = "Cancel")
    private List<RegisterModel> cancels;
}
