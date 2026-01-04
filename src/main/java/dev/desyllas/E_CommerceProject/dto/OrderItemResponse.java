package dev.desyllas.E_CommerceProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemResponse {

    private Integer productId;
    private String productName;
    private int quantity;
    private double price;
}
