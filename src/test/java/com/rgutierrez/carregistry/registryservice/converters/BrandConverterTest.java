package com.rgutierrez.carregistry.registryservice.converters;

import com.rgutierrez.carregistry.domain.Brand;
import com.rgutierrez.carregistry.repository.entities.BrandEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BrandConverterTest {

    @InjectMocks
    private BrandConverter brandConverter;

    @Test
    void brandToEntity_test() {
        Brand brand = new Brand();
        brand.setId(1);
        brand.setName("a");
        brand.setWarranty(1);
        brand.setCountry("a");

        BrandEntity brandEntity = brandConverter.brandToEntity(brand);

        assertEquals(brand.getId(), brandEntity.getId());
        assertEquals(brand.getName(), brandEntity.getName());
        assertEquals(brand.getWarranty(), brandEntity.getWarranty());
        assertEquals(brand.getCountry(), brandEntity.getCountry());
    }

    @Test
    void entityToBrand_test() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setName("a");
        brandEntity.setWarranty(1);
        brandEntity.setCountry("a");

        Brand brand = brandConverter.entityToBrand(brandEntity);

        assertEquals(brandEntity.getId(), brand.getId());
        assertEquals(brandEntity.getName(), brand.getName());
        assertEquals(brandEntity.getWarranty(), brand.getWarranty());
        assertEquals(brandEntity.getCountry(), brand.getCountry());
    }
}