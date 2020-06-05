package vn.edu.vnu.uet.dkt.dto.service.semester;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dkt.common.Constant;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;
import vn.edu.vnu.uet.dkt.common.security.AccountService;
import vn.edu.vnu.uet.dkt.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dkt.dto.model.Semester;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;
import vn.edu.vnu.uet.dkt.rest.model.semester.ListSemesterResponse;
import vn.edu.vnu.uet.dkt.rest.model.semester.SemesterResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SemesterService {
    private final SemesterDao semesterDao;
    private final MapperFacade mapperFacade;
    private final AccountService accountService;
    private final StudentSubjectDao studentSubjectDao;

    public SemesterService(SemesterDao semesterDao, MapperFacade mapperFacade, AccountService accountService, StudentSubjectDao studentSubjectDao) {
        this.semesterDao = semesterDao;
        this.mapperFacade = mapperFacade;
        this.accountService = accountService;
        this.studentSubjectDao = studentSubjectDao;

    }

    public ListSemesterResponse getAll() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(10);
        List<Semester> semesters = semesterDao.getByStartDate(localDateTime);
        List<SemesterResponse> semesterResponses = mapperFacade.mapAsList(semesters, SemesterResponse.class);
        return new ListSemesterResponse(semesterResponses);
    }

    public SemesterResponse getActive() {
        List<Semester> semesters = semesterDao.getSemesterRegistering();
        Map<Long, Semester> semesterMap = semesters.stream().collect(Collectors.toMap(Semester::getId, x -> x));
        if (semesters.size() == 0) return null;
        DktStudent dktStudent = accountService.getUserSession();
        List<Long> semesterIds = studentSubjectDao.getSemesterStudentIn(dktStudent.getId());
        if (CollectionUtils.isEmpty(semesterIds)) return null;
        for (Long semesterId : semesterIds) {
            Semester semester = semesterMap.get(semesterId);
            if (semester != null && semester.getStatus() == Constant.REGISTERING) {
                return mapperFacade.map(semester, SemesterResponse.class);
            }

        }
        return null;
    }
}
