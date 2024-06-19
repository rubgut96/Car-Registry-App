package com.rgutierrez.carregistry.controller.mapper;

import com.rgutierrez.carregistry.controller.dtos.BrandRequest;
import com.rgutierrez.carregistry.controller.dtos.BrandResponse;
import com.rgutierrez.carregistry.controller.dtos.CarRequest;
import com.rgutierrez.carregistry.controller.dtos.CarResponse;
import com.rgutierrez.carregistry.domain.Brand;
import com.rgutierrez.carregistry.domain.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarMapperTest {

    @InjectMocks
    private CarMapper carMapper;
    @Mock
    private BrandMapper brandMapper;

    @Test
    void toResponse() {
        Brand brand = new Brand();
        Car car = new Car();
        car.setId(1);
        car.setBrand(brand);
        car.setModel("a");
        car.setMileage(1);
        car.setPrice(1.00);
        car.setYear(1);
        car.setDescription("a");
        car.setFuelType("a");

        BrandResponse brandResponse = new BrandResponse();

        when(brandMapper.toResponse(brand)).thenReturn(brandResponse);

        CarResponse carResponse = carMapper.toResponse(car);

        assertEquals(car.getId(), carResponse.getId());
        assertEquals(brandResponse, carResponse.getBrand());
        assertEquals(car.getModel(), carResponse.getModel());
        assertEquals(car.getMileage(), carResponse.getMileage());
        assertEquals(car.getPrice(), carResponse.getPrice());
        assertEquals(car.getYear(), carResponse.getYear());
        assertEquals(car.getDescription(), carResponse.getDescription());
        assertEquals(car.getFuelType(), carResponse.getFuelType());
    }

    @Test
    void toCar() {

        BrandRequest brandRequest = new BrandRequest();
        CarRequest carRequest = new CarRequest();
        carRequest.setId(1);
        carRequest.setBrand(brandRequest);
        carRequest.setModel("a");
        carRequest.setMileage(1);
        carRequest.setPrice(1.00);
        carRequest.setYear(1);
        carRequest.setDescription("a");
        carRequest.setFuelType("a");

        Brand brand = new Brand();

        when(brandMapper.toBrand(brandRequest)).thenReturn(brand);

        Car car = carMapper.toCar(carRequest);

        assertEquals(carRequest.getId(), car.getId());
        assertEquals(brand, car.getBrand());
        assertEquals(carRequest.getModel(), car.getModel());
        assertEquals(carRequest.getMileage(), car.getMileage());
        assertEquals(carRequest.getPrice(), car.getPrice());
        assertEquals(carRequest.getYear(), car.getYear());
        assertEquals(carRequest.getDescription(), car.getDescription());
        assertEquals(carRequest.getFuelType(), car.getFuelType());
    }
}