package vn.edu.vnu.uet.dkt.dto.dao.exam;

import vn.edu.vnu.uet.dkt.dto.model.Exam;

import java.util.List;

public interface ExamDao {
    Exam store(Exam exam);

    Exam getById(Long id);

    List<Exam> getAllBySemesterId( Long semesterId);

    List<Exam> getExamByLocationAndSubjectSemester(Long locationId, Long subjectSemesterId);

    List<Exam> getExamBySemesterIdAndSubjectIdIn(Long semesterId, List<Long> studentIds);

    List<Exam> getByExamIdIn(List<Long> examIds);
}
