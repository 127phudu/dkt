package vn.edu.vnu.uet.dkt.dto.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numberOfStudent;
    private Integer numberOfStudentSubscribe;
    private String examCode;
    private LocalDateTime startTime;
    private LocalDateTime date;
    private Long subjectSemesterId;
    private Long roomSemesterId;
    private Long semesterId;
    private Long subjectId;
    private Long locationId;
    private LocalDateTime endTime;
}
