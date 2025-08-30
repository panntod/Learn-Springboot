package panntod.core.learn_springboot.services;

import panntod.core.learn_springboot.dto.*;
import panntod.core.learn_springboot.entities.Product;
import panntod.core.learn_springboot.mappers.ProductMapper;
import panntod.core.learn_springboot.repositories.ProductRepository;
import panntod.core.learn_springboot.specs.ProductSpecification;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository repo;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repo, ProductMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional
    public ProductDto create(ProductCreateDto createDto) {
        Product p = mapper.toEntity(createDto);
        Product saved = repo.save(p);
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductDto> search(ProductSearchRequest req, Pageable pageable) {
        var spec = ProductSpecification.bySearch(req);
        Page<Product> page = repo.findAll(spec, pageable);
        var content = page.stream().map(mapper::toDto).toList();
        return new PageResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber() + 1,
                page.getSize()
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductDto> listAll(Pageable pageable) {
        Page<Product> page = repo.findAll(pageable);
        var content = page.stream().map(mapper::toDto).toList();
        return new PageResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber() + 1,
                page.getSize()
        );
    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        return repo.findById(id).map(mapper::toDto).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
