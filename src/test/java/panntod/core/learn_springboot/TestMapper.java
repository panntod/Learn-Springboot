package panntod.core.learn_springboot;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import panntod.core.learn_springboot.dto.ProductCreateDto;
import panntod.core.learn_springboot.entities.Product;
import panntod.core.learn_springboot.mappers.ProductMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TestMapper {

    // ambil instance mapper dari MapStruct
    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void testProductMapper() {
        // buat DTO
        ProductCreateDto dto = new ProductCreateDto();
        dto.setName("Laptop Gaming");
        dto.setCategory("Electronics");
        dto.setDescription("High performance laptop");
        dto.setPrice(BigDecimal.valueOf(2500.00));

        // convert ke entity
        Product entity = mapper.toEntity(dto);

        // cek hasil mapping
        assertNotNull(entity, "Entity tidak boleh null");
        assertEquals(dto.getName(), entity.getName(), "Nama harus sama");
        assertEquals(dto.getCategory(), entity.getCategory(), "Category harus sama");
        assertEquals(dto.getDescription(), entity.getDescription(), "Description harus sama");
        assertEquals(dto.getPrice(), entity.getPrice(), "Price harus sama");

        // bisa cek createdAt default null atau Instant.now()
        System.out.println("Mapped entity: " + entity);
    }
}
