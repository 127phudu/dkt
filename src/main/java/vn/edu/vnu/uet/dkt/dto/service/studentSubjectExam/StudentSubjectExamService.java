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
import vn.edu.vnu.uet.dkt.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubjectExam.StudentSubjectExamDao;
import vn.edu.vnu.uet.dkt.dto.model.Exam;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubjectExam;
import vn.edu.vnu.uet.dkt.dto.service.exam.ExamService;
import vn.edu.vnu.uet.dkt.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam.StudentSubjectExamRequest;
import vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam.StudentSubjectExamResponse;

@Service
public class StudentSubjectExamService {
    private final StudentSubjectExamDao studentSubjectExamDao;
    private final StudentSubjectDao studentSubjectDao;
    private final AccountService accountService;
    private final StudentSubjectService studentSubjectService;
    private final ExamService examService;
    private final MapperFacade mapperFacade;
    private final ExamDao examDao;

    public StudentSubjectExamService(StudentSubjectExamDao studentSubjectExamDao, StudentSubjectDao studentSubjectDao, AccountService accountService, StudentSubjectService studentSubjectService, ExamService examService, MapperFacade mapperFacade, ExamDao examDao) {
        this.studentSubjectExamDao = studentSubjectExamDao;
        this.studentSubjectDao = studentSubjectDao;
        this.accountService = accountService;
        this.studentSubjectService = studentSubjectService;
        this.examService = examService;
        this.mapperFacade = mapperFacade;
        this.examDao = examDao;
    }

    @Transactional
    public StudentSubjectExamResponse create(StudentSubjectExamRequest request) {
        validateStudentSubjectExam(request);
        Exam exam = examDao.getById(request.getExamId());
        StudentSubjectExam studentSubjectExam = mapperFacade.map(request, StudentSubjectExam.class);
        studentSubjectExam.setStatus(Constant.active);
        studentSubjectExam.setSemesterId(exam.getSemesterId());
        Integer numberStudent = exam.getNumberOfStudentSubscribe();
        if (numberStudent == null) {
            numberStudent = 1;
        } else {
            numberStudent += 1;
        }
        exam.setNumberOfStudentSubscribe(numberStudent);
        examDao.store(exam);
        return mapperFacade.map(studentSubjectExamDao.store(studentSubjectExam), StudentSubjectExamResponse.class);
    }

    public void validateStudentSubjectExam(StudentSubjectExamRequest request) {
        if (request.getExamId() == null) {
            throw new BadRequestException(400, "Exam không thể null");
        }
        if (request.getStudentSubjectId() == null) {
            throw new BadRequestException(400, "StudentSubject không thể null");
        }
        if (!studentSubjectService.existStudentSubject(request.getStudentSubjectId())) {
            throw new BadRequestException(400, "StudentSubject không tồn tại");
        }
        if (!examService.isExistExam(request.getExamId())) {
            throw new BadRequestException(400, "Exam không tồn tại");
        }
        if (isExistStudentSubjectExam(request.getExamId(), request.getStudentSubjectId())) {
            throw new BadRequestException(400, "StudentSubjectExam đã tồn tại");
        }
        StudentSubject studentSubject = studentSubjectDao.getById(request.getStudentSubjectId());

        DktStudent dktStudent = accountService.getUserSession();
        if (studentSubject.getStudentId() != dktStudent.getId()) {
            throw new ForbiddenException();
        }

        Exam exam = examDao.getById(request.getExamId());

        if (exam.getSubjectSemesterId() != studentSubject.getSubjectSemesterId()) {
            throw new BadRequestException(400, "StudentSubject và Exam không hợp lệ!");
        }
        Integer numberOfStudentSubscribe = exam.getNumberOfStudentSubscribe();
        if (numberOfStudentSubscribe == null) numberOfStudentSubscribe = 0;
        if (numberOfStudentSubscribe >= exam.getNumberOfStudent()) {
            throw new BadRequestException(400, "Số lượng sinh viên trong phòng đã đầy");
        }
    }

    public boolean isExistStudentSubjectExam(Long examId, Long studentSubjectId) {
        StudentSubjectExam studentSubjectExam = studentSubjectExamDao.getByExamIdAndStudentSubjectId(examId, studentSubjectId);
        return studentSubjectExam != null;
    }
}
