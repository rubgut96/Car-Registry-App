package com.rgutierrez.carregistry.domain;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
    private Integer id;
    private String name;
    private Integer warranty;
    private String country;
}