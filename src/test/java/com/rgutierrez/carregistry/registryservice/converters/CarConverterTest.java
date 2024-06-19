package com.rgutierrez.carregistry.registryservice.converters;

import com.rgutierrez.carregistry.domain.Brand;
import com.rgutierrez.carregistry.domain.Car;
import com.rgutierrez.carregistry.repository.entities.BrandEntity;
import com.rgutierrez.carregistry.repository.entities.CarEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarConverterTest {

    @InjectMocks
    private CarConverter carConverter;

    @Mock
    private BrandConverter brandConverter;

    @Test
    void carToEntity_test(){
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

        BrandEntity brandEntity = new BrandEntity();

        when(brandConverter.brandToEntity(brand)).thenReturn(brandEntity);

        CarEntity carEntity = carConverter.carToEntity(car);

        assertEquals(car.getId(), carConverter.carToEntity(car).getId());
        assertEquals(brandEntity, carConverter.carToEntity(car).getBrand());
        assertEquals(car.getModel(), carConverter.carToEntity(car).getModel());
        assertEquals(car.getMileage(), carConverter.carToEntity(car).getMileage());
        assertEquals(car.getPrice(), carConverter.carToEntity(car).getPrice());
        assertEquals(car.getYear(), carConverter.carToEntity(car).getYear());
        assertEquals(car.getDescription(), carConverter.carToEntity(car).getDescription());
        assertEquals(car.getFuelType(), carConverter.carToEntity(car).getFuelType());
    }

    @Test
    void entityToCar_test(){
        BrandEntity brandEntity = new BrandEntity();

        CarEntity carEntity = new CarEntity();
        carEntity.setId(1);
        carEntity.setBrand(brandEntity);
        carEntity.setModel("a");
        carEntity.setMileage(1);
        carEntity.setPrice(1.0);
        carEntity.setYear(1);
        carEntity.setDescription("a");
        carEntity.setFuelType("a");

        Brand brand = new Brand();

        when(brandConverter.entityToBrand(brandEntity)).thenReturn(brand);

        Car car = carConverter.entityToCar(carEntity);

        assertEquals(carEntity.getId(), car.getId());
        assertEquals(brand, car.getBrand());
        assertEquals(carEntity.getModel(), car.getModel());
        assertEquals(carEntity.getMileage(), car.getMileage());
        assertEquals(carEntity.getPrice(), car.getPrice());
        assertEquals(carEntity.getYear(), car.getYear());
        assertEquals(carEntity.getDescription(), car.getDescription());
        assertEquals(carEntity.getFuelType(), car.getFuelType());
    }

}