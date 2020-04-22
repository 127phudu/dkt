package vn.edu.vnu.uet.dkt.dto.dao.exam;

import vn.edu.vnu.uet.dkt.dto.model.Exam;

public interface ExamDao {
    Exam store(Exam exam);
    Exam getById(Long id);
    Exam getByExamCode(String examCode);
}
