package vn.edu.vnu.uet.dkt.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dkt.dto.model.SubjectSemester;

@Repository
public interface SubjectSemesterRepository extends JpaRepository<SubjectSemester, Long> {
    SubjectSemester findBySemesterIdAndSubjectId(Long semesterId, Long subjectId);
}
