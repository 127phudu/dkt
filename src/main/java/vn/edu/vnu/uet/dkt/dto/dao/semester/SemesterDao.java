package vn.edu.vnu.uet.dkt.dto.dao.semester;

import vn.edu.vnu.uet.dkt.dto.model.Semester;

import java.time.LocalDateTime;
import java.util.List;

public interface SemesterDao  {
    Semester getById(Long id);
    List<Semester> getByStartDate(LocalDateTime dateTime);
    List<Semester> getSemesterRegistering();
    List<Semester> getSemesterRegistered();
    List<Semester> getSemesterIn(List<Long> ids);
}
