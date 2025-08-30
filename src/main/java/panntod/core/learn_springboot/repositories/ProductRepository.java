package panntod.core.learn_springboot.repositories;

import panntod.core.learn_springboot.entities.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // add custom queries if needed
}
