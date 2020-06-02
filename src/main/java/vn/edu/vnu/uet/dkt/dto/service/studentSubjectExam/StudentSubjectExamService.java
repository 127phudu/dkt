package vn.edu.vnu.uet.dkt.dto.service.studentSubjectExam;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.vnu.uet.dkt.common.Constant;
import vn.edu.vnu.uet.dkt.common.exception.BadRequestException;
import vn.edu.vnu.uet.dkt.common.exception.ForbiddenException;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;
import vn.edu.vnu.uet.dkt.common.security.AccountService;
import vn.edu.vnu.uet.dkt.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dkt.dto.dao.location.LocationDao;
import vn.edu.vnu.uet.dkt.dto.dao.room.RoomDao;
import vn.edu.vnu.uet.dkt.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubjectExam.StudentSubjectExamDao;
import vn.edu.vnu.uet.dkt.dto.dao.subject.SubjectDao;
import vn.edu.vnu.uet.dkt.dto.dao.subjectSemester.SubjectSemesterDao;
import vn.edu.vnu.uet.dkt.dto.model.*;
import vn.edu.vnu.uet.dkt.dto.service.exam.ExamService;
import vn.edu.vnu.uet.dkt.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dkt.rest.model.PageBase;
import vn.edu.vnu.uet.dkt.rest.model.PageResponse;
import vn.edu.vnu.uet.dkt.rest.model.exam.ExamResponse;
import vn.edu.vnu.uet.dkt.rest.model.exam.ListExamResponse;
import vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentSubjectExamService {
    private final StudentSubjectExamDao studentSubjectExamDao;
    private final StudentSubjectDao studentSubjectDao;
    private final AccountService accountService;
    private final StudentSubjectService studentSubjectService;
    private final SubjectSemesterDao subjectSemesterDao;
    private final ExamService examService;
    private final SubjectDao subjectDao;
    private final LocationDao locationDao;
    private final MapperFacade mapperFacade;
    private final ExamDao examDao;
    private final RoomDao roomDao;
    private final SemesterDao semesterDao;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public StudentSubjectExamService(StudentSubjectExamDao studentSubjectExamDao, StudentSubjectDao studentSubjectDao, AccountService accountService, StudentSubjectService studentSubjectService, SubjectSemesterDao subjectSemesterDao, ExamService examService, SubjectDao subjectDao, LocationDao locationDao, MapperFacade mapperFacade, ExamDao examDao, RoomDao roomDao, SemesterDao semesterDao) {
        this.studentSubjectExamDao = studentSubjectExamDao;
        this.studentSubjectDao = studentSubjectDao;
        this.accountService = accountService;
        this.studentSubjectService = studentSubjectService;
        this.subjectSemesterDao = subjectSemesterDao;
        this.examService = examService;
        this.subjectDao = subjectDao;
        this.locationDao = locationDao;
        this.mapperFacade = mapperFacade;
        this.examDao = examDao;
        this.roomDao = roomDao;
        this.semesterDao = semesterDao;
    }

    @Transactional
    public StudentSubjectExamResponse create(StudentSubjectExamRequest request) {
        validateStudentSubjectExam(request);
        StudentSubject studentSubject = studentSubjectDao.getById(request.getStudentSubjectId());
        SubjectSemester subjectSemester = subjectSemesterDao.getBySubjectIdAndSemesterId(
                studentSubject.getSubjectId(),
                studentSubject.getSemesterId()
        );

        Exam exam = getExamIdByLocation(request.getLocationId(), subjectSemester.getId());
        StudentSubjectExam studentSubjectExam = mapperFacade.map(request, StudentSubjectExam.class);
        studentSubjectExam.setExamId(exam.getId());
        studentSubjectExam.setSemesterId(exam.getSemesterId());
        studentSubjectExam.setStudentId(studentSubject.getStudentId());
        Integer numberStudent = exam.getNumberOfStudentSubscribe();
        if (numberStudent == null) {
            numberStudent = 1;
        } else {
            numberStudent += 1;
        }
        exam.setNumberOfStudentSubscribe(numberStudent);
        examDao.store(exam);

        studentSubject.setIsRegistered(true);
        studentSubjectDao.store(studentSubject);
        StudentSubjectExamResponse response = mapperFacade.map(
                studentSubjectExamDao.store(studentSubjectExam),
                StudentSubjectExamResponse.class
        );
        response.setLocationId(exam.getLocationId());
        return response;
    }

    public ListStudentSubjectExamResponse getAllByStudentId(PageBase pageBase) {
        DktStudent dktStudent = accountService.getUserSession();
        List<StudentSubjectExam> studentSubjectExams = studentSubjectExamDao.getByStudentId(dktStudent.getId());
        return getStudentExamPaging(studentSubjectExams, pageBase);
    }

    public ListRegisterResultResponse getResult(Long semesterId, PageBase pageBase) {
        DktStudent dktStudent = accountService.getUserSession();
        List<StudentSubjectExam> studentExams = studentSubjectExamDao.getByStudentIdAndSemesterId(dktStudent.getId(), semesterId);
        List<Long> examIds = studentExams.stream().map(StudentSubjectExam::getExamId).collect(Collectors.toList());
        List<Exam> exams = examDao.getByExamIdIn(examIds);
        List<Location> locations = locationDao.getAll();
        Map<Long, Location> locationMap = locations.stream().collect(Collectors.toMap(Location::getId, x -> x));
        List<Room> rooms = roomDao.getAll();
        Map<Long, Room> roomMap = rooms.stream().collect(Collectors.toMap(Room::getId, x -> x));
        List<Subject> subjects =  subjectDao.getAll();
        Map<Long, Subject> subjectMap = subjects.stream().collect(Collectors.toMap(Subject::getId, x -> x));
        List<RegisterResultResponse> resultResponses = exams.stream().map(exam -> getResultResponse(exam, locationMap, subjectMap, roomMap)).collect(Collectors.toList());
        return generateResponse(resultResponses, pageBase);
    }

    public ListRegisterResultResponse generateResponse(List<RegisterResultResponse> resultResponses, PageBase pageBase) {
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int begin = (page -1) * size;
        int total = resultResponses.size();
        int maxSize = Math.min(total, size * page);
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new ListRegisterResultResponse(resultResponses.subList(begin, maxSize), pageResponse);
    }

    public RegisterResultResponse getResultResponse(Exam exam, Map<Long, Location> locationMap, Map<Long, Subject> subjectMap, Map<Long, Room> roomMap) {
        RegisterResultResponse resultResponse = new RegisterResultResponse();
        resultResponse.setStartTime(exam.getStartTime().format(format));
        resultResponse.setEndTime(exam.getEndTime().format(format));
        resultResponse.setDate(exam.getDate().format(formatDate));

        Location location = locationMap.get(exam.getLocationId());
        resultResponse.setLocationId(exam.getLocationId());
        resultResponse.setLocation(location.getLocationName());
        Subject subject = subjectMap.get(exam.getSubjectId());
        resultResponse.setSubjectName(subject.getSubjectName());
        resultResponse.setSubjectCode(subject.getSubjectCode());
        resultResponse.setStudentSubjectId(exam.getSubjectSemesterId());

        Room room = roomMap.get(exam.getRoomId());
        resultResponse.setRoomName(room.getRoomName());
        return resultResponse;
    }

    public void validateStudentSubjectExam(StudentSubjectExamRequest request) {

        if (request.getStudentSubjectId() == null) {
            throw new BadRequestException(400, "StudentSubject không thể null");
        }
        if (!studentSubjectService.existStudentSubject(request.getStudentSubjectId())) {
            throw new BadRequestException(400, "StudentSubject không tồn tại");
        }
        StudentSubject studentSubject = studentSubjectDao.getById(request.getStudentSubjectId());
        Semester semester = semesterDao.getById(studentSubject.getSemesterId());

        if (semester.getStatus() == null || semester.getStatus().equals(Constant.inActive)) {
            throw new BadRequestException(400, "Chưa đến giờ đăng ký thi");
        }
        DktStudent dktStudent = accountService.getUserSession();
        if (!studentSubject.getStudentId().equals(dktStudent.getId())) {
            throw new ForbiddenException();
        }
    }

    public void delete(Long id) {
        StudentSubjectExam studentSubjectExam = studentSubjectExamDao.getById(id);
        Exam exam = examDao.getById(studentSubjectExam.getExamId());
        int numberStudent = exam.getNumberOfStudentSubscribe() == null ? 0 : exam.getNumberOfStudentSubscribe();
        if (numberStudent == 0) {
            exam.setNumberOfStudentSubscribe(0);
        } else {
            exam.setNumberOfStudentSubscribe(numberStudent - 1);
        }
        examDao.store(exam);

        StudentSubject studentSubject = studentSubjectDao.getById(studentSubjectExam.getStudentSubjectId());
        studentSubject.setIsRegistered(false);
        studentSubjectDao.store(studentSubject);
        studentSubjectExamDao.delete(studentSubjectExam);
    }

    public boolean isExistStudentSubjectExam(Long examId, Long studentSubjectId) {
        StudentSubjectExam studentSubjectExam = studentSubjectExamDao.getByExamIdAndStudentSubjectId(examId, studentSubjectId);
        return studentSubjectExam != null;
    }

    private ListStudentSubjectExamResponse getStudentExamPaging(List<StudentSubjectExam> studentSubjectExams, PageBase pageBase) {
        List<StudentSubjectExam> studentSubjectExamList = new ArrayList<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = studentSubjectExams.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            studentSubjectExamList.add(studentSubjectExams.get(i));
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new ListStudentSubjectExamResponse(
                mapperFacade.mapAsList(studentSubjectExamList, StudentSubjectExamResponse.class),
                pageResponse
        );
    }

    private Exam getExamIdByLocation(Long locationId, Long subjectSemesterId) {
        List<Exam> exams = examDao.getExamByLocationAndSubjectSemester(locationId, subjectSemesterId);
        Exam slot = findSlot(exams);
        if (slot == null) {
            throw new BadRequestException(400, "Không còn chỗ đăng ký");
        }
        return slot;
    }

    private Exam findSlot(List<Exam> exams) {
        for (Exam exam : exams) {
            if (exam.getNumberOfStudentSubscribe() == null || exam.getNumberOfStudentSubscribe() < exam.getNumberOfStudent()) {
                return exam;
            }
        }
        return null;
    }
}
