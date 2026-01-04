package dev.desyllas.E_CommerceProject.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Integer categoryId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be >= 0.0")
    private Double price;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    @NotNull(message = "Availability is required")
    private Boolean available;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be ≥ 0")
    private Integer quantity;

}
