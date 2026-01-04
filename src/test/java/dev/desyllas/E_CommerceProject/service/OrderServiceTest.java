package dev.desyllas.E_CommerceProject.service;

import dev.desyllas.E_CommerceProject.dto.CreateOrderRequest;
import dev.desyllas.E_CommerceProject.dto.OrderItemRequest;
import dev.desyllas.E_CommerceProject.dto.OrderResponse;
import dev.desyllas.E_CommerceProject.model.Order;
import dev.desyllas.E_CommerceProject.model.Product;
import dev.desyllas.E_CommerceProject.model.User;
import dev.desyllas.E_CommerceProject.repository.OrderRepository;
import dev.desyllas.E_CommerceProject.repository.ProductRepository;
import dev.desyllas.E_CommerceProject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User(1L, "test@example.com", "password", null, null);
        product = new Product(1, "Laptop", "High-end", "BrandX", null, 1200.0, LocalDate.now(), true, 5);


        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
    }

    @Test
    void createOrder_success() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setItems(List.of(new OrderItemRequest() {{
            setProductId(1);
            setQuantity(2);
        }}));

        when(productRepo.findById(1)).thenReturn(Optional.of(product));
        when(orderRepo.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(2400.0, response.getTotalPrice());
        assertEquals(1, response.getItems().size());
        assertEquals(2, response.getItems().get(0).getQuantity());


        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(orderCaptor.capture());
        assertEquals(2400.0, orderCaptor.getValue().getTotalPrice());
    }

    @Test
    void createOrder_productNotFound_throws() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setItems(List.of(new OrderItemRequest() {{
            setProductId(99);
            setQuantity(1);
        }}));

        when(productRepo.findById(99)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> orderService.createOrder(request));

        assertEquals("Product not found", ex.getMessage());
    }

    @Test
    void getMyOrders_returnsOrders() {
        Order order = new Order();
        order.setId(1);
        order.setTotalPrice(100.0);
        order.setUser(user);

        when(orderRepo.findByUserId(user.getId())).thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getMyOrders();
        assertEquals(1, responses.size());
        assertEquals(100.0, responses.get(0).getTotalPrice());
    }

    @Test
    void getMyOrderById_found() {
        Order order = new Order();
        order.setId(1);
        order.setTotalPrice(100.0);
        order.setUser(user);

        when(orderRepo.findByIdAndUserId(1, user.getId())).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getMyOrderById(1);
        assertEquals(100.0, response.getTotalPrice());
    }

    @Test
    void getMyOrderById_notFound_throws() {
        when(orderRepo.findByIdAndUserId(99, user.getId())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> orderService.getMyOrderById(99));
        assertEquals("Order not found or does not belong to you", ex.getMessage());
    }
}
