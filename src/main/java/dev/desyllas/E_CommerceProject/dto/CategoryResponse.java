package dev.desyllas.E_CommerceProject.dto;

import lombok.Getter;

@Getter
public class CategoryResponse {

    private Integer id;
    private String name;

    public CategoryResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
