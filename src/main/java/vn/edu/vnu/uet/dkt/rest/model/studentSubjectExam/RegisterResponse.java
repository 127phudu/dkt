package vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {
    @JsonProperty("NumberSuccess")
    private Integer numberSuccess;

    @JsonProperty("NumberFail")
    private Integer numberFail;

    public RegisterResponse(Integer numberSuccess, Integer numberFail) {
        this.numberSuccess = numberSuccess;
        this.numberFail = numberFail;
    }
}
