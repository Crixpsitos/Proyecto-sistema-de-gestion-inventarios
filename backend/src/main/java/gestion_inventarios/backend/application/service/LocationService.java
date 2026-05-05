package gestion_inventarios.backend.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import gestion_inventarios.backend.application.ports.in.location.LocationUseCase;
import gestion_inventarios.backend.application.ports.out.location.LocationRepositoryPort;
import gestion_inventarios.backend.domain.exception.LocationCodeAlreadyExistsException;
import gestion_inventarios.backend.domain.model.locations.Location;
import gestion_inventarios.backend.domain.model.locations.LocationCode;
import gestion_inventarios.backend.domain.model.locations.LocationType;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService implements LocationUseCase {

    private final LocationRepositoryPort locationRepository;

    @Override
    public Location create(String name, String code, LocationType type, String address, boolean active) {
        LocationCode locationCode = new LocationCode(code);

        if (locationRepository.existsByCode(locationCode)) {
            throw new LocationCodeAlreadyExistsException(code);
            
        }

        Location location = new Location(name, locationCode, type, address, active, LocalDateTime.now(), LocalDateTime.now());

        
        return locationRepository.save(location);
    }

    @Override
    public Location update(Long id, String name, LocationType type, String address) {
        Location location = locationRepository.findById(id);
        location.update(name, type, address);
        return locationRepository.save(location);
    }

    @Override
    public void deactivate(Long id) {
        locationRepository.deactivate(id);
    }

    @Override
    public void activate(Long id) {
        locationRepository.activate(id);
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.findById(id);
    }

    @Override
    public PageResult<Location> findAll(PageRequest pageRequest, String type, Boolean isActive) {
        return locationRepository.findAll(pageRequest, type, isActive);
    }

    @Override
    public PageResult<Location> findAllActive(PageRequest pageResult) {
        return locationRepository.findAllActive(pageResult);
    }

    @Override
    public PageResult<Location> search(PageRequest pageRequest, String search) {
        return locationRepository.search(pageRequest, search);
    }

    @Override
    public PageResult<Location> findByType(PageRequest pageRequest, LocationType type) {
        return locationRepository.findByType(pageRequest, type);
    }

    @Override
    public boolean isCodeAvailable(String code) {
        return !locationRepository.existsByCode(new LocationCode(code));
    }

    

}