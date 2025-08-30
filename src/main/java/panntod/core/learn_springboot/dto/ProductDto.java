package panntod.core.learn_springboot.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductDto(Long id, String name, String category, String description, BigDecimal price, Instant createdAt) {}
