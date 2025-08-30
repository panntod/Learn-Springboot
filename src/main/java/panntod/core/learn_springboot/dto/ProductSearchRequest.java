package panntod.core.learn_springboot.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductSearchRequest {
    private String name;
    private String category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
