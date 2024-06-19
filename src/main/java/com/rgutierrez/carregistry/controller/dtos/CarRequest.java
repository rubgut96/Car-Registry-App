package com.rgutierrez.carregistry.controller.dtos;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarRequest {
    @NotNull
    private Integer id;
    private BrandRequest brand;
    private String model;
    private Integer mileage;
    private Double price;
    private Integer year;
    private String description;
    private String fuelType;
}
