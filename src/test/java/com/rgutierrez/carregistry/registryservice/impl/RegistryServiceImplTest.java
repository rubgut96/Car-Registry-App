package com.rgutierrez.carregistry.registryservice.impl;

import com.rgutierrez.carregistry.domain.Brand;
import com.rgutierrez.carregistry.domain.Car;
import com.rgutierrez.carregistry.registryservice.converters.BrandConverter;
import com.rgutierrez.carregistry.registryservice.converters.CarConverter;
import com.rgutierrez.carregistry.repository.BrandRepository;
import com.rgutierrez.carregistry.repository.CarRepository;
import com.rgutierrez.carregistry.repository.entities.BrandEntity;
import com.rgutierrez.carregistry.repository.entities.CarEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class RegistryServiceImplTest {

    @InjectMocks
    private RegistryServiceImpl registryService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarConverter carConverter;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private BrandConverter brandConverter;
    private Brand brand;
    private Brand createBrand() {
        Brand brand = new Brand();
        brand.setId(1);
        brand.setName("name");
        brand.setWarranty(1);
        brand.setCountry("country");
        return brand;
    }
    private Car car;
    private Car createCar(){
        Car car = new Car();
        car.setId(1);
        car.setBrand(brand);
        car.setModel("model");
        car.setMileage(1);
        car.setPrice(1.0);
        car.setYear(1);
        car.setDescription("description");
        car.setFuelType("fuelType");
        return car;
    }
    private BrandEntity brandEntity;
    private BrandEntity createBrandEntity() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(brand.getId());
        brandEntity.setName(brand.getName());
        brandEntity.setWarranty(brand.getWarranty());
        brandEntity.setCountry(brand.getCountry());
        return brandEntity;
    }
    private CarEntity carEntity;
    private CarEntity createCarEntity(){
        CarEntity carEntity = new CarEntity();
        carEntity.setId(car.getId());
        carEntity.setBrand(brandEntity);
        carEntity.setModel(car.getModel());
        carEntity.setMileage(car.getMileage());
        carEntity.setPrice(car.getPrice());
        carEntity.setYear(car.getYear());
        carEntity.setDescription(car.getDescription());
        carEntity.setFuelType(car.getFuelType());
        return carEntity;
    }

    @BeforeEach
    public void setUp() {
        brand = createBrand();
        car = createCar();
        brandEntity = createBrandEntity();
        carEntity = createCarEntity();
    }
    @Test
    void saveBrand_test_success() {

        when(brandRepository.findById(brand.getId())).thenReturn(Optional.empty());
        when(brandConverter.brandToEntity(brand)).thenReturn(brandEntity);
        when(brandRepository.save(brandEntity)).thenReturn(brandEntity);
        when(brandConverter.entityToBrand(brandEntity)).thenReturn(brand);

        Brand result = registryService.saveBrand(brand);

        assertEquals(brand, result);
    }
    @Test
    void saveBrand_test_failure(){

        when(brandRepository.findById(brand.getId())).thenReturn(Optional.of(brandEntity));

        assertThrows(IllegalArgumentException.class, () -> registryService.saveBrand(brand));
    }
    @Test
    void saveCar_test_success() {

        when(carRepository.findById(car.getId())).thenReturn(Optional.empty());
        when(carConverter.carToEntity(car)).thenReturn(carEntity);
        when(carRepository.save(carEntity)).thenReturn(carEntity);
        when(carConverter.entityToCar(carEntity)).thenReturn(car);

        Car result = registryService.saveCar(car);

        assertEquals(car, result);
    }
    @Test
    void saveCar_test_failure(){

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(carEntity));

        assertThrows(IllegalArgumentException.class, () -> registryService.saveCar(car));
    }
    @Test
    void showCarById_test_success(){

        when(carRepository.findById(carEntity.getId())).thenReturn(Optional.of(carEntity));
        when(carConverter.entityToCar(Optional.of(carEntity).get())).thenReturn(car);

        Car result = registryService.showCarById(car.getId());

        assertEquals(car, result);
    }
    @Test
    void showCarById_test_failure(){

        int idNotFound = 1;

        when(carRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> registryService.showCarById(idNotFound));
    }
    @Test
    void showBrandById_test_success(){

        when(brandRepository.findById(brandEntity.getId())).thenReturn(Optional.of(brandEntity));
        when(brandConverter.entityToBrand(Optional.of(brandEntity).get())).thenReturn(brand);

        Brand result = registryService.showBrandById(brand.getId());

        assertEquals(brand, result);
    }
    @Test
    void showBrandById_test_failure(){

        int idNotFound = 1;

        when(brandRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> registryService.showBrandById(idNotFound));
    }
    @Test
    void deleteCarById_test_success(){

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(carEntity));

        registryService.deleteCarById(car.getId());

        verify(carRepository).deleteById(car.getId());
    }
    @Test
    void deleteCarById_test_failure2(){

        Integer id = car.getId();

        when(carRepository.findById(car.getId())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> registryService.deleteCarById(id));
    }
    @Test
    void deleteBrandById_test_success(){

        when(brandRepository.findById(brand.getId())).thenReturn(Optional.of(brandEntity));
        when(carRepository.countCarsByBrandId(brand.getId())).thenReturn(0);

        registryService.deleteBrandById(brand.getId());

        verify(brandRepository).deleteById(brand.getId());
    }
    @Test
    void deleteBrandById_test_failure(){

        Integer id = brand.getId();

        when(brandRepository.findById(brand.getId())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> registryService.deleteBrandById(id));
    }
    @Test
    void deleteBrandById_test_failure2(){

        Integer id = brand.getId();

        when(brandRepository.findById(brand.getId())).thenReturn(Optional.of(brandEntity));
        when(carRepository.countCarsByBrandId(brand.getId())).thenReturn(3);

        assertThrows(RuntimeException.class, () -> registryService.deleteBrandById(id));
    }
    @Test
    void updateCar_test_success(){

        CarEntity oldCarEntity = new CarEntity();

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(oldCarEntity));
        when(carConverter.carToEntity(car)).thenReturn(carEntity);
        when(carRepository.save(carEntity)).thenReturn(carEntity);
        when(carConverter.entityToCar(carEntity)).thenReturn(car);

        Car result = registryService.updateCar(car);

        assertEquals(car, result);
    }
    @Test
    void updateCar_test_failure(){

        when(carRepository.findById(car.getId())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> registryService.updateCar(car));
    }
    @Test
    void updateBrand_test_success(){

        BrandEntity oldBrandEntity = new BrandEntity();

        when(brandRepository.findById(brand.getId())).thenReturn(Optional.of(oldBrandEntity));
        when(brandConverter.brandToEntity(brand)).thenReturn(brandEntity);
        when(brandRepository.save(brandEntity)).thenReturn(brandEntity);
        when(brandConverter.entityToBrand(brandEntity)).thenReturn(brand);

        Brand result = registryService.updateBrand(brand);

        assertEquals(brand, result);
    }
    @Test
    void updateBrand_test_failure(){

        when(brandRepository.findById(brand.getId())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> registryService.updateBrand(brand));
    }
    @Test
    void showCars_test_success() throws Exception {

        List<Car> cars = new ArrayList<>();
        cars.add(car);

        List<CarEntity> carEntities = new ArrayList<>();
        carEntities.add(carEntity);

        when(carRepository.findAll()).thenReturn(carEntities);
        when(carConverter.entityToCar(carEntity)).thenReturn(car);

        CompletableFuture<List<Car>> resultFuture = registryService.showCars();

        List<Car> result = resultFuture.get();
        assertEquals(cars, result);
        assertEquals(cars.size(), result.size());
    }

    @Test
    void showBrands_test_success() throws Exception{

        List<Brand> brands = new ArrayList<>();
        brands.add(brand);

        List<BrandEntity> brandEntities = new ArrayList<>();
        brandEntities.add(brandEntity);

        when(brandRepository.findAll()).thenReturn(brandEntities);
        when(brandConverter.entityToBrand(brandEntity)).thenReturn(brand);

        CompletableFuture<List<Brand>> resultFuture = registryService.showBrands();

        List<Brand> result = resultFuture.get();
        assertEquals(brands, result);
        assertEquals(brands.size(), result.size());
    }
    @Test
    void showCarsByBrandId_test_success() throws Exception {

        List<Car> cars = new ArrayList<>();
        cars.add(car);

        List<CarEntity> carEntities = new ArrayList<>();
        carEntities.add(carEntity);

        when(carRepository.findCarsByBrandId(any(Integer.class))).thenReturn(carEntities);
        when(carConverter.entityToCar(carEntity)).thenReturn(car);

        CompletableFuture<List<Car>> result = registryService.showCarsByBrandId(car.getBrand().getId());

        assertEquals(cars, result.get());
    }
    @Test
    void showCarsByBrandIdAndByPriceRange_success() throws Exception {

        List<Car> cars = new ArrayList<>();
        cars.add(car);

        List<CarEntity> carEntities = new ArrayList<>();
        carEntities.add(carEntity);

        when(carRepository.findCarsByBrandIdAndByPriceRange(any(Integer.class), any(Double.class), any(Double.class))).thenReturn(carEntities);
        when(carConverter.entityToCar(carEntity)).thenReturn(car);

        CompletableFuture<List<Car>> result = registryService.showCarsByBrandIdAndByPriceRange(car.getBrand().getId(),.00, 2.00);

        assertEquals(cars, result.get());
    }
    @Test
    void showCarsByPrice_test_success() throws Exception {

        List<Car> cars = new ArrayList<>();
        cars.add(car);

        List<CarEntity> carEntities = new ArrayList<>();
        carEntities.add(carEntity);

        when(carRepository.findCarsByPriceRange(1.00, 2.00)).thenReturn(carEntities);
        when(carConverter.entityToCar(carEntity)).thenReturn(car);

        CompletableFuture<List<Car>> result = registryService.showCarsByPrice(1.00, 2.00);

        assertEquals(cars, result.get());
    }
    @Test
    void saveCars_test_success() throws Exception{

        List<Car> cars = new ArrayList<>();
        cars.add(car);

        List<CarEntity> carEntities = new ArrayList<>();
        carEntities.add(carEntity);

        Set<Integer> idsCarEntities = new HashSet<>();

        when(carRepository.findCarEntitiesIds()).thenReturn(idsCarEntities);
        when(carConverter.carToEntity(car)).thenReturn(carEntity);
        when(carRepository.saveAll(carEntities)).thenReturn(carEntities);
        when(carConverter.entityToCar(carEntity)).thenReturn(car);

        CompletableFuture<List<Car>> futureResult = registryService.saveCars(cars);

        assertEquals(cars, futureResult.get());
    }
    @Test
    void saveCars_test_failure(){

        List<Car> cars = new ArrayList<>();
        cars.add(car);

        Set<Integer> idsCarEntities = new HashSet<>(List.of(1));

        when(carRepository.findCarEntitiesIds()).thenReturn(idsCarEntities);

        assertThrows(RuntimeException.class, () -> registryService.saveCars(cars));
    }
    @Test
    void deleteAll_test_success(){
        registryService.deleteAll();

        verify(carRepository).deleteAll();
        verify(brandRepository).deleteAll();
    }
}
