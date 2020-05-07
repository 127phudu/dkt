package vn.edu.vnu.uet.dkt.dto.dao.studentSubject;

import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;

import java.util.List;

public interface StudentSubjectDao {
    List<StudentSubject> getAll();

    StudentSubject store(StudentSubject studentSubject);

    StudentSubject getByStudentAndSubjectSemesterId(Long studentId, Long subjectSemesterId);

    StudentSubject getByIdAndSubjectId(Long id, Long studentId);

    StudentSubject getById(Long id);

    List<StudentSubject> getBySemesterIdAndStudentId(Long semesterId, Long studentId);
}
