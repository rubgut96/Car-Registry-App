package com.rgutierrez.carregistry.controller.mapper;


import com.rgutierrez.carregistry.controller.dtos.BrandRequest;
import com.rgutierrez.carregistry.controller.dtos.BrandResponse;
import com.rgutierrez.carregistry.domain.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {
    public BrandResponse toResponse(Brand brand){
        BrandResponse brandResponse = new BrandResponse();

        brandResponse.setId(brand.getId());
        brandResponse.setName(brand.getName());
        brandResponse.setWarranty(brand.getWarranty());
        brandResponse.setCountry(brand.getCountry());

        return brandResponse;
    }

    public Brand toBrand(BrandRequest brandRequest){
        Brand brand = new Brand();

        brand.setId(brandRequest.getId());
        brand.setName(brandRequest.getName());
        brand.setWarranty(brandRequest.getWarranty());
        brand.setCountry(brandRequest.getCountry());

        return brand;
    }
}
