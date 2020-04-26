package vn.edu.vnu.uet.dkt.dto.dao.studentSubject;

import org.springframework.beans.factory.annotation.Autowired;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;
import vn.edu.vnu.uet.dkt.dto.repository.StudentSubjectRepository;

import java.util.List;
import java.util.Optional;

public class StudentSubjectDaoImpl implements StudentSubjectDao {
    @Autowired
    private StudentSubjectRepository studentSubjectRepository;

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
    public StudentSubject getById(Long id) {
        Optional<StudentSubject> studentSubject = studentSubjectRepository.findById(id);
        return studentSubject.orElseGet(studentSubject::get);
    }
}
