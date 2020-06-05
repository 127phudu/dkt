package vn.edu.vnu.uet.dkt.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;

import java.util.List;

@Repository
public interface StudentSubjectRepository extends JpaRepository<StudentSubject, Long> {
    StudentSubject findByStudentIdAndSubjectSemesterId(Long studentId, Long subjectSemesterId);
    List<StudentSubject> findBySemesterId(Long semesterId);
    StudentSubject findByIdAndStudentId(Long id, Long studentId);
    List<StudentSubject> findBySemesterIdAndStudentId(Long semesterId, Long studentId);
    List<StudentSubject> findByStudentId(Long studentId);
    List<StudentSubject> findDistinctSemesterIdByStudentId(Long studentId);
}
