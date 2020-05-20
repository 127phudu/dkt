package vn.edu.vnu.uet.dkt.dto.service.exam;

import ma.glasnost.orika.MapperFacade;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;
import vn.edu.vnu.uet.dkt.common.security.AccountService;
import vn.edu.vnu.uet.dkt.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dkt.dto.model.Exam;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;
import vn.edu.vnu.uet.dkt.rest.model.exam.ExamResponse;
import vn.edu.vnu.uet.dkt.rest.model.exam.ListExamResponse;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamService {
    private final ExamDao examDao;
    private final MapperFacade mapperFacade;
    private final AccountService accountService;
    private final StudentSubjectDao studentSubjectDao;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ExamService(ExamDao examDao, MapperFacade mapperFacade, AccountService accountService, StudentSubjectDao studentSubjectDao) {
        this.examDao = examDao;
        this.mapperFacade = mapperFacade;
        this.accountService = accountService;
        this.studentSubjectDao = studentSubjectDao;
    }

    public ListExamResponse getAll(Long semesterId) {
        DktStudent dktStudent = accountService.getUserSession();
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySemesterIdAndStudentId(semesterId, dktStudent.getId());
        List<Long> subjectIds = studentSubjects.stream().map(StudentSubject::getStudentId).collect(Collectors.toList());
        List<Exam> exams = examDao.getExamBySemesterIdAndSubjectIdIn(semesterId, subjectIds);
        if (CollectionUtils.isEmpty(exams)) return null;
        return generateListExamResponse(exams);
    }

    @Cacheable(value = "exams", key = "#id")
    public ExamResponse getExam(Long id) {
        System.out.println("get from db");
        Exam exam = examDao.getById(id);
        return mapperFacade.map(exam, ExamResponse.class);
    }

/*    public ListExamResponse search(Long semesterId) {
        DktStudent dktStudent = accountService.getUserSession();
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySemesterIdAndStudentId(semesterId, dktStudent.getId());
        List<Long> subjectIds = studentSubjects.stream().map(StudentSubject::getStudentId).collect(Collectors.toList());
        List<Exam> exams = examDao.getExamBySemesterIdAndStudentIdIn(semesterId, subjectIds);

    }*/

    public boolean isExistExam(Long examId) {
        Exam exam = examDao.getById(examId);
        return exam != null;
    }
    public boolean isExistExam(String examCode) {
        Exam exam  = examDao.getByExamCode(examCode);
        return exam != null;
    }

    private ListExamResponse generateListExamResponse(List<Exam> exams) {
        Map<String, ExamResponse> response = new HashMap<>();
        for (Exam exam : exams) {
            String key = exam.getSubjectId() + " " + exam.getStartTime() + " - " + exam.getEndTime();
            if (response.containsKey(key)) {
                ExamResponse examResponse = response.get(key);
                int numStd = examResponse.getNumberOfStudent() +(exam.getNumberOfStudent() == null ? 0 : exam.getNumberOfStudent());
                int subscribe = examResponse.getNumberOfStudentSubscribe() +(exam.getNumberOfStudentSubscribe() == null ? 0 : exam.getNumberOfStudentSubscribe());
                examResponse.setNumberOfStudentSubscribe(subscribe);
                examResponse.setNumberOfStudent(numStd);
                response.put(key, examResponse);
            } else {
                ExamResponse examResponse = getExamResponse(exam);
                response.put(key, examResponse);
            }
        }
        return new ListExamResponse(new ArrayList<>(response.values()));
    }

    private ExamResponse getExamResponse(Exam exam) {
        ExamResponse examResponse = new ExamResponse();
        examResponse.setStartTime(exam.getStartTime().format(format));
        examResponse.setEndTime(exam.getEndTime().format(format));
        examResponse.setDate(exam.getDate().format(format));
        examResponse.setLocation(exam.getLocationId());
        examResponse.setNumberOfStudent(exam.getNumberOfStudent() == null ? 0 : exam.getNumberOfStudent());
        examResponse.setNumberOfStudentSubscribe(exam.getNumberOfStudentSubscribe() == null ? 0 : exam.getNumberOfStudentSubscribe());
        examResponse.setExamCode(exam.getExamCode());
        return examResponse;
    }
}
