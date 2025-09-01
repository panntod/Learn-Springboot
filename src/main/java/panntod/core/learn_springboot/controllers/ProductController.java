package panntod.core.learn_springboot.controllers;

import panntod.core.learn_springboot.dto.*;
import panntod.core.learn_springboot.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller untuk resource Product.
 * <p>
 * Endpoint:
 * - POST   /api/products         -> create produk baru
 * - GET    /api/products         -> list produk dengan pagination + sorting
 * - POST   /api/products/search  -> pencarian produk dengan payload request
 * - GET    /api/products/{id}    -> detail produk
 * - PUT    /api/products/{id}    -> update penuh
 * - PATCH  /api/products/{id}    -> update parsial
 * - DELETE /api/products/{id}    -> hapus produk
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // Constructor Injection (best practice)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Membuat produk baru.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> create(@Valid @RequestBody ProductCreateDto dto) {
        var createdProduct = productService.create(dto);
        return ResponseEntity.status(201).body(
                new ApiResponse<>("SUCCESS", "Product created successfully", Optional.of(createdProduct))
        );
    }

    /**
     * Mengambil daftar produk dengan pagination & sorting.
     */
    @GetMapping
    public ResponseEntity<PageResponse<ProductDto>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) List<String> sort,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, parseSortParam(sort));

        // Bungkus ke DTO request supaya tetap rapih
        ProductSearchRequest req = new ProductSearchRequest(name, category, minPrice, maxPrice);

        return ResponseEntity.ok(productService.search(req, pageable));
    }


    /**
     * Mengambil detail produk berdasarkan ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> get(@PathVariable Long id) {
        var product = productService.findById(id);
        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Product found", Optional.of(product))
        );
    }

    /**
     * Update penuh produk (replace semua field).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductCreateDto dto
    ) {
        var updatedProduct = productService.update(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Product updated successfully", Optional.of(updatedProduct))
        );
    }

    /**
     * Update parsial produk (hanya field tertentu).
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> patch(
            @PathVariable Long id,
            @RequestBody ProductDto dto
    ) {
        var patchedProduct = productService.patch(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Product patched successfully", Optional.of(patchedProduct))
        );
    }

    /**
     * Menghapus produk berdasarkan ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Product deleted successfully", Optional.empty())
        );
    }

    /**
     * Helper method untuk parsing parameter sort.
     */
    private Sort parseSortParam(List<String> sortList) {
        if (sortList == null || sortList.isEmpty()) {
            return Sort.by(Sort.Order.desc("createdAt")); // default sort
        }

        return Sort.by(sortList.stream().map(s -> {
            String[] parts = s.split(",");
            String property;
            Sort.Direction direction;

            if (parts.length == 1) {
                // Jika cuma "asc" atau "desc", pakai default property createdAt
                property = "createdAt";
                direction = parts[0].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            } else {
                property = parts[0].trim();
                direction = parts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            }

            return new Sort.Order(direction, property);
        }).toList());
    }
}
