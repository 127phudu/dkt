package vn.edu.vnu.uet.dkt.dto.service.studentSubjectExam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dkt.common.model.DktStudent;
import vn.edu.vnu.uet.dkt.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dkt.dto.dao.studentSubjectExam.StudentSubjectExamDao;
import vn.edu.vnu.uet.dkt.dto.model.Exam;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubject;
import vn.edu.vnu.uet.dkt.dto.model.StudentSubjectExam;
import vn.edu.vnu.uet.dkt.rest.model.studentSubjectExam.RegisterModel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@Service
public class StoreRegisterService {

    private static final Logger log = LoggerFactory.getLogger(StoreRegisterService.class);
    private final StudentSubjectExamDao studentSubjectExamDao;
    private final StudentSubjectDao studentSubjectDao;
    private final ExamDao examDao;
    private final Lock lock = new ReentrantLock();

    public StoreRegisterService(StudentSubjectExamDao studentSubjectExamDao, StudentSubjectDao studentSubjectDao, ExamDao examDao) {
        this.studentSubjectExamDao = studentSubjectExamDao;
        this.studentSubjectDao = studentSubjectDao;
        this.examDao = examDao;
    }

    @Transactional
    public void cancel(List<RegisterModel> cancelModel, DktStudent dktStudent) {
        List<Long> getSubjectSemesterIds = cancelModel.stream().map(RegisterModel::getSubjectSemesterId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(getSubjectSemesterIds)) return;
        List<Long> cancelList = new ArrayList<>();
        for (Long subjectSemesterId : getSubjectSemesterIds) {
            StudentSubject studentSubject = studentSubjectDao.getByStudentAndSubjectSemesterId(dktStudent.getId(),subjectSemesterId);
            cancelList.add(studentSubject.getId());
        }
        if (CollectionUtils.isEmpty(cancelList)) return;
        List<StudentSubjectExam> studentSubjectExams = studentSubjectExamDao.getByStudentSubjectIdIn(cancelList);
        List<Long> examIds = studentSubjectExams.stream().map(StudentSubjectExam::getExamId).collect(Collectors.toList());
        List<Exam> exams = examDao.getByExamIdIn(examIds);
        for (Exam exam : exams) {
            int numberStudentSubscribe = exam.getNumberOfStudentSubscribe() == null ? 0 : exam.getNumberOfStudentSubscribe();
            if (numberStudentSubscribe == 0) continue;
            numberStudentSubscribe--;
            exam.setNumberOfStudentSubscribe(numberStudentSubscribe);
            examDao.store(exam);
        }
        studentSubjectExamDao.deleteAll(studentSubjectExams);
    }

    @Transactional
    public void storeAndValidate(Exam slot, StudentSubject studentSubject) {
        Exam exam = examDao.getById(slot.getId());
        int numberStudent = exam.getNumberOfStudent();
        int numberStudentSubscribe = exam.getNumberOfStudentSubscribe() == null ? 0 : exam.getNumberOfStudentSubscribe();
        if (numberStudentSubscribe >= numberStudent) return;
        numberStudentSubscribe++;
        exam.setNumberOfStudentSubscribe(numberStudentSubscribe);

        studentSubject.setIsRegistered(true);

        StudentSubjectExam studentSubjectExam = new StudentSubjectExam();
        studentSubjectExam.setExamId(exam.getId());
        studentSubjectExam.setSemesterId(exam.getSemesterId());
        studentSubjectExam.setStudentId(studentSubject.getStudentId());
        studentSubjectExam.setStudentSubjectId(studentSubject.getId());
        studentSubjectExam.setSubjectSemesterId(studentSubject.getSubjectSemesterId());
        log.info("number subscribe : {}, studentSubjectId: {}", numberStudentSubscribe, studentSubject.getId() );
        examDao.store(exam);
        studentSubjectDao.store(studentSubject);
        studentSubjectExamDao.store(studentSubjectExam);
    }
}
