package gestion_inventarios.backend.infrastructure.out.persistence.movements.mapper;

import java.util.Collections;

import org.springframework.stereotype.Component;

import gestion_inventarios.backend.domain.model.movements.Movement;
import gestion_inventarios.backend.infrastructure.out.persistence.Category.Entity.CategoryEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.location.entity.LocationCodeEmbeddable;
import gestion_inventarios.backend.infrastructure.out.persistence.location.entity.LocationEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.location.mapper.LocationMapper;
import gestion_inventarios.backend.infrastructure.out.persistence.movements.entity.MovementEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.products.entity.ProductEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.products.entity.ProductPriceEmbeddable;
import gestion_inventarios.backend.infrastructure.out.persistence.products.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MovementMapper {
        private final ProductMapper productMapper;
        private final LocationMapper locationMapper;

        public MovementEntity toEntity(Movement m) {

                ProductPriceEmbeddable priceEmbeddable = new ProductPriceEmbeddable(
                                m.getProduct().getProductPrice().price(),
                                m.getProduct().getProductPrice().currency().getCurrencyCode());

                CategoryEntity categoryEntity = new CategoryEntity(
                                m.getProduct().getCategory().getId(),
                                m.getProduct().getCategory().getName(),
                                m.getProduct().getCategory().getDescription(),
                                Collections.emptyList(),
                                m.getProduct().getCategory().getCreatedAt(),
                                m.getProduct().getCategory().getUpdatedAt());

                ProductEntity productEntity = new ProductEntity(m.getProduct().getId(), m.getProduct().getSku(),
                                m.getProduct().getName(), m.getProduct().getDescription(), categoryEntity,
                                m.getProduct().getBrand(),
                                m.getProduct().getModel(), m.getProduct().getColor(), m.getProduct().getSize(),
                                m.getProduct().getImage(), m.getProduct().getQuantity(), priceEmbeddable,
                                m.getProduct().isActive(),
                                m.getProduct().getCreatedAt(), m.getProduct().getUpdatedAt());

                LocationEntity locationSourceEntity = m.getSource() != null
                                ? new LocationEntity(m.getSource().getId(), m.getSource().getName(),
                                                new LocationCodeEmbeddable(m.getSource().getCode().getValue()),
                                                m.getSource().getType(), m.getSource().getAddress(),
                                                m.getSource().isActive(), m.getSource().getCreatedAt(),
                                                m.getSource().getUpdatedAt())
                                : null;

                LocationEntity locationDestinationEntity = m.getDestination() != null
                                ? new LocationEntity(m.getDestination().getId(), m.getDestination().getName(),
                                                new LocationCodeEmbeddable(m.getDestination().getCode().getValue()),
                                                m.getDestination().getType(), m.getDestination().getAddress(),
                                                m.getDestination().isActive(), m.getDestination().getCreatedAt(),
                                                m.getDestination().getUpdatedAt())
                                : null;

                MovementEntity entity = new MovementEntity();
                entity.setId(m.getId());
                entity.setType(m.getType());
                entity.setProduct(productMapper.toEntity(m.getProduct()));
                entity.setSource(locationSourceEntity);
                entity.setDestination(locationDestinationEntity);
                entity.setQuantity(m.getQuantity());
                entity.setNotes(m.getNotes());
                entity.setCreatedBy(m.getCreatedBy());
                entity.setCreatedAt(m.getCreatedAt());
                return entity;
        }

        public Movement toDomain(MovementEntity e) {
                return new Movement(
                                e.getId(),
                                e.getType(),
                                productMapper.toDomain(e.getProduct()),
                                locationMapper.toDomain(e.getSource()),
                                locationMapper.toDomain(e.getDestination()),
                                e.getQuantity(),
                                e.getNotes(),
                                e.getCreatedBy(),
                                e.getCreatedAt());
        }
}
