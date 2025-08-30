package panntod.core.learn_springboot.specs;

import panntod.core.learn_springboot.entities.Product;
import panntod.core.learn_springboot.dto.ProductSearchRequest;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> bySearch(ProductSearchRequest req) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (req == null) return p;

            if (req.getName() != null && !req.getName().isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("name")), "%" + req.getName().toLowerCase() + "%"));
            }
            if (req.getCategory() != null && !req.getCategory().isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("category")), "%" + req.getCategory().toLowerCase() + "%"));
            }
            BigDecimal min = req.getMinPrice();
            BigDecimal max = req.getMaxPrice();
            if (min != null) {
                p = cb.and(p, cb.ge(root.get("price"), min));
            }
            if (max != null) {
                p = cb.and(p, cb.le(root.get("price"), max));
            }
            return p;
        };
    }
}
