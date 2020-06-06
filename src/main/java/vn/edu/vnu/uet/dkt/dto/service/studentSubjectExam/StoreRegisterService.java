package vn.edu.vnu.uet.dkt.dto.service.studentSubjectExam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreRegisterService {

    private static final Logger log = LoggerFactory.getLogger(StoreRegisterService.class);
    private final StudentSubjectExamDao studentSubjectExamDao;
    private final StudentSubjectDao studentSubjectDao;
    private final ExamDao examDao;

    public StoreRegisterService(StudentSubjectExamDao studentSubjectExamDao, StudentSubjectDao studentSubjectDao, ExamDao examDao) {
        this.studentSubjectExamDao = studentSubjectExamDao;
        this.studentSubjectDao = studentSubjectDao;
        this.examDao = examDao;
    }

    @Transactional
    public void cancel(List<RegisterModel> cancelModel, DktStudent dktStudent) {
        List<Long> getStudentSubjectIds = cancelModel.stream().map(RegisterModel::getSubjectSemesterId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(getStudentSubjectIds)) return;
        List<Long> cancelList = new ArrayList<>();
        for (Long studentSubjectId : getStudentSubjectIds) {
            StudentSubject studentSubject = studentSubjectDao.getById(studentSubjectId);
            if (studentSubject == null || !dktStudent.getId().equals(studentSubject.getStudentId())) continue;
            cancelList.add(studentSubjectId);
        }
        if (CollectionUtils.isEmpty(cancelList)) return;
        List<StudentSubjectExam> studentSubjectExams = studentSubjectExamDao.getByStudentSubjectIdIn(cancelList);
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

        examDao.store(exam);
        studentSubjectDao.store(studentSubject);
        studentSubjectExamDao.store(studentSubjectExam);
    }
}
