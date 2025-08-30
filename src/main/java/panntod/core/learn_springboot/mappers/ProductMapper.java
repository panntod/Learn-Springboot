package panntod.core.learn_springboot.mappers;

import panntod.core.learn_springboot.entities.Product;
import panntod.core.learn_springboot.dto.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product entity);
    Product toEntity(ProductCreateDto dto);
}
