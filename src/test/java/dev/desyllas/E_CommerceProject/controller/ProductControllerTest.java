package dev.desyllas.E_CommerceProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.desyllas.E_CommerceProject.dto.ProductRequest;
import dev.desyllas.E_CommerceProject.dto.ProductResponse;
import dev.desyllas.E_CommerceProject.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void getAllProducts_success() throws Exception {
        ProductResponse product = new ProductResponse(1, "Laptop", "High-end", "BrandX",
                "Electronics", 1200.0, LocalDate.now(), true, 10);
        when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void getProductById_found() throws Exception {
        ProductResponse product = new ProductResponse(1, "Laptop", "High-end", "BrandX",
                "Electronics", 1200.0, LocalDate.now(), true, 10);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void getProductById_notFound() throws Exception {
        when(productService.getProductById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addProduct_success() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Laptop");
        request.setDescription("High-end");
        request.setBrand("BrandX");
        request.setCategoryId(1);
        request.setPrice(1200.0);
        request.setReleaseDate(LocalDate.now());
        request.setAvailable(true);
        request.setQuantity(10);

        ProductResponse response = new ProductResponse(1, "Laptop", "High-end", "BrandX",
                "Electronics", 1200.0, LocalDate.now(), true, 10);

        when(productService.addProduct(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void updateProduct_found() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Laptop Pro");
        request.setDescription("High-end updated");
        request.setBrand("BrandX");
        request.setCategoryId(1);
        request.setPrice(1300.0);
        request.setReleaseDate(LocalDate.now());
        request.setAvailable(true);
        request.setQuantity(5);

        ProductResponse response = new ProductResponse(1, "Laptop Pro", "High-end updated", "BrandX",
                "Electronics", 1300.0, LocalDate.now(), true, 5);

        when(productService.updateProduct(eq(1), any(ProductRequest.class))).thenReturn(Optional.of(response));

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Pro"));
    }

    @Test
    void updateProduct_notFound() throws Exception {
        ProductRequest request = new ProductRequest();
        when(productService.updateProduct(eq(99), any(ProductRequest.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/products/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_found() throws Exception {
        when(productService.deleteProduct(1)).thenReturn(true);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_notFound() throws Exception {
        when(productService.deleteProduct(99)).thenReturn(false);

        mockMvc.perform(delete("/api/products/99"))
                .andExpect(status().isNotFound());
    }
}
