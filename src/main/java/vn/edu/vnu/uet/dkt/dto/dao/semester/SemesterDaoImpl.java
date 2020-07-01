package vn.edu.vnu.uet.dkt.dto.dao.semester;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dkt.common.Constant;
import vn.edu.vnu.uet.dkt.dto.model.Semester;
import vn.edu.vnu.uet.dkt.dto.repository.SemesterRepository;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SemesterDaoImpl implements SemesterDao {
    private final SemesterRepository semesterRepository;

    public SemesterDaoImpl(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Semester getById(Long id) {
        return semesterRepository.findById(id).orElse(null);
    }

    @Override
    public List<Semester> getByStartDate(LocalDateTime dateTime) {
        List<Semester> semesters = semesterRepository.findByStartDateAfter(dateTime);
        if (CollectionUtils.isEmpty(semesters)) {
            return new ArrayList<>();
        }
        return semesters;
    }

    @Override
    public List<Semester> getSemesterRegistering() {
        List<Semester> semesters = semesterRepository.findByStatus(Constant.REGISTERING);
        if (CollectionUtils.isEmpty(semesters)) {
            return new ArrayList<>();
        }
        return semesters;
    }

    @Override
    public List<Semester> getSemesterRegistered() {
        List<Semester> semesters = semesterRepository.findByStatus(Constant.REGISTERED);
        if (CollectionUtils.isEmpty(semesters)) {
            return new ArrayList<>();
        }
        return semesters;
    }

    @Override
    public List<Semester> getSemesterIn(List<Long> ids) {
        List<Semester> semesters = semesterRepository.findByIdIn(ids);
        if (CollectionUtils.isEmpty(semesters)) {
            return new ArrayList<>();
        }
        return semesters;
    }
}
