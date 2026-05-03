package gestion_inventarios.backend.infrastructure.out.persistence.movements.specification;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import gestion_inventarios.backend.domain.model.movements.MovementFilters;
import gestion_inventarios.backend.infrastructure.out.persistence.movements.entity.MovementEntity;
import jakarta.persistence.criteria.Predicate;

public class MovementSpecification {

    public static Specification<MovementEntity> withFilters(MovementFilters filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.type() != null) {
                predicates.add(cb.equal(root.get("type"), filters.type()));
            }

            if (filters.productId() != null) {
                predicates.add(cb.equal(root.get("product").get("id"), filters.productId()));
            }

            if (filters.locationId() != null) {
                predicates.add(
                        cb.or(
                                cb.equal(root.get("source").get("id"), filters.locationId()),
                                cb.equal(root.get("destination").get("id"), filters.locationId())));
            }

            if (filters.dateFrom() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"), 
                                filters.dateFrom() 
                ));
            }

            if (filters.dateTo() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"),
                                filters.dateTo() 
                ));
            }
            

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}