package vn.edu.vnu.uet.dkt.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dkt.dto.model.Exam;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findAllByRoomSemesterId(Long semesterId);
    List<Exam> findAllByLocationIdAndSubjectSemesterId(Long locationId, Long subjectSemesterId);
    List<Exam> findAllBySemesterIdAndSubjectIdIn(Long semesterId, List<Long> subjectIds);
    List<Exam> findAllByIdIn(List<Long> ids);
}