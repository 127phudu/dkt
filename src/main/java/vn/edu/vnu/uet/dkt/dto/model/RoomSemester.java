package vn.edu.vnu.uet.dkt.dto.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "room_semesters")
public class RoomSemester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numberOfComputer;
    private String roomSemesterCode;
    private Integer availableComputer;
    private Long semesterId;
    private Long roomId;
}
