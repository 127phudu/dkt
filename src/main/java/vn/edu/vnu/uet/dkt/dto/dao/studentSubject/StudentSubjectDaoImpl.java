package vn.edu.vnu.uet.dkt.dto.dao.studentSubject;

import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;
import vn.edu.vnu.uet.dkt.dto.repository.StudentSubjectRepository;

import java.util.List;

@Service
public class StudentSubjectDaoImpl implements StudentSubjectDao {
    private final StudentSubjectRepository studentSubjectRepository;

    public StudentSubjectDaoImpl(StudentSubjectRepository studentSubjectRepository) {
        this.studentSubjectRepository = studentSubjectRepository;
    }

    @Override
    public List<StudentSubject> getAll() {
        return studentSubjectRepository.findAll();
    }

    @Override
    public StudentSubject store(StudentSubject studentSubject) {
        return studentSubjectRepository.save(studentSubject);
    }

    @Override
    public StudentSubject getByStudentAndSubjectSemesterId(Long studentId, Long subjectSemesterId) {
        return studentSubjectRepository.findByStudentIdAndSubjectSemesterId(studentId, subjectSemesterId);
    }

    @Override
    public StudentSubject getByIdAndSubjectId(Long id, Long studentId) {
        return studentSubjectRepository.findByIdAndStudentId(id, studentId);
    }

    @Override
    public StudentSubject getById(Long id) {
        return studentSubjectRepository.findById(id).orElse(null);
    }

    @Override
    public List<StudentSubject> getBySemesterIdAndStudentId(Long semesterId, Long studentId) {
        return studentSubjectRepository.findBySemesterIdAndStudentId(semesterId, studentId);
    }

    @Override
    public List<StudentSubject> getByStudentId(Long studentId) {
        return studentSubjectRepository.findByStudentId(studentId);
    }

    @Override
    public List<StudentSubject> getSemesterStudentIn(Long studentId) {
        return studentSubjectRepository.findDistinctSemesterIdByStudentId(studentId);
    }
}
