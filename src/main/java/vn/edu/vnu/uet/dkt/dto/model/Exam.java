package vn.edu.vnu.uet.dkt.dto.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

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
    private Instant startTime;
    private Instant date;
    private Long locationId;
    private Long subjectSemesterId;
    private Long roomSemesterId;
    private Long semesterId;
    private Integer duration;
}
