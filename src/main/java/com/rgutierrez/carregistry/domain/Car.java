package com.rgutierrez.carregistry.domain;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private Integer id;
    private Brand brand;
    private String model;
    private Integer mileage;
    private Double price;
    private Integer year;
    private String description;
    private String fuelType;
}
