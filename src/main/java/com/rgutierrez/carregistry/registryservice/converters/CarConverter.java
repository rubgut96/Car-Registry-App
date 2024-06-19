package com.rgutierrez.carregistry.registryservice.converters;
import com.rgutierrez.carregistry.domain.Car;
import com.rgutierrez.carregistry.repository.entities.CarEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarConverter {

    private final BrandConverter brandConverter;

    public CarEntity carToEntity(Car car){
        CarEntity carEntity = new CarEntity();

        carEntity.setId(car.getId());
        carEntity.setBrand(brandConverter.brandToEntity(car.getBrand()));
        carEntity.setModel(car.getModel());
        carEntity.setMileage(car.getMileage());
        carEntity.setPrice(car.getPrice());
        carEntity.setYear(car.getYear());
        carEntity.setDescription(car.getDescription());
        carEntity.setFuelType(car.getFuelType());

        return  carEntity;
    }

    public Car entityToCar(CarEntity carEntity){

        Car car = new Car();

        car.setId(carEntity.getId());
        car.setBrand(brandConverter.entityToBrand(carEntity.getBrand()));
        car.setModel(carEntity.getModel());
        car.setMileage(carEntity.getMileage());
        car.setPrice(carEntity.getPrice());
        car.setYear(carEntity.getYear());
        car.setDescription(carEntity.getDescription());
        car.setFuelType(carEntity.getFuelType());

        return car;
    }
}
