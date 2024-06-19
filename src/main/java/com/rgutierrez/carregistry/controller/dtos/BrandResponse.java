package com.rgutierrez.carregistry.controller.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BrandResponse {
    private Integer id;
    private String name;
    private Integer warranty;
    private String country;
}
