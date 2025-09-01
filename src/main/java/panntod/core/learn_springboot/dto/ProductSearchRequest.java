package panntod.core.learn_springboot.dto;

public record ProductSearchRequest(
        String name,
        String category,
        Double minPrice,
        Double maxPrice
) {}
