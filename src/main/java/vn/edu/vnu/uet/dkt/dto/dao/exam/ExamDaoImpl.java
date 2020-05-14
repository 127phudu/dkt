package vn.edu.vnu.uet.dkt.dto.dao.exam;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dkt.dto.model.Exam;
import vn.edu.vnu.uet.dkt.dto.repository.ExamRepository;

import java.util.List;

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

    @Override
    public List<Exam> getExamByLocationAndSubjectSemester(Long locationId, Long subjectSemesterId) {
        List<Exam> exams = examRepository.findAllByLocationIdAndSubjectSemesterId(locationId, subjectSemesterId);
        if(CollectionUtils.isEmpty(exams)) return null;
        return exams;
    }

    @Override
    public List<Exam> getExamBySemesterIdAndStudentIdIn(Long semesterId, List<Long> studentIds) {
        List<Exam> exams = examRepository.findAllBySemesterIdAndSubjectIdIn(semesterId, studentIds);
    }
}
