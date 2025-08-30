package panntod.core.learn_springboot.controllers;

import panntod.core.learn_springboot.dto.*;
import panntod.core.learn_springboot.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService svc;

    public ProductController(ProductService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductCreateDto dto) {
        var created = svc.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    // GET with pagination & sorting query params: page, size, sort
    @GetMapping
    public ResponseEntity<PageResponse<ProductDto>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) List<String> sort
    ) {
        // kurangi 1 agar 1-based menjadi 0-based
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, parseSortParam(sort));
        return ResponseEntity.ok(svc.listAll(pageable));
    }

    // POST search by payload + pagination params in URL
    @PostMapping("/search")
    public ResponseEntity<PageResponse<ProductDto>> search(
            @RequestBody(required = false) ProductSearchRequest req,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) List<String> sort
    ) {
        // kurangi 1 agar 1-based menjadi 0-based
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, parseSortParam(sort));
        return ResponseEntity.ok(svc.listAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(svc.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    // helper to parse sort param(s)
    private Sort parseSortParam(List<String> sortList) {
        if (sortList == null || sortList.isEmpty()) {
            return Sort.by(Sort.Order.desc("createdAt")); // default
        }

        return Sort.by(
                sortList.stream().map(s -> {
                    String[] parts = s.split(",");
                    String prop;
                    Sort.Direction dir;
                    if (parts.length == 1) {
                        // jika cuma "asc" atau "desc", pakai default property createdAt
                        prop = "createdAt";
                        dir = parts[0].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
                    } else {
                        prop = parts[0].trim();
                        dir = parts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
                    }
                    return new Sort.Order(dir, prop);
                }).toList()
        );
    }
}
