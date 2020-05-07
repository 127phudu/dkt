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
import vn.edu.vnu.uet.dkt.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubjectExam.StudentSubjectExamDao;
import vn.edu.vnu.uet.dkt.dto.dao.subjectSemester.SubjectSemesterDao;
import vn.edu.vnu.uet.dkt.dto.model.*;
import vn.edu.vnu.uet.dkt.dto.service.exam.ExamService;
import vn.edu.vnu.uet.dkt.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dkt.rest.model.PageBase;
import vn.edu.vnu.uet.dkt.rest.model.PageResponse;
import vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam.ListStudentSubjectExamResponse;
import vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam.StudentSubjectExamRequest;
import vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam.StudentSubjectExamResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentSubjectExamService {
    private final StudentSubjectExamDao studentSubjectExamDao;
    private final StudentSubjectDao studentSubjectDao;
    private final AccountService accountService;
    private final StudentSubjectService studentSubjectService;
    private final SubjectSemesterDao subjectSemesterDao;
    private final ExamService examService;
    private final MapperFacade mapperFacade;
    private final ExamDao examDao;
    private final SemesterDao semesterDao;

    public StudentSubjectExamService(StudentSubjectExamDao studentSubjectExamDao, StudentSubjectDao studentSubjectDao, AccountService accountService, StudentSubjectService studentSubjectService, SubjectSemesterDao subjectSemesterDao, ExamService examService, MapperFacade mapperFacade, ExamDao examDao, SemesterDao semesterDao) {
        this.studentSubjectExamDao = studentSubjectExamDao;
        this.studentSubjectDao = studentSubjectDao;
        this.accountService = accountService;
        this.studentSubjectService = studentSubjectService;
        this.subjectSemesterDao = subjectSemesterDao;
        this.examService = examService;
        this.mapperFacade = mapperFacade;
        this.examDao = examDao;
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
        studentSubjectExam.setStatus(Constant.active);
        studentSubjectExam.setExamId(exam.getId());
        studentSubjectExam.setSemesterId(exam.getSemesterId());
        Integer numberStudent = exam.getNumberOfStudentSubscribe();
        if (numberStudent == null) {
            numberStudent = 1;
        } else {
            numberStudent += 1;
        }
        exam.setNumberOfStudentSubscribe(numberStudent);
        examDao.store(exam);
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
        if (studentSubject.getStudentId() != dktStudent.getId()) {
            throw new ForbiddenException();
        }
    }

    public void delete(Long id) {
        StudentSubjectExam studentSubjectExam = studentSubjectExamDao.getById(id);
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
        ListStudentSubjectExamResponse studentSubjectExamResponse = new ListStudentSubjectExamResponse(
                mapperFacade.mapAsList(studentSubjectExamList, StudentSubjectExamResponse.class),
                pageResponse
        );
        return studentSubjectExamResponse;
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
