package panntod.core.learn_springboot.repositories;

import panntod.core.learn_springboot.entities.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Repository untuk entity Product.
 *
 * Repository ini berfungsi sebagai "jembatan" antara kode Java dan database.
 * Dengan extend JpaRepository & JpaSpecificationExecutor, kita otomatis dapat
 * banyak method siap pakai tanpa harus bikin query manual.
 */
@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long>, // menyediakan CRUD dasar (save, findById, delete, dll.)
        JpaSpecificationExecutor<Product> { // memungkinkan query lebih fleksibel dengan Specification API

    // Di sini kita bisa menambahkan custom query jika bawaan JpaRepository tidak cukup.
    // Contoh:
    // List<Product> findByName(String name);
}
