package vn.edu.vnu.uet.dkt.dto.service.studentSubject;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.common.security.AccountService;
import vn.edu.vnu.uet.dkt.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;

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


    public boolean existStudentSubject(Long studentId, Long subjectSemesterId) {
        StudentSubject studentSubject = studentSubjectDao.getByStudentAndSubjectSemesterId(studentId, subjectSemesterId);
        return studentSubject != null;
    }
    public boolean existStudentSubject(Long id) {
        StudentSubject studentSubject = studentSubjectDao.getById(id);
        return studentSubject != null;
    }

}
