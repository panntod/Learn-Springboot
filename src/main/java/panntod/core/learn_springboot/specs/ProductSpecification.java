package panntod.core.learn_springboot.specs;

import panntod.core.learn_springboot.entities.Product;
import panntod.core.learn_springboot.dto.ProductSearchRequest;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.math.BigDecimal;

/**
 * Class untuk membangun dynamic query (filter) menggunakan JPA Specification.
 * <p>
 * Tujuan: Membuat pencarian produk lebih fleksibel berdasarkan request dari client.
 */
public class ProductSpecification {

    /**
     * Membangun Specification<Product> berdasarkan kriteria dari ProductSearchRequest.
     *
     * @param searchRequest berisi parameter pencarian (name, category, minPrice, maxPrice)
     * @return Specification<Product> yang bisa dipakai di Repository
     */
    public static Specification<Product> bySearch(ProductSearchRequest searchRequest) {
        return (root, query, criteriaBuilder) -> {
            // Predicate awal: "selalu true" (conjunction) -> akan ditambahkan filter jika ada
            Predicate predicate = criteriaBuilder.conjunction();

            // kalau tidak ada request pencarian, langsung return predicate kosong
            if (searchRequest == null) return predicate;

            // filter berdasarkan nama (case-insensitive, LIKE %name%)
            if (searchRequest.name() != null && !searchRequest.name().isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + searchRequest.name().toLowerCase() + "%"
                        )
                );
            }

            // filter berdasarkan kategori (case-insensitive, LIKE %category%)
            if (searchRequest.category() != null && !searchRequest.category().isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("category")),
                                "%" + searchRequest.category().toLowerCase() + "%"
                        )
                );
            }

            // filter berdasarkan harga minimum
            if (searchRequest.minPrice() != null) {
                BigDecimal minPrice = BigDecimal.valueOf(searchRequest.minPrice());
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.ge(root.get("price"), minPrice)
                );
            }

            // filter berdasarkan harga maksimum
            if (searchRequest.maxPrice() != null) {
                BigDecimal maxPrice = BigDecimal.valueOf(searchRequest.maxPrice());
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.le(root.get("price"), maxPrice)
                );
            }

            return predicate;
        };
    }
}
