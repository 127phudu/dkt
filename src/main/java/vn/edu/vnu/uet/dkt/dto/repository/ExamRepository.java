package vn.edu.vnu.uet.dkt.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dkt.dto.model.Exam;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findAllByRoomSemesterId(Long semesterId);
    List<Exam> findAllByLocationIdAndSubjectSemesterId(Long locationId, Long subjectSemesterId);
    List<Exam> findAllBySemesterIdAndSubjectIdIn(Long semesterId, List<Long> subjectIds);
    List<Exam> findAllBySubjectSemesterId(Long subjectSemesterId);
    List<Exam> findAllByIdIn(List<Long> ids);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Exam> findById(Long id);
    List<Exam> findAllBySemesterId(Long semesterId);
}