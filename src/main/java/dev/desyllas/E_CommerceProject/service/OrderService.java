package dev.desyllas.E_CommerceProject.service;

import dev.desyllas.E_CommerceProject.dto.CreateOrderRequest;
import dev.desyllas.E_CommerceProject.dto.OrderItemResponse;
import dev.desyllas.E_CommerceProject.dto.OrderResponse;
import dev.desyllas.E_CommerceProject.model.Order;
import dev.desyllas.E_CommerceProject.model.OrderItem;
import dev.desyllas.E_CommerceProject.model.Product;
import dev.desyllas.E_CommerceProject.model.User;
import dev.desyllas.E_CommerceProject.repository.OrderRepository;
import dev.desyllas.E_CommerceProject.repository.ProductRepository;
import dev.desyllas.E_CommerceProject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public OrderService(OrderRepository orderRepo, ProductRepository productRepo, UserRepository userRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        User user = getAuthenticatedUser();

        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());

        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (var itemRequest : request.getItems()) {

            Product product = productRepo.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setOrder(order);

            double itemTotal = product.getPrice() * itemRequest.getQuantity();
            total += itemTotal;

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);

        Order savedOrder = orderRepo.save(order);

        // Convert to DTO response
        List<OrderItemResponse> itemResponses = savedOrder.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getCreatedAt(),
                savedOrder.getTotalPrice(),
                itemResponses
        );
    }

    public List<OrderResponse> getMyOrders() {
        User user = getAuthenticatedUser();

        return orderRepo.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    public OrderResponse getMyOrderById(Integer orderId) {
        User user = getAuthenticatedUser();

        Order order = orderRepo.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found or does not belong to you"));

        return toResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse toResponse(Order savedOrder) {
        List<OrderItemResponse> itemResponses = savedOrder.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getCreatedAt(),
                savedOrder.getTotalPrice(),
                itemResponses
        );
    }
}
