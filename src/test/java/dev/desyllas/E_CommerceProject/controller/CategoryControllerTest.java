package dev.desyllas.E_CommerceProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.desyllas.E_CommerceProject.dto.CategoryRequest;
import dev.desyllas.E_CommerceProject.dto.CategoryResponse;
import dev.desyllas.E_CommerceProject.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    void getAllCategories_success() throws Exception {
        CategoryResponse cat = new CategoryResponse(1, "Electronics");
        when(categoryService.getAll()).thenReturn(List.of(cat));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void getCategoryById_found() throws Exception {
        CategoryResponse cat = new CategoryResponse(1, "Electronics");
        when(categoryService.getById(1)).thenReturn(Optional.of(cat));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void getCategoryById_notFound() throws Exception {
        when(categoryService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCategory_success() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Books");

        CategoryResponse response = new CategoryResponse(2, "Books");
        when(categoryService.create(any(CategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @Test
    void updateCategory_found() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Home Electronics");

        CategoryResponse response = new CategoryResponse(1, "Home Electronics");
        when(categoryService.update(eq(1), any(CategoryRequest.class))).thenReturn(Optional.of(response));

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Home Electronics"));
    }

    @Test
    void updateCategory_notFound() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Nonexistent");
        when(categoryService.update(eq(99), any(CategoryRequest.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/categories/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_found() throws Exception {
        when(categoryService.delete(1)).thenReturn(true);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCategory_notFound() throws Exception {
        when(categoryService.delete(99)).thenReturn(false);

        mockMvc.perform(delete("/api/categories/99"))
                .andExpect(status().isNotFound());
    }
}
