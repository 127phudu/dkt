package vn.edu.vnu.uet.dkt.dto.dao.exam;

import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dkt.dto.model.Exam;
import vn.edu.vnu.uet.dkt.dto.repository.ExamRepository;

import java.util.Optional;

@Repository
public class ExamDaoImpl implements ExamDao{
    private final ExamRepository examRepository;

    public ExamDaoImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public Exam store(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    public Exam getById(Long id) {
        Optional<Exam> exam = examRepository.findById(id);
        return exam.orElseGet(exam::get);
    }

    @Override
    public Exam getByExamCode(String examCode) {
        return examRepository.findByExamCode(examCode);
    }
}
