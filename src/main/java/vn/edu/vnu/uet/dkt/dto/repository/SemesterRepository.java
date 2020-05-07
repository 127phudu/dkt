package vn.edu.vnu.uet.dkt.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dkt.dto.model.Semester;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

}