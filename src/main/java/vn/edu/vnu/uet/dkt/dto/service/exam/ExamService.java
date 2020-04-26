package vn.edu.vnu.uet.dkt.dto.service.exam;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dkt.dto.model.Exam;
import vn.edu.vnu.uet.dkt.rest.model.exam.ExamResponse;
import vn.edu.vnu.uet.dkt.rest.model.exam.ListExamResponse;

import java.util.List;

@Service
public class ExamService {
    private final ExamDao examDao;
    private final MapperFacade mapperFacade;

    public ExamService(ExamDao examDao, MapperFacade mapperFacade) {
        this.examDao = examDao;
        this.mapperFacade = mapperFacade;
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

}
