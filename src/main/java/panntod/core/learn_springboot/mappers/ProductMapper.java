package panntod.core.learn_springboot.mappers;

import panntod.core.learn_springboot.entities.Product;
import panntod.core.learn_springboot.dto.*;
import org.mapstruct.*;

/**
 * Mapper interface untuk konversi antara Entity <-> DTO.
 *
 * Dengan MapStruct, kita tidak perlu menulis kode mapping manual
 * (misalnya setter/getter satu-satu). MapStruct akan otomatis
 * generate implementasi dari interface ini saat compile.
 *
 * `componentModel = "spring"` -> supaya Mapper ini dikelola sebagai bean Spring,
 * sehingga bisa langsung di-@Autowired / di-inject ke Service.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Konversi dari Entity (Product) -> DTO (ProductDto).
     *
     * Biasanya digunakan saat ingin mengembalikan data ke client (API Response).
     */
    ProductDto toDto(Product entity);

    /**
     * Konversi dari DTO (ProductCreateDto) -> Entity (Product).
     *
     * Biasanya digunakan saat menerima data dari client (API Request),
     * lalu diubah ke entity sebelum disimpan ke database.
     */
    Product toEntity(ProductCreateDto dto);
}
