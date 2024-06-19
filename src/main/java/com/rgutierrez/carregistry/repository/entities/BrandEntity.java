package com.rgutierrez.carregistry.repository.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="brand")

public class BrandEntity {
    @Id
    @Column(name="id")
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="warranty")
    private Integer warranty;
    @Column(name="country")
    private String country;
    @OneToMany(mappedBy = "brand",cascade = CascadeType.ALL)
    private List<CarEntity> car;
}