package vn.edu.vnu.uet.dkt.dto.model.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@RedisHash("exam")
public class RedisExam {
    @Id private Long id;

    private Integer numberOfStudent;
    private Integer numberOfStudentSubscribe;
    private String examCode;
    private LocalDateTime startTime;
    private LocalDate date;
    private Long subjectSemesterId;
    private Long roomSemesterId;
    private Long semesterId;
    private Long subjectId;
    private Long locationId;
    private LocalDateTime endTime;
}
