package vn.edu.vnu.uet.dkt.dto.service.semester;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dkt.dto.model.Semester;
import vn.edu.vnu.uet.dkt.rest.model.semester.ListSemesterResponse;
import vn.edu.vnu.uet.dkt.rest.model.semester.SemesterResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SemesterService {
    private final SemesterDao semesterDao;
    private final MapperFacade mapperFacade;

    public SemesterService(SemesterDao semesterDao, MapperFacade mapperFacade) {
        this.semesterDao = semesterDao;
        this.mapperFacade = mapperFacade;
    }

    public ListSemesterResponse getAll() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(10);
        List<Semester> semesters = semesterDao.getByCreatedDate(localDateTime);
        List<SemesterResponse> semesterResponses = mapperFacade.mapAsList(semesters,SemesterResponse.class);
        return new ListSemesterResponse(semesterResponses);
    }
}
