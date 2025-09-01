package panntod.core.learn_springboot.services;

import panntod.core.learn_springboot.dto.*;
import panntod.core.learn_springboot.entities.Product;
import panntod.core.learn_springboot.mappers.ProductMapper;
import panntod.core.learn_springboot.repositories.ProductRepository;
import panntod.core.learn_springboot.specs.ProductSpecification;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer untuk entity Product.
 *
 * Service ini bertugas menjalankan business logic,
 * menghubungkan antara Controller (API layer) dan Repository (DB layer),
 * serta melakukan mapping Entity <-> DTO.
 */
@Service
public class ProductService {

    private final ProductRepository repo;
    private final ProductMapper mapper;

    // Constructor Injection untuk dependency (best practice)
    public ProductService(ProductRepository repo, ProductMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    /**
     * Membuat produk baru.
     * - Terima DTO dari controller
     * - Mapping ke entity
     * - Simpan ke DB
     * - Balikkan DTO hasil save
     */
    @Transactional
    public ProductDto create(ProductCreateDto createDto) {
        Product product = mapper.toEntity(createDto);
        Product savedProduct = repo.save(product);
        return mapper.toDto(savedProduct);
    }

    /**
     * Mencari produk berdasarkan kriteria (name, category, price range) + pagination + sorting.
     * - Gunakan Specification untuk build query dinamis
     * - Bungkus hasil dalam PageResponse custom
     */
    @Transactional(readOnly = true)
    public PageResponse<ProductDto> search(ProductSearchRequest req, Pageable pageable) {
        var spec = ProductSpecification.bySearch(req);
        Page<Product> page = repo.findAll(spec, pageable);

        var content = page.stream()
                .map(mapper::toDto)
                .toList();

        return new PageResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber() + 1, // convert ke 1-based index
                page.getSize()
        );
    }

    /**
     * Mengambil detail produk berdasarkan ID.
     * Jika tidak ditemukan -> lempar exception.
     */
    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        return repo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    /**
     * Mengupdate produk secara penuh (replace semua field).
     * Cocok untuk HTTP PUT.
     */
    @Transactional
    public ProductDto update(Long id, ProductCreateDto updateDto) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(updateDto.getName());
        existing.setCategory(updateDto.getCategory());
        existing.setPrice(updateDto.getPrice());

        Product saved = repo.save(existing);
        return mapper.toDto(saved);
    }

    /**
     * Mengupdate produk secara parsial (hanya field yang ada).
     * Cocok untuk HTTP PATCH.
     */
    @Transactional
    public ProductDto patch(Long id, ProductDto patchDto) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update hanya field yang tidak null
        if (patchDto.name() != null) existing.setName(patchDto.name());
        if (patchDto.category() != null) existing.setCategory(patchDto.category());
        if (patchDto.price() != null) existing.setPrice(patchDto.price());

        Product saved = repo.save(existing);
        return mapper.toDto(saved);
    }

    /**
     * Menghapus produk berdasarkan ID.
     */
    @Transactional
    public void delete(Long id) {
        Product existing = repo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Product not found"));

        repo.deleteById(id);
    }
}
