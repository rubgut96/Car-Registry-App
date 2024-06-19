package com.rgutierrez.carregistry.registryservice;

import com.rgutierrez.carregistry.domain.Brand;
import com.rgutierrez.carregistry.domain.Car;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface RegistryService {
    Car saveCar(Car car);
    Brand saveBrand(Brand brand);
    void deleteCarById(Integer carId);
    void deleteBrandById(Integer brandId);
    Car updateCar(Car car);
    Brand updateBrand(Brand brandId);
    Car showCarById(Integer carId);
    Brand showBrandById(Integer brandId);
    CompletableFuture<List<Car>> showCars();
    CompletableFuture<List<Brand>> showBrands();
    CompletableFuture<List<Car>> showCarsByPrice(Double price1,Double price2);
    CompletableFuture<List<Car>> showCarsByBrandId(Integer brandId);
    CompletableFuture<List<Car>> showCarsByBrandIdAndByPriceRange(Integer brandId, Double price1,Double price2);
    CompletableFuture<List<Car>> saveCars(List<Car> cars);
    void deleteAll();
}