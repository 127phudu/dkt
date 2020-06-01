package vn.edu.vnu.uet.dkt.dto.dao.location;

import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.dto.model.Location;
import vn.edu.vnu.uet.dkt.dto.repository.LocationRepository;

import java.util.List;

@Service
public class LocationDaoImpl implements LocationDao{
    private final LocationRepository locationRepository;

    public LocationDaoImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }
}
