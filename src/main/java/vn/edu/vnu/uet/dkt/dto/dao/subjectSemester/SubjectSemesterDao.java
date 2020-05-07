package vn.edu.vnu.uet.dkt.dto.dao.subjectSemester;

import vn.edu.vnu.uet.dkt.dto.model.SubjectSemester;

public interface SubjectSemesterDao {
    SubjectSemester getBySubjectIdAndSemesterId(Long subjectId, Long semesterId);
}
