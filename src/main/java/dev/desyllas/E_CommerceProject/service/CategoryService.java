package dev.desyllas.E_CommerceProject.service;

import dev.desyllas.E_CommerceProject.dto.CategoryRequest;
import dev.desyllas.E_CommerceProject.dto.CategoryResponse;
import dev.desyllas.E_CommerceProject.model.Category;
import dev.desyllas.E_CommerceProject.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<CategoryResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<CategoryResponse> getById(Integer id) {
        return repo.findById(id)
                .map(this::toResponse);
    }

    public CategoryResponse create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());

        Category saved = repo.save(category);
        return toResponse(saved);
    }

    public Optional<CategoryResponse> update(Integer id, CategoryRequest request) {

        return repo.findById(id)
                .map(existing -> {
                    existing.setName(request.getName());
                    Category updated = repo.save(existing);
                    return toResponse(updated);
                });
    }

    public boolean delete(Integer id) {
        return repo.findById(id).map(category -> {
            repo.delete(category);
            return true;
        }).orElse(false);
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}
