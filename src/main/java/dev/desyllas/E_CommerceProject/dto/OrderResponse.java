package dev.desyllas.E_CommerceProject.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

    private Integer id;
    private LocalDateTime createdAt;
    private double totalPrice;
    private List<OrderItemResponse> items;


    public OrderResponse(Integer id, LocalDateTime createdAt, double totalPrice, List<OrderItemResponse> items) {
        this.id = id;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.items = items;
    }

}
