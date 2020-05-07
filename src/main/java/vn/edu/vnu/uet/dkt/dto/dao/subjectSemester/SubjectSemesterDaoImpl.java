package vn.edu.vnu.uet.dkt.dto.dao.subjectSemester;

import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.dto.model.SubjectSemester;
import vn.edu.vnu.uet.dkt.dto.repository.SubjectSemesterRepository;

@Service
public class SubjectSemesterDaoImpl implements SubjectSemesterDao{
    private final SubjectSemesterRepository subjectSemesterRepository;

    public SubjectSemesterDaoImpl(SubjectSemesterRepository subjectSemesterRepository) {
        this.subjectSemesterRepository = subjectSemesterRepository;
    }

    @Override
    public SubjectSemester getBySubjectIdAndSemesterId(Long subjectId, Long semesterId) {
        return subjectSemesterRepository.findBySemesterIdAndSemesterId(semesterId, subjectId);
    }
}
