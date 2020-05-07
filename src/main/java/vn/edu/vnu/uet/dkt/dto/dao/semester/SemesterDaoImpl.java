package vn.edu.vnu.uet.dkt.dto.dao.semester;

import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.dto.model.Semester;
import vn.edu.vnu.uet.dkt.dto.repository.SemesterRepository;

@Service
public class SemesterDaoImpl implements SemesterDao {
    private final SemesterRepository semesterRepository;

    public SemesterDaoImpl(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    @Override
    public Semester getById(Long id) {
        return semesterRepository.findById(id).orElse(null);
    }
}
