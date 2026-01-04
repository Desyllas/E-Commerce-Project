package dev.desyllas.E_CommerceProject.service;

import dev.desyllas.E_CommerceProject.dto.CategoryRequest;
import dev.desyllas.E_CommerceProject.dto.CategoryResponse;
import dev.desyllas.E_CommerceProject.model.Category;
import dev.desyllas.E_CommerceProject.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepo;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category(1, "Electronics", null);
    }

    @Test
    void getAll_returnsList() {
        when(categoryRepo.findAll()).thenReturn(List.of(category));

        List<CategoryResponse> responses = categoryService.getAll();
        assertEquals(1, responses.size());
        assertEquals("Electronics", responses.get(0).getName());
    }

    @Test
    void getById_found() {
        when(categoryRepo.findById(1)).thenReturn(Optional.of(category));

        Optional<CategoryResponse> responseOpt = categoryService.getById(1);
        assertTrue(responseOpt.isPresent());
        assertEquals("Electronics", responseOpt.get().getName());
    }

    @Test
    void getById_notFound() {
        when(categoryRepo.findById(99)).thenReturn(Optional.empty());

        Optional<CategoryResponse> responseOpt = categoryService.getById(99);
        assertTrue(responseOpt.isEmpty());
    }

    @Test
    void create_success() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Books");

        Category savedCategory = new Category(2, "Books", null);
        when(categoryRepo.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponse response = categoryService.create(request);
        assertEquals("Books", response.getName());

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepo).save(captor.capture());
        assertEquals("Books", captor.getValue().getName());
    }

    @Test
    void update_found() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Gadgets");

        when(categoryRepo.findById(1)).thenReturn(Optional.of(category));
        when(categoryRepo.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        Optional<CategoryResponse> responseOpt = categoryService.update(1, request);
        assertTrue(responseOpt.isPresent());
        assertEquals("Gadgets", responseOpt.get().getName());
    }

    @Test
    void update_notFound_returnsEmpty() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Gadgets");

        when(categoryRepo.findById(99)).thenReturn(Optional.empty());

        Optional<CategoryResponse> responseOpt = categoryService.update(99, request);
        assertTrue(responseOpt.isEmpty());
    }

    @Test
    void delete_found() {
        when(categoryRepo.findById(1)).thenReturn(Optional.of(category));

        boolean result = categoryService.delete(1);
        assertTrue(result);
        verify(categoryRepo).delete(category);
    }

    @Test
    void delete_notFound_returnsFalse() {
        when(categoryRepo.findById(99)).thenReturn(Optional.empty());

        boolean result = categoryService.delete(99);
        assertFalse(result);
    }
}
