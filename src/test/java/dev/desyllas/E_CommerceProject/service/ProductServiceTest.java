package dev.desyllas.E_CommerceProject.service;

import dev.desyllas.E_CommerceProject.dto.ProductRequest;
import dev.desyllas.E_CommerceProject.dto.ProductResponse;
import dev.desyllas.E_CommerceProject.model.Category;
import dev.desyllas.E_CommerceProject.model.Product;
import dev.desyllas.E_CommerceProject.repository.CategoryRepository;
import dev.desyllas.E_CommerceProject.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @InjectMocks
    private ProductService productService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category(1, "Electronics", null);
    }

    @Test
    void addProduct_success() {
        ProductRequest request = new ProductRequest();
        request.setName("Laptop");
        request.setDescription("High-end laptop");
        request.setBrand("BrandX");
        request.setCategoryId(1);
        request.setPrice(1200.0);
        request.setReleaseDate(LocalDate.now());
        request.setAvailable(true);
        request.setQuantity(10);

        when(categoryRepo.findById(1)).thenReturn(Optional.of(category));

        Product savedProduct = new Product(1, "Laptop", "High-end laptop", "BrandX",
                category, 1200.0, LocalDate.now(), true, 10);

        when(productRepo.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponse response = productService.addProduct(request);

        assertNotNull(response);
        assertEquals("Laptop", response.getName());
        assertEquals("Electronics", response.getCategoryName());

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepo).save(productCaptor.capture());
        assertEquals("Laptop", productCaptor.getValue().getName());
    }

    @Test
    void addProduct_categoryNotFound_throws() {
        ProductRequest request = new ProductRequest();
        request.setCategoryId(99);

        when(categoryRepo.findById(99)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> productService.addProduct(request));

        assertEquals("Category not found", ex.getMessage());
    }

    @Test
    void updateProduct_success() {
        ProductRequest request = new ProductRequest();
        request.setName("Laptop Pro");
        request.setDescription("High-end laptop updated");
        request.setBrand("BrandX");
        request.setCategoryId(1);
        request.setPrice(1500.0);
        request.setReleaseDate(LocalDate.now());
        request.setAvailable(true);
        request.setQuantity(5);

        Product existingProduct = new Product(1, "Laptop", "Old desc", "BrandX",
                category, 1200.0, LocalDate.now(), true, 10);

        when(productRepo.findById(1)).thenReturn(Optional.of(existingProduct));
        when(categoryRepo.findById(1)).thenReturn(Optional.of(category));
        when(productRepo.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        var responseOpt = productService.updateProduct(1, request);
        assertTrue(responseOpt.isPresent());
        ProductResponse response = responseOpt.get();
        assertEquals("Laptop Pro", response.getName());
        assertEquals(1500.0, response.getPrice());


        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepo).save(captor.capture());
        assertEquals("Laptop Pro", captor.getValue().getName());
    }

    @Test
    void updateProduct_notFound_returnsEmpty() {
        ProductRequest request = new ProductRequest();
        when(productRepo.findById(99)).thenReturn(Optional.empty());

        var responseOpt = productService.updateProduct(99, request);
        assertTrue(responseOpt.isEmpty());
    }

    @Test
    void patchProduct_success_partialUpdate() {
        ProductRequest request = new ProductRequest();
        request.setName("Laptop Mini"); // only updating name
        request.setAvailable(false);
        request.setQuantity(3);

        Product existingProduct = new Product(1, "Laptop", "Old desc", "BrandX",
                category, 1200.0, LocalDate.now(), true, 10);

        when(productRepo.findById(1)).thenReturn(Optional.of(existingProduct));
        when(categoryRepo.findById(anyInt())).thenReturn(Optional.of(category));
        when(productRepo.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        var responseOpt = productService.patchProduct(1, request);
        assertTrue(responseOpt.isPresent());
        ProductResponse response = responseOpt.get();
        assertEquals("Laptop Mini", response.getName());
        assertFalse(response.isAvailable());
        assertEquals(3, response.getQuantity());

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepo).save(captor.capture());
        assertEquals("Laptop Mini", captor.getValue().getName());
    }

    @Test
    void patchProduct_notFound_returnsEmpty() {
        ProductRequest request = new ProductRequest();
        when(productRepo.findById(99)).thenReturn(Optional.empty());

        var responseOpt = productService.patchProduct(99, request);
        assertTrue(responseOpt.isEmpty());
    }


}
