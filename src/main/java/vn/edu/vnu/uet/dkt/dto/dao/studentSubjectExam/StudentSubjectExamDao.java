package vn.edu.vnu.uet.dkt.dto.dao.studentSubjectExam;

import vn.edu.vnu.uet.dkt.dto.model.StudentSubjectExam;

import java.util.List;

public interface StudentSubjectExamDao {
    StudentSubjectExam store(StudentSubjectExam studentSubjectExam);

    StudentSubjectExam getById(Long id);

    StudentSubjectExam getByExamIdAndStudentSubjectId(Long examId, Long studentSubjectId);

    List<StudentSubjectExam> getAll();
    List<StudentSubjectExam> getByStudentId(Long id);
    void delete(StudentSubjectExam studentSubjectExam);

}
