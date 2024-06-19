package com.rgutierrez.carregistry.registryservice.impl;

import com.rgutierrez.carregistry.domain.Brand;
import com.rgutierrez.carregistry.domain.Car;
import com.rgutierrez.carregistry.registryservice.RegistryService;
import com.rgutierrez.carregistry.registryservice.converters.BrandConverter;
import com.rgutierrez.carregistry.registryservice.converters.CarConverter;
import com.rgutierrez.carregistry.repository.BrandRepository;
import com.rgutierrez.carregistry.repository.CarRepository;
import com.rgutierrez.carregistry.repository.entities.BrandEntity;
import com.rgutierrez.carregistry.repository.entities.CarEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistryServiceImpl implements RegistryService {
    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final CarConverter carConverter;
    private final BrandConverter brandConverter;
    @Override
    public Car saveCar(Car car){
        //Verifies that the id is not in use.
        if(carRepository.findById(car.getId()).isEmpty()) {
            //Saves the car and returns its data.
            CarEntity newCarEntitySaved = carRepository.save(carConverter.carToEntity(car));
            return carConverter.entityToCar(newCarEntitySaved);
        }
        throw new IllegalArgumentException("Id "+ car.getId() +" is being used.");

    }
    @Override
    public Brand saveBrand(Brand brand){
        //Verifies that the brand ID is not in use.
        if(brandRepository.findById(brand.getId()).isEmpty()) {
            //Saves the brand and returns its data.
            BrandEntity newBrandEntitySaved = brandRepository.save(brandConverter.brandToEntity(brand));
            return brandConverter.entityToBrand(newBrandEntitySaved);
        }
        throw new IllegalArgumentException("Id "+ brand.getId() +" is being used.");
    }
    @Override
    public Car showCarById(Integer carId){
        Optional<CarEntity> carEntity = carRepository.findById(carId);
        //Verifies that the car is present in the database.
        if(carEntity.isPresent()) {
            //Returns its data.
            return carConverter.entityToCar(carEntity.get());
        }
        throw new NullPointerException("Id "+ carId +" is not being used.");
    }
    @Override
    public Brand showBrandById(Integer brandId){
        Optional<BrandEntity> brandEntity = brandRepository.findById(brandId);
        //Verifies that the brand is present in the database.
        if(brandEntity.isPresent()) {
            //Returns its data.
            return brandConverter.entityToBrand(brandEntity.get());
        }
        throw new NullPointerException("Id "+ brandId +" is not being used.");
    }
    @Override
    public void deleteCarById(Integer carId){
        Optional<CarEntity> wantedCarEntity = carRepository.findById(carId);
        //Verifies that the car is present in the database.
        if(wantedCarEntity.isEmpty()){
            throw new NullPointerException("There is no car with id "+ carId );
        }
        carRepository.deleteById(carId);
    }
    @Override
    public void deleteBrandById(Integer brandId){
        Optional<BrandEntity> wantedBrandEntity = brandRepository.findById(brandId);
        //Verifies that the brand is present in the database.
        if(wantedBrandEntity.isEmpty()){
            throw new NullPointerException("There is no brand with id "+ brandId );
        }
        //Verifies that the brand has no car assigned to it.
        if (carRepository.countCarsByBrandId(brandId)>0){
            throw new IllegalArgumentException("There is one or more cars with this brand assigned");
        }
        //Deletes the brand.
        brandRepository.deleteById(brandId);
    }
    @Override
    public Car updateCar(Car car){
        int id = car.getId();
        //Verifies that the car is present in the database.
        Optional<CarEntity> oldCarEntity = carRepository.findById(id);
        if (oldCarEntity.isEmpty()) {
            throw new NullPointerException("There is no car with id "+ id );
        }
        //It makes sure not to change the brand.
        car.setBrand(brandConverter.entityToBrand(oldCarEntity.get().getBrand()));
        //Updates the car and returns the updated data. It also updates the corresponding brand.
        CarEntity savedCarEntity = carRepository.save(carConverter.carToEntity(car));
        return carConverter.entityToCar(savedCarEntity);
    }
    @Override
    public Brand updateBrand(Brand brand){
        int id = brand.getId();
        //Verifies that the brand is present in the database.
        Optional<BrandEntity> oldBrandEntity = brandRepository.findById(id);
        if (oldBrandEntity.isEmpty()) {
            throw new NullPointerException("There is no brand with id "+ id );
        }
        //Updates the brand and returns the updated data.
        BrandEntity savedBrandEntity = brandRepository.save(brandConverter.brandToEntity(brand));
        return brandConverter.entityToBrand(savedBrandEntity);
    }
    @Override
    @Async
    public CompletableFuture<List<Car>> showCars() {
        List<CarEntity> carEntities = carRepository.findAll();
        return CompletableFuture.completedFuture(carEntities.stream()
                .map(carConverter::entityToCar)
                .toList());
    }
    @Override
    @Async
    public CompletableFuture<List<Brand>> showBrands() {
        List<BrandEntity> brandEntities = brandRepository.findAll();
        return CompletableFuture.completedFuture(brandEntities.stream()
                .map(brandConverter::entityToBrand)
                .toList());
    }
    @Override
    @Async
    public CompletableFuture<List<Car>> showCarsByPrice(Double price1,Double price2){
        List<CarEntity> carEntities = carRepository.findCarsByPriceRange(price1, price2);
        return CompletableFuture.completedFuture(carEntities.stream()
                .map(carConverter::entityToCar)
                .toList());
    }
    @Override
    @Async
    public CompletableFuture<List<Car>> showCarsByBrandId(Integer brandId){
        List<CarEntity> carEntities = carRepository.findCarsByBrandId(brandId);
        return CompletableFuture.completedFuture(carEntities.stream()
                .map(carConverter::entityToCar)
                .toList());
    }
    @Override
    @Async
    public CompletableFuture<List<Car>> showCarsByBrandIdAndByPriceRange(Integer brandId, Double price1,Double price2){
        List<CarEntity> carEntities = carRepository.findCarsByBrandIdAndByPriceRange(brandId, price1, price2);
        return CompletableFuture.completedFuture(carEntities.stream()
                .map(carConverter::entityToCar)
                .toList());
    }
    @Override
    @Async
    public CompletableFuture<List<Car>> saveCars(List<Car> cars) {
        //Obtains the cars ids.
        Set<Integer> idsCars = cars.stream()
                .map(Car::getId)
                .collect(Collectors.toSet());
        Set<Integer> idsCarEntities = carRepository.findCarEntitiesIds();
        //Checks that the cars ids are not in use.
        if(!Collections.disjoint(idsCars, idsCarEntities)){
            idsCars.retainAll(idsCarEntities);
            throw new CarIdInUseException("Ids: " + idsCars + " are being used.");
        }
        //Proceeds to add the cars to the database.
        List<CarEntity> carEntities = cars.stream()
                .map(carConverter::carToEntity)
                .toList();
        List<CarEntity> savedCarEntities = carRepository.saveAll(carEntities);
        List<Car> savedCars = savedCarEntities.stream()
                .map(carConverter::entityToCar)
                .toList();
        return CompletableFuture.completedFuture(savedCars);
    }
    public static class CarIdInUseException extends RuntimeException {
        public CarIdInUseException(String message) {
            super(message);
        }
    }
    @Override
    public void deleteAll() {
        carRepository.deleteAll();
        brandRepository.deleteAll();
    }
}
