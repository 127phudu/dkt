package vn.edu.vnu.uet.dkt.dto.dao.exam;

import vn.edu.vnu.uet.dkt.dto.model.Exam;

import java.util.List;

public interface ExamDao {
    Exam store(Exam exam);

    Exam getById(Long id);

    Exam getByExamCode(String examCode);

    List<Exam> getAllBySemesterId( Long semesterId);

    List<Exam> getExamByLocationAndSubjectSemester(Long locationId, Long subjectSemesterId);

    List<Exam> getExamBySemesterIdAndStudentIdIn(Long semesterId, List<Long> studentIds);
}
