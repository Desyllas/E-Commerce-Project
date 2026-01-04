package dev.desyllas.E_CommerceProject.service;

import dev.desyllas.E_CommerceProject.dto.ProductRequest;
import dev.desyllas.E_CommerceProject.dto.ProductResponse;
import dev.desyllas.E_CommerceProject.model.Category;
import dev.desyllas.E_CommerceProject.model.Product;
import dev.desyllas.E_CommerceProject.repository.CategoryRepository;
import dev.desyllas.E_CommerceProject.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductService(ProductRepository productRepo, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<ProductResponse> getAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductById(int id) {
        return productRepo.findById(id)
                .map(this::toResponse);
    }

    public ProductResponse addProduct(ProductRequest request) {

        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setCategory(category);
        product.setPrice(request.getPrice());
        product.setReleaseDate(request.getReleaseDate());
        product.setAvailable(request.getAvailable());
        product.setQuantity(request.getQuantity());

        Product saved = productRepo.save(product);

        return toResponse(saved);
    }

    public Optional<ProductResponse> updateProduct(int id, ProductRequest request) {
        return productRepo.findById(id).map(existing -> {

            Category category = categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));

            existing.setName(request.getName());
            existing.setDescription(request.getDescription());
            existing.setBrand(request.getBrand());
            existing.setCategory(category);
            existing.setPrice(request.getPrice());
            existing.setReleaseDate(request.getReleaseDate());
            existing.setAvailable(request.getAvailable());
            existing.setQuantity(request.getQuantity());

            Product updated = productRepo.save(existing);

            return toResponse(updated);
        });
    }

    public Optional<ProductResponse> patchProduct(int id, ProductRequest request) {
        return productRepo.findById(id).map(existing -> {

            if (request.getName() != null) existing.setName(request.getName());
            if (request.getDescription() != null) existing.setDescription(request.getDescription());
            if (request.getBrand() != null) existing.setBrand(request.getBrand());
            if (request.getPrice() != null) existing.setPrice(request.getPrice());
            if (request.getReleaseDate() != null) existing.setReleaseDate(request.getReleaseDate());

            existing.setAvailable(request.getAvailable());

            if (request.getQuantity() >= 0) {
                existing.setQuantity(request.getQuantity());
            }

            if (request.getCategoryId() != null) {
                Category category = categoryRepo.findById(request.getCategoryId())
                        .orElseThrow(() -> new EntityNotFoundException("Category not found"));
                existing.setCategory(category);
            }

            Product updated = productRepo.save(existing);

            return toResponse(updated);
        });
    }

    public boolean deleteProduct(int id) {
        return productRepo.findById(id).map(product -> {
            productRepo.delete(product);
            return true;
        }).orElse(false);
    }


    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getBrand(),
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getPrice(),
                product.getReleaseDate(),
                product.isAvailable(),
                product.getQuantity()
        );
    }
}
