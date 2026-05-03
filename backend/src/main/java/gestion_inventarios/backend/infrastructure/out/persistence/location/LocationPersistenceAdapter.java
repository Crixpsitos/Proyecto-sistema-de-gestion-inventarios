package gestion_inventarios.backend.infrastructure.out.persistence.location;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import gestion_inventarios.backend.application.ports.out.LocationRepositoryPort;
import gestion_inventarios.backend.domain.exception.LocationNotFoundException;
import gestion_inventarios.backend.domain.model.locations.Location;
import gestion_inventarios.backend.domain.model.locations.LocationCode;
import gestion_inventarios.backend.domain.model.locations.LocationType;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.out.persistence.location.entity.LocationEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.location.mapper.LocationMapper;
import gestion_inventarios.backend.infrastructure.out.persistence.location.repository.LocationJpaRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocationPersistenceAdapter implements LocationRepositoryPort {

    private final LocationJpaRepository repository;
    private final LocationMapper mapper;

    @Override
    public Location findById(Long id) {

        return repository.findById(id).map(mapper::toDomain).orElseThrow(() -> new LocationNotFoundException(id));
    }

    @Override
    public PageResult<Location> findByType(PageRequest pageRequest, LocationType type) {
        org.springframework.data.domain.PageRequest springPage = org.springframework.data.domain.PageRequest
                .of(pageRequest.page(), pageRequest.size(), Sort.by(Sort.Direction.ASC, "createdAt"));

        var page = repository.findByType(type, springPage);

        return PageResult.of(page.getContent().stream().map(mapper::toDomain).toList(), pageRequest.page(),
                pageRequest.size(), page.getTotalElements());
    }

    @Override
    public Location save(Location location) {
        LocationEntity entity = mapper.toEntity(location);
        entity = repository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public PageResult<Location> search(PageRequest pageRequest, String search) {
        org.springframework.data.domain.PageRequest springPage = org.springframework.data.domain.PageRequest
                .of(pageRequest.page(), pageRequest.size(), Sort.by(Sort.Direction.ASC, "createdAt"));

        var page = repository.search(springPage, search);

        return PageResult.of(page.getContent().stream().map(mapper::toDomain).toList(), pageRequest.page(),
                pageRequest.size(), page.getTotalElements());
    }

    @Override
    public PageResult<Location> findAllActive(PageRequest pageRequest) {
        org.springframework.data.domain.PageRequest springPage = org.springframework.data.domain.PageRequest
                .of(pageRequest.page(), pageRequest.size(), Sort.by(Sort.Direction.ASC, "createdAt"));

        var page = repository.findAllActive(springPage);

        return PageResult.of(page.getContent().stream().map(mapper::toDomain).toList(), pageRequest.page(),
                pageRequest.size(), page.getTotalElements());
    }

    @Override
    public PageResult<Location> findAll(PageRequest pageRequest, String type, Boolean onlyActive) {
        org.springframework.data.domain.PageRequest springPage = org.springframework.data.domain.PageRequest
                .of(pageRequest.page(), pageRequest.size(), Sort.by(Sort.Direction.ASC, "createdAt"));
        LocationType locationType = (type != null && !type.equals("all"))
                ? LocationType.valueOf(type)
                : null;
        
                Boolean activeFilter = Boolean.TRUE.equals(onlyActive) ? true : null;

        var page = repository.findAll(locationType, activeFilter, springPage);



        return PageResult.of(page.getContent().stream().map(mapper::toDomain).toList(), pageRequest.page(),
                pageRequest.size(), page.getTotalElements());
    }

    @Override
    public void deactivate(Long id) {
        repository.deactivate(id);
    }

    @Override
    public void activate(Long id) {
        repository.activate(id);
    }

    @Override
    public boolean existsByCode(LocationCode code) {
        return repository.existsByCode(code.getValue());
    }

}
