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
public class BrandRequest {
    @NotNull
    private Integer id;
    private String name;
    private Integer warranty;
    private String country;
}
