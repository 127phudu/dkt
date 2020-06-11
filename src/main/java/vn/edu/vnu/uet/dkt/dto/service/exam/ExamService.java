package vn.edu.vnu.uet.dkt.dto.service.exam;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;
import vn.edu.vnu.uet.dkt.common.security.AccountService;
import vn.edu.vnu.uet.dkt.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dkt.dto.dao.location.LocationDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubjectExam.StudentSubjectExamDao;
import vn.edu.vnu.uet.dkt.dto.dao.subject.SubjectDao;
import vn.edu.vnu.uet.dkt.dto.model.*;
import vn.edu.vnu.uet.dkt.rest.model.PageBase;
import vn.edu.vnu.uet.dkt.rest.model.PageResponse;
import vn.edu.vnu.uet.dkt.rest.model.exam.ExamResponse;
import vn.edu.vnu.uet.dkt.rest.model.exam.ListExamResponse;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamService {
    private final ExamDao examDao;
    private final MapperFacade mapperFacade;
    private final AccountService accountService;
    private final StudentSubjectDao studentSubjectDao;
    private final LocationDao locationDao;
    private final SubjectDao subjectDao;
    private final StudentSubjectExamDao studentSubjectExamDao;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ExamService(ExamDao examDao, MapperFacade mapperFacade, AccountService accountService, StudentSubjectDao studentSubjectDao, LocationDao locationDao, SubjectDao subjectDao, StudentSubjectExamDao studentSubjectExamDao) {
        this.examDao = examDao;
        this.mapperFacade = mapperFacade;
        this.accountService = accountService;
        this.studentSubjectDao = studentSubjectDao;
        this.locationDao = locationDao;
        this.subjectDao = subjectDao;
        this.studentSubjectExamDao = studentSubjectExamDao;
    }

    public ListExamResponse getAll(Long semesterId, PageBase pageBase) {
        DktStudent dktStudent = accountService.getUserSession();
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySemesterIdAndStudentId(semesterId, dktStudent.getId());
        List<Long> subjectIds = studentSubjects.stream().map(StudentSubject::getSubjectId).collect(Collectors.toList());
        List<Exam> exams = examDao.getExamBySemesterIdAndSubjectIdIn(semesterId, subjectIds);
        if (CollectionUtils.isEmpty(exams)) return null;
        List<ExamResponse> examResponses = groupExam(exams);
        return generateListExamResponse(examResponses, pageBase);
    }

    public List<ExamResponse> groupExam(List<Exam> exams) {
        List<Location> locations = locationDao.getAll();
        Map<Long, Location> locationMap = locations.stream().collect(Collectors.toMap(Location::getId, x -> x));
        List<Subject> subjects =  subjectDao.getAll();
        Map<Long, Subject> subjectMap = subjects.stream().collect(Collectors.toMap(Subject::getId, x -> x));
        Map<String, ExamResponse> examMap = new HashMap<>();
        for (Exam exam : exams) {
            ExamResponse examResponse = new ExamResponse();
            String key = exam.getLocationId()+"-"+exam.getSubjectId()+"-"+exam.getStartTime()+"-"+exam.getEndTime();
            if (examMap.containsKey(key)) {
                examResponse = examMap.get(key);
                int numStd = examResponse.getNumberOfStudent() +(exam.getNumberOfStudent() == null ? 0 : exam.getNumberOfStudent());
                int subscribe = examResponse.getNumberOfStudentSubscribe() +(exam.getNumberOfStudentSubscribe() == null ? 0 : exam.getNumberOfStudentSubscribe());
                examResponse.setNumberOfStudentSubscribe(subscribe);
                examResponse.setNumberOfStudent(numStd);
            } else {
                examResponse = getExamResponse(exam, locationMap, subjectMap);
                examMap.put(key,examResponse);
            }
        }
        List<ExamResponse> responses = new ArrayList<>(examMap.values());
        responses = responses.stream().sorted(Comparator.comparingLong(ExamResponse::getSubjectSemesterId)).collect(Collectors.toList());
        return responses;
    }

    public ExamResponse getExam(Long id) {
        Exam exam = examDao.getById(id);
        return mapperFacade.map(exam, ExamResponse.class);
    }

    public ListExamResponse generateListExamResponse(List<ExamResponse> examResponses, PageBase pageBase) {
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int begin = (page -1) * size;
        int total = examResponses.size();
        int maxSize = Math.min(total, size * page);
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new ListExamResponse(examResponses.subList(begin, maxSize), pageResponse);
    }

    public boolean isExistExam(Long examId) {
        Exam exam = examDao.getById(examId);
        return exam != null;
    }

    public ExamResponse getExamResponse(Exam exam, Map<Long, Location> locationMap, Map<Long, Subject> subjectMap) {
        ExamResponse examResponse = new ExamResponse();
        examResponse.setStartTime(exam.getStartTime().format(format));
        examResponse.setEndTime(exam.getEndTime().format(format));
        examResponse.setDate(exam.getDate().format(formatDate));

        Location location = locationMap.get(exam.getLocationId());
        examResponse.setLocationId(exam.getLocationId());
        examResponse.setLocation(location.getLocationName());
        examResponse.setNumberOfStudent(exam.getNumberOfStudent() == null ? 0 : exam.getNumberOfStudent());
        examResponse.setNumberOfStudentSubscribe(exam.getNumberOfStudentSubscribe() == null ? 0 : exam.getNumberOfStudentSubscribe());
        Subject subject = subjectMap.get(exam.getSubjectId());
        examResponse.setSubjectName(subject.getSubjectName());
        examResponse.setSubjectCode(subject.getSubjectCode());
        examResponse.setNumberOfCredit(subject.getNumberOfCredit());
        examResponse.setSubjectSemesterId(exam.getSubjectSemesterId());
        return examResponse;
    }
}
