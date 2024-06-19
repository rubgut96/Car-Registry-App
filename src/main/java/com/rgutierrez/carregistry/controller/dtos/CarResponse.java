package com.rgutierrez.carregistry.controller.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarResponse {
    private Integer id;
    private BrandResponse brand;
    private String model;
    private Integer mileage;
    private Double price;
    private Integer year;
    private String description;
    private String fuelType;
}
