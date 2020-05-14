package vn.edu.vnu.uet.dkt.dto.service.exam;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;
import vn.edu.vnu.uet.dkt.common.security.AccountService;
import vn.edu.vnu.uet.dkt.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dkt.dto.model.Exam;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;
import vn.edu.vnu.uet.dkt.rest.model.PageBase;
import vn.edu.vnu.uet.dkt.rest.model.exam.ExamResponse;
import vn.edu.vnu.uet.dkt.rest.model.exam.ListExamResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {
    private final ExamDao examDao;
    private final MapperFacade mapperFacade;
    private final AccountService accountService;
    private final StudentSubjectDao studentSubjectDao;

    public ExamService(ExamDao examDao, MapperFacade mapperFacade, AccountService accountService, StudentSubjectDao studentSubjectDao) {
        this.examDao = examDao;
        this.mapperFacade = mapperFacade;
        this.accountService = accountService;
        this.studentSubjectDao = studentSubjectDao;
    }

    public ListExamResponse getAll(Long semesterId) {
        List<Exam> exams = examDao.getAllBySemesterId(semesterId);
        List<ExamResponse> examResponses = mapperFacade.mapAsList(exams, ExamResponse.class);
        return new ListExamResponse(examResponses);
    }

    public ExamResponse getExam(Long id) {
        Exam exam = examDao.getById(id);
        return mapperFacade.map(exam, ExamResponse.class);
    }

    public ExamResponse search(Long semesterId, String query, PageBase pageBase) {
        DktStudent dktStudent = accountService.getUserSession();
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySemesterIdAndStudentId(semesterId, dktStudent.getId());
        List<Long> subjectIds = studentSubjects.stream().map(StudentSubject::getStudentId).collect(Collectors.toList());
        List<Exam> exams = examDao.getExamBySemesterIdAndStudentIdIn(semesterId, subjectIds);

    }

    public boolean isExistExam(Long examId) {
        Exam exam = examDao.getById(examId);
        return exam != null;
    }
    public boolean isExistExam(String examCode) {
        Exam exam  = examDao.getByExamCode(examCode);
        return exam != null;
    }
}
