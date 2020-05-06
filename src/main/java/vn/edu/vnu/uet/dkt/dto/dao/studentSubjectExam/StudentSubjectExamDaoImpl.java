package vn.edu.vnu.uet.dkt.dto.dao.studentSubjectExam;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubjectExam;
import vn.edu.vnu.uet.dkt.dto.repository.StudentSubjectExamRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentSubjectExamDaoImpl implements StudentSubjectExamDao {
    private final StudentSubjectExamRepository studentSubjectExamRepository;

    public StudentSubjectExamDaoImpl(StudentSubjectExamRepository studentSubjectExamRepository) {
        this.studentSubjectExamRepository = studentSubjectExamRepository;
    }

    @Override
    public StudentSubjectExam store(StudentSubjectExam studentSubjectExam) {
        return studentSubjectExamRepository.save(studentSubjectExam);
    }

    @Override
    public StudentSubjectExam getById(Long id) {
        Optional<StudentSubjectExam> studentSubjectExam = studentSubjectExamRepository.findById(id);
        return studentSubjectExam.orElseGet(studentSubjectExam::get);
    }

    @Override
    public StudentSubjectExam getByExamIdAndStudentSubjectId(Long examId, Long studentSubjectId) {
        return studentSubjectExamRepository.findByExamIdAndAndStudentSubjectId(examId, studentSubjectId);
    }

    @Override
    public List<StudentSubjectExam> getAll() {
        List<StudentSubjectExam> studentSubjectExams =  studentSubjectExamRepository.findAll();
        if(CollectionUtils.isEmpty(studentSubjectExams)) {
            return new ArrayList<>();
        }
        return studentSubjectExams;
    }

    @Override
    public List<StudentSubjectExam> getByStudentId(Long id) {
        List<StudentSubjectExam> studentSubjectExams =  studentSubjectExamRepository.findByStudentId(id);
        if(CollectionUtils.isEmpty(studentSubjectExams)) {
            return new ArrayList<>();
        }
        return studentSubjectExams;
    }
}
