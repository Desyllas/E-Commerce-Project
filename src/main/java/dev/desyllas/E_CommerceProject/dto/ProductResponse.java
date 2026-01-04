package dev.desyllas.E_CommerceProject.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProductResponse {

    private int id;
    private String name;
    private String description;
    private String brand;
    private String categoryName;
    private Double price;
    private LocalDate releaseDate;
    private boolean available;
    private int quantity;

    public ProductResponse(int id, String name, String description, String brand,
                           String categoryName, Double price, LocalDate releaseDate,
                           boolean available, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.categoryName = categoryName;
        this.price = price;
        this.releaseDate = releaseDate;
        this.available = available;
        this.quantity = quantity;
    }

}
