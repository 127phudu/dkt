package vn.edu.vnu.uet.dkt.dto.service.semester;

import ma.glasnost.orika.MapperFacade;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dkt.common.Constant;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;
import vn.edu.vnu.uet.dkt.common.security.AccountService;
import vn.edu.vnu.uet.dkt.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dkt.dto.model.Semester;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;
import vn.edu.vnu.uet.dkt.rest.model.PageBase;
import vn.edu.vnu.uet.dkt.rest.model.PageResponse;
import vn.edu.vnu.uet.dkt.rest.model.semester.ListSemesterResponse;
import vn.edu.vnu.uet.dkt.rest.model.semester.SemesterResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    public ListSemesterResponse getAll(PageBase pageBase) {
        DktStudent dktStudent = accountService.getUserSession();
        List<StudentSubject> studentSubjects = studentSubjectDao.getStudentSubjectByStudentId(dktStudent.getId());
        List<Long> semesterIds = studentSubjects.stream()
                .map(StudentSubject::getSemesterId)
                .distinct().collect(Collectors.toList());
        List<Semester> semesters = semesterDao.getSemesterIn(semesterIds);
        semesters.sort(Comparator.comparing(Semester::getEndDate).reversed());

        List<SemesterResponse> semesterResponses = mapperFacade.mapAsList(semesters, SemesterResponse.class);
        return  generateSemesterPaging(semesterResponses,pageBase);
    }

    public SemesterResponse getActive() {
        DktStudent dktStudent = accountService.getUserSession();
        List<StudentSubject> studentSubjects = studentSubjectDao.getStudentSubjectByStudentId(dktStudent.getId());
        List<Long> semesterIds = studentSubjects.stream()
                .map(StudentSubject::getSemesterId)
                .distinct().collect(Collectors.toList());
        List<Semester> semesters = semesterDao.getSemesterIn(semesterIds);
        semesters.sort(Comparator.comparing(Semester::getEndDate).reversed());
        for (Semester semester : semesters) {
            if (semester.getStatus() == Constant.REGISTERING) {
                return mapperFacade.map(semester, SemesterResponse.class);
            }
        }
        for (Semester semester : semesters) {
            if (semester.getStatus() == Constant.REGISTERED) {
                return mapperFacade.map(semester, SemesterResponse.class);
            }
        }
        return null;
    }

    private ListSemesterResponse generateSemesterPaging(List<SemesterResponse> semesterResponses, PageBase pageBase) {
        List<SemesterResponse> semesterResponseList = new ArrayList<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = semesterResponses.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            semesterResponseList.add(semesterResponses.get(i));
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new ListSemesterResponse(semesterResponseList, pageResponse);
    }
}
