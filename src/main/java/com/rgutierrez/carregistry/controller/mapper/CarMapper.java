package com.rgutierrez.carregistry.controller.mapper;

import com.rgutierrez.carregistry.controller.dtos.CarRequest;
import com.rgutierrez.carregistry.controller.dtos.CarResponse;
import com.rgutierrez.carregistry.domain.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarMapper {
    private final BrandMapper brandMapper;

    public CarResponse toResponse(Car car){
        CarResponse carResponse = new CarResponse();

        carResponse.setId(car.getId());
        carResponse.setBrand(brandMapper.toResponse(car.getBrand()));
        carResponse.setModel(car.getModel());
        carResponse.setMileage(car.getMileage());
        carResponse.setPrice(car.getPrice());
        carResponse.setYear(car.getYear());
        carResponse.setDescription(car.getDescription());
        carResponse.setFuelType(car.getFuelType());

        return  carResponse;
    }

    public Car toCar(CarRequest carRequest){

        Car car = new Car();

        car.setId(carRequest.getId());
        car.setBrand(brandMapper.toBrand(carRequest.getBrand()));
        car.setModel(carRequest.getModel());
        car.setMileage(carRequest.getMileage());
        car.setPrice(carRequest.getPrice());
        car.setYear(carRequest.getYear());
        car.setDescription(carRequest.getDescription());
        car.setFuelType(carRequest.getFuelType());

        return car;
    }
}
