package com.rgutierrez.carregistry.controller.mapper;

import com.rgutierrez.carregistry.controller.dtos.BrandRequest;
import com.rgutierrez.carregistry.controller.dtos.BrandResponse;
import com.rgutierrez.carregistry.domain.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BrandMapperTest {

    @InjectMocks
    private BrandMapper brandMapper;

    @Test
    void ToResponse_test() {
        Brand brand = new Brand();
        brand.setId(1);
        brand.setName("1");
        brand.setWarranty(1);
        brand.setCountry("a");

        BrandResponse brandResponse = brandMapper.toResponse(brand);

        assertEquals(brand.getId(), brandResponse.getId());
        assertEquals(brand.getName(), brandResponse.getName());
        assertEquals(brand.getWarranty(), brandResponse.getWarranty());
        assertEquals(brand.getCountry(), brandResponse.getCountry());
    }

    @Test
    void toBrand_test() {

        BrandRequest brandRequest = new BrandRequest();
        brandRequest.setId(1);
        brandRequest.setName("a");
        brandRequest.setWarranty(1);
        brandRequest.setCountry("a");

        Brand brand = brandMapper.toBrand(brandRequest);

        assertEquals(brandRequest.getId(), brand.getId());
        assertEquals(brandRequest.getName(), brand.getName());
        assertEquals(brandRequest.getWarranty(), brand.getWarranty());
        assertEquals(brandRequest.getCountry(), brand.getCountry());
    }
}