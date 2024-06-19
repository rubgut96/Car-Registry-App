package com.rgutierrez.carregistry.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="car")

public class CarEntity {
    @Id
    @Column(name="id")
    private Integer id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="brand_id", referencedColumnName="id")
    private BrandEntity brand;
    @Column(name="model")
    private String model;
    @Column(name="mileage")
    private Integer mileage;
    @Column(name="price")
    private Double price;
    @Column(name="year")
    private Integer year;
    @Column(name="description")
    private String description;
    @Column(name="fuel_type")
    private String fuelType;
}