package com.rgutierrez.carregistry.registryservice.converters;

import com.rgutierrez.carregistry.domain.Brand;
import com.rgutierrez.carregistry.repository.entities.BrandEntity;
import org.springframework.stereotype.Component;

@Component
public class BrandConverter {
    public BrandEntity brandToEntity(Brand brand){
        BrandEntity brandEntity = new BrandEntity();

        brandEntity.setId(brand.getId());
        brandEntity.setName(brand.getName());
        brandEntity.setWarranty(brand.getWarranty());
        brandEntity.setCountry(brand.getCountry());

        return brandEntity;
    }

    public Brand entityToBrand(BrandEntity brandEntity){
        Brand brand = new Brand();

        brand.setId(brandEntity.getId());
        brand.setName(brandEntity.getName());
        brand.setWarranty(brandEntity.getWarranty());
        brand.setCountry(brandEntity.getCountry());

        return brand;
    }
}
