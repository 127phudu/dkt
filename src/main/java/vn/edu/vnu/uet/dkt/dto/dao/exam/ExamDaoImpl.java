package vn.edu.vnu.uet.dkt.dto.dao.exam;

import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dkt.dto.model.Exam;
import vn.edu.vnu.uet.dkt.dto.repository.ExamRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExamDaoImpl implements ExamDao {
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
        return examRepository.findById(id).orElse(null);
    }

    @Override
    public Exam getByExamCode(String examCode) {
        return examRepository.findByExamCode(examCode);
    }

    @Override
    public List<Exam> getAllBySemesterId(Long semesterId) {
        return examRepository.findAllByRoomSemesterId(semesterId);
    }
}
