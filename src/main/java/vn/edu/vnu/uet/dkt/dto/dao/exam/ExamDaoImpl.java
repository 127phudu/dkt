package vn.edu.vnu.uet.dkt.dto.dao.exam;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dkt.dto.model.Exam;
import vn.edu.vnu.uet.dkt.dto.model.Semester;
import vn.edu.vnu.uet.dkt.dto.model.redis.RedisExam;
import vn.edu.vnu.uet.dkt.dto.repository.ExamRepository;
import vn.edu.vnu.uet.dkt.dto.repository.SemesterRepository;
import vn.edu.vnu.uet.dkt.dto.repository.redis.RedisExamRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class ExamDaoImpl implements ExamDao {
    private final ExamRepository examRepository;
    private final SemesterRepository semesterRepository;
    private final RedisExamRepository redisExamRepository;
    private final MapperFacade mapperFacade;

    public ExamDaoImpl(ExamRepository examRepository, RedisExamRepository redisExamRepository,MapperFacade mapperFacade, SemesterRepository semesterRepository) {
        this.examRepository = examRepository;
        this.redisExamRepository = redisExamRepository;
        this.semesterRepository = semesterRepository;
        this.mapperFacade = mapperFacade;
    }

    @Override
    public Exam store(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    public Exam getById(Long id) {
        RedisExam redisExam = redisExamRepository.findById(id).orElse(null);
        return mapperFacade.map(redisExam, Exam.class);

//        return examRepository.findById(id).orElse(null);
    }

//    @Override
//    public long redisTrans(Long id) {
//        RedisExam redisExam = redisExamRepository.findById(id).orElse(null);
//        Long location_id = redisExam.getLocationId();
//        if (location_id < 450) {
//            redisExam.setLocationId(location_id + 1);
////            redisExamRepository.save(redisExam);
//        } else {
//            return 0;
//        }
//        return location_id;
//    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public long redisTrans(Long id) {
        Exam exam = examRepository.findById(id).orElse(null);
        Long location_id = exam.getLocationId();
        if (location_id < 150) {
            exam.setLocationId(location_id + 1);
            Semester semester = new Semester();
            semester.setSemesterCode("123");
            semester.setSemesterName("Lôn mồn");
            semester.setDescription("Mồn lèo");
            semesterRepository.save(semester);
            examRepository.save(exam);
        } else {
            return 0;
        }
        return location_id;

//        return examRepository.findById(id).orElse(null);
    }

    @Override
    public Exam getByExamCode(String examCode) {
        return examRepository.findByExamCode(examCode);
    }

    @Override
    public List<Exam> getAllBySemesterId(Long semesterId) {
        return examRepository.findAllByRoomSemesterId(semesterId);
    }

    @Override
    public List<Exam> getExamByLocationAndSubjectSemester(Long locationId, Long subjectSemesterId) {
        List<Exam> exams = examRepository.findAllByLocationIdAndSubjectSemesterId(locationId, subjectSemesterId);
        if(CollectionUtils.isEmpty(exams)) return null;
        return exams;
    }

    @Override
    public List<Exam> getExamBySemesterIdAndSubjectIdIn(Long semesterId, List<Long> subjectIds) {
        List<Exam> exams = examRepository.findAllBySemesterIdAndSubjectIdIn(semesterId, subjectIds);
        if(CollectionUtils.isEmpty(exams)) return null;
        return exams;
    }
}
