package vn.edu.vnu.uet.dkt.dto.repository.redis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dkt.dto.model.Exam;
import vn.edu.vnu.uet.dkt.dto.model.redis.RedisExam;

import java.time.LocalDate;
import java.util.List;

;

@Repository
public interface RedisExamRepository extends JpaRepository<RedisExam, Long> {
    RedisExam findByExamCode(String examCode);
    List<RedisExam> findBySemesterId(Long semesterId);
    List<RedisExam> findByRoomSemesterIdAndDate(Long roomSemesterId, LocalDate date);
    List<RedisExam> findBySemesterIdAndSubjectId(Long semesterId, Long subjectId);
    List<RedisExam> findByIdIn(List<Long> ids);
}
