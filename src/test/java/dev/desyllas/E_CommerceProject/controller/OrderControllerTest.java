package dev.desyllas.E_CommerceProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.desyllas.E_CommerceProject.dto.CreateOrderRequest;
import dev.desyllas.E_CommerceProject.dto.OrderItemRequest;
import dev.desyllas.E_CommerceProject.dto.OrderItemResponse;
import dev.desyllas.E_CommerceProject.dto.OrderResponse;
import dev.desyllas.E_CommerceProject.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private OrderResponse sampleOrder;

    @BeforeEach
    void setup() {
        OrderItemResponse item = new OrderItemResponse(1, "Laptop", 2, 1200.0);
        sampleOrder = new OrderResponse(1, LocalDateTime.now(), 2400.0, List.of(item));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void createOrder_success() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setItems(List.of(new OrderItemRequest(){{
            setProductId(1);
            setQuantity(2);
        }}));

        when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(sampleOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value(2400.0));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void getMyOrders_success() throws Exception {
        when(orderService.getMyOrders()).thenReturn(List.of(sampleOrder));

        mockMvc.perform(get("/api/orders/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void getMyOrderById_success() throws Exception {
        when(orderService.getMyOrderById(1)).thenReturn(sampleOrder);

        mockMvc.perform(get("/api/orders/my/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void getAllOrders_admin_success() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(sampleOrder));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void getAllOrders_nonAdmin_forbidden() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isForbidden());
    }
}
