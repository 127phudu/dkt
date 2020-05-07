package vn.edu.vnu.uet.dkt.dto.service.studentSubject;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.common.security.AccountService;
import vn.edu.vnu.uet.dkt.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;
import vn.edu.vnu.uet.dkt.rest.model.PageBase;
import vn.edu.vnu.uet.dkt.rest.model.PageResponse;
import vn.edu.vnu.uet.dkt.rest.model.studentSubject.ListStudentSubjectResponse;
import vn.edu.vnu.uet.dkt.rest.model.studentSubject.StudentSubjectResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentSubjectService {
    private final StudentSubjectDao studentSubjectDao;
    private final StudentDao studentDao;
    private final MapperFacade mapperFacade;
    private final AccountService accountService;

    public StudentSubjectService(MapperFacade mapperFacade, StudentSubjectDao studentSubjectDao, StudentDao studentDao, AccountService accountService) {
        this.mapperFacade = mapperFacade;
        this.studentSubjectDao = studentSubjectDao;
        this.studentDao = studentDao;
        this.accountService = accountService;
    }

    public StudentSubject create(StudentSubject request) {
        return studentSubjectDao.store(request);
    }

    public List<StudentSubject> getAll() {
        return studentSubjectDao.getAll();
    }

    public ListStudentSubjectResponse getStudentSubjectBySemesterId(Long id, PageBase pageBase) {
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySemesterId(id);
        return getStudentSubjectPaging(studentSubjects, pageBase);
    }

    private ListStudentSubjectResponse getStudentSubjectPaging(List<StudentSubject> studentSubject, PageBase pageBase) {
        List<StudentSubject> studentSubjectList = new ArrayList<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = studentSubject.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            studentSubjectList.add(studentSubject.get(i));
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new ListStudentSubjectResponse(
                mapperFacade.mapAsList(studentSubjectList, StudentSubjectResponse.class),
                pageResponse
        );
    }

    public StudentSubjectResponse getById(Long id) {
        return mapperFacade.map(studentSubjectDao.getById(id), StudentSubjectResponse.class);
    }

    public boolean existStudentSubject(Long studentId, Long subjectSemesterId) {
        StudentSubject studentSubject = studentSubjectDao.getByStudentAndSubjectSemesterId(studentId, subjectSemesterId);
        return studentSubject != null;
    }
    public boolean existStudentSubject(Long id) {
        StudentSubject studentSubject = studentSubjectDao.getById(id);
        return studentSubject != null;
    }

}
