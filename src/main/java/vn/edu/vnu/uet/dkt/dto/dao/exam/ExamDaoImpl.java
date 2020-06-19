package vn.edu.vnu.uet.dkt.dto.dao.exam;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dkt.dto.model.Exam;
import vn.edu.vnu.uet.dkt.dto.repository.ExamRepository;

import javax.persistence.LockModeType;
import java.util.ArrayList;
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
    public List<Exam> getAllBySemesterId(Long semesterId) {
        return examRepository.findAllBySemesterId(semesterId);
    }

    @Override
    public List<Exam> getExamByLocationAndSubjectSemester(Long locationId, Long subjectSemesterId) {
        List<Exam> exams = examRepository.findAllByLocationIdAndSubjectSemesterId(locationId, subjectSemesterId);
        if(CollectionUtils.isEmpty(exams)) return null;
        return exams;
    }

    @Override
    public List<Exam> getExamBySemesterIdAndSubjectIdIn(Long semesterId, List<Long> subjectIds) {
        List<Exam> exams = examRepository.findAllBySemesterIdAndSubjectIdIn(semesterId, subjectIds);
        if(CollectionUtils.isEmpty(exams)) return null;
        return exams;
    }

    @Override
    public List<Exam> getExamBySubjectSemesterId(Long subjectSemesterId) {
        List<Exam> exams = examRepository.findAllBySubjectSemesterId(subjectSemesterId);
        if(CollectionUtils.isEmpty(exams)) return null;
        return exams;
    }

    @Override
    public List<Exam> getByExamIdIn(List<Long> examIds) {
        List<Exam> exams = examRepository.findAllByIdIn(examIds);
        if(CollectionUtils.isEmpty(exams)) return new ArrayList<>();
        return exams;
    }
}
