package com.rgutierrez.carregistry.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rgutierrez.carregistry.authservice.AuthenticationService;
import com.rgutierrez.carregistry.controller.dtos.BrandRequest;
import com.rgutierrez.carregistry.controller.dtos.BrandResponse;
import com.rgutierrez.carregistry.controller.dtos.CarRequest;
import com.rgutierrez.carregistry.controller.dtos.CarResponse;
import com.rgutierrez.carregistry.controller.mapper.BrandMapper;
import com.rgutierrez.carregistry.controller.mapper.CarMapper;
import com.rgutierrez.carregistry.domain.Brand;
import com.rgutierrez.carregistry.domain.Car;
import com.rgutierrez.carregistry.filter.JwtAuthenticationFilter;
import com.rgutierrez.carregistry.jwtservice.JwtService;
import com.rgutierrez.carregistry.registryservice.RegistryService;
import com.rgutierrez.carregistry.userservice.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@WebMvcTest(controllers = RegistryController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegistryService registryService;
    @MockBean
    private CarMapper carMapper;
    @MockBean
    private BrandMapper brandMapper;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private BrandRequest brandRequest;
    private BrandRequest createBrandRequest() {
        BrandRequest brandRequest = new BrandRequest();
        brandRequest.setId(1);
        brandRequest.setName("a");
        brandRequest.setWarranty(1);
        brandRequest.setCountry("a");
        return brandRequest;
    }
    private CarRequest carRequest;
    private CarRequest createCarRequest(){
        CarRequest carRequest = new CarRequest();
        carRequest.setId(1);
        carRequest.setBrand(brandRequest);
        carRequest.setModel("a");
        carRequest.setMileage(1);
        carRequest.setPrice(1.0);
        carRequest.setYear(1);
        carRequest.setDescription("a");
        carRequest.setFuelType("a");
        return carRequest;
    }
    private Brand brand;
    private Brand createBrand() {
        Brand brand = new Brand();
        brand.setId(brandRequest.getId());
        brand.setName(brandRequest.getName());
        brand.setWarranty(brandRequest.getWarranty());
        brand.setCountry(brandRequest.getCountry());
        return brand;
    }
    private Car car;
    private Car createCar(){
        Car car = new Car();
        car.setId(carRequest.getId());
        car.setBrand(brand);
        car.setModel(carRequest.getModel());
        car.setMileage(carRequest.getMileage());
        car.setPrice(carRequest.getPrice());
        car.setYear(carRequest.getYear());
        car.setDescription(carRequest.getDescription());
        car.setFuelType(carRequest.getFuelType());
        return car;
    }
    private BrandResponse brandResponse;
    private BrandResponse createBrandResponse() {
        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setId(brandRequest.getId());
        brandResponse.setName(brandRequest.getName());
        brandResponse.setWarranty(brandRequest.getWarranty());
        brandResponse.setCountry(brandRequest.getCountry());
        return brandResponse;
    }
    private CarResponse carResponse;
    private CarResponse createCarResponse(){
        CarResponse carResponse = new CarResponse();
        carResponse.setId(carRequest.getId());
        carResponse.setBrand(brandResponse);
        carResponse.setModel(carRequest.getModel());
        carResponse.setMileage(carRequest.getMileage());
        carResponse.setPrice(carRequest.getPrice());
        carResponse.setYear(carRequest.getYear());
        carResponse.setDescription(carRequest.getDescription());
        carResponse.setFuelType(carRequest.getFuelType());
        return carResponse;
    }
    private ObjectMapper mapper;
    private ObjectMapper createMapper(){
        return new ObjectMapper();
    }
    private String brandRequestJson;
    private String createBrandRequestJson() throws JsonProcessingException {
        return mapper.writeValueAsString(brandRequest);
    }
    private String brandResponseJson;
    private String createBrandResponseJson() throws JsonProcessingException {
        return mapper.writeValueAsString(brandResponse);
    }
    private String carRequestJson;
    private String createCarRequestJson() throws JsonProcessingException {
        return mapper.writeValueAsString(carRequest);
    }
    private String carResponseJson;
    private String createCarResponseJson() throws JsonProcessingException {
        return mapper.writeValueAsString(carResponse);
    }

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        brandRequest = createBrandRequest();
        carRequest = createCarRequest();
        brand = createBrand();
        car = createCar();
        brandResponse = createBrandResponse();
        carResponse = createCarResponse();
        mapper = createMapper();
        brandRequestJson = createBrandRequestJson();
        brandResponseJson = createBrandResponseJson();
        carRequestJson = createCarRequestJson();
        carResponseJson = createCarResponseJson();
    }
    @Test
    void addCar_test_success() throws Exception {

        when(carMapper.toCar(any(CarRequest.class))).thenReturn(car);
        when(registryService.saveCar(car)).thenReturn(car);
        when(carMapper.toResponse(car)).thenReturn(carResponse);

        this.mockMvc.
                perform(MockMvcRequestBuilders.post("/registry/addCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(carResponseJson));
    }
    @Test
    void addBrand_test_failure() throws Exception {

        when(brandMapper.toBrand(any(BrandRequest.class))).thenReturn(brand);
        when(registryService.saveBrand(brand)).thenThrow(new IllegalArgumentException());


        mockMvc.perform(MockMvcRequestBuilders.post("/registry/addBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brandRequestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void addBrand_test_success() throws Exception {

        when(brandMapper.toBrand(any(BrandRequest.class))).thenReturn(brand);
        when(registryService.saveBrand(brand)).thenReturn(brand);
        when(brandMapper.toResponse(brand)).thenReturn(brandResponse);

        this.mockMvc.
                perform(MockMvcRequestBuilders.post("/registry/addBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brandRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(brandResponseJson));
    }
    @Test
    void addCar_test_failure() throws Exception {

        when(carMapper.toCar(any(CarRequest.class))).thenReturn(car);
        when(registryService.saveCar(car)).thenThrow(new IllegalArgumentException());


        mockMvc.perform(MockMvcRequestBuilders.post("/registry/addCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carRequestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void showCarById_test_success() throws Exception {

        Integer carId = car.getId();

        when(registryService.showCarById(any(Integer.class))).thenReturn(car);
        when(carMapper.toResponse(car)).thenReturn(carResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showCarById/{carId}",carId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(carResponseJson));
    }
    @Test
    void showCarById_test_failure() throws Exception {

        Integer carId = car.getId();

        when(registryService.showCarById(any(Integer.class))).thenReturn(car);
        when(carMapper.toResponse(car)).thenThrow(new NullPointerException());

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showCarById/{carId}",carId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    void showBrandById_test_success() throws Exception {

        Integer brandId = brand.getId();

        when(registryService.showBrandById(any(Integer.class))).thenReturn(brand);
        when(brandMapper.toResponse(brand)).thenReturn(brandResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showBrandById/{brandId}",brandId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(brandResponseJson));
    }
    @Test
    void showBrandById_test_failure() throws Exception {

        Integer brandId = brand.getId();

        when(registryService.showBrandById(any(Integer.class))).thenReturn(brand);
        when(brandMapper.toResponse(brand)).thenThrow(new NullPointerException());

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showBrandById/{brandId}",brandId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    void showCars_test_success() throws Exception {

        List<Car> cars = new ArrayList<>();
        cars.add(car);

        when(registryService.showCars()).thenReturn(CompletableFuture.completedFuture(cars));
        when(carMapper.toResponse(car)).thenReturn(carResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showCars"));
        verify(registryService).showCars();
        verify(carMapper, times(1)).toResponse(car);
    }
    @Test
    void showBrands_test_success() throws Exception {

        List<Brand> brands = new ArrayList<>();
        brands.add(brand);

        when(registryService.showBrands()).thenReturn(CompletableFuture.completedFuture(brands));
        when(brandMapper.toResponse(brand)).thenReturn(brandResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showBrands"));
        verify(registryService).showBrands();
        verify(brandMapper, times(1)).toResponse(brand);

    }
    @Test
    void showCarsByPriceRange_test_failure() throws Exception {

        Double price1 = 3.;
        Double price2 = 2.;

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showCarsByPriceRange/{price1}/{price2}", price1, price2))
                        .andExpect(MockMvcResultMatchers.content().string(""));
        verify(carMapper, times(0)).toResponse(any(Car.class));
    }
    @Test
    void showCarsByPriceRange_test_success() throws Exception {

        List<Car> cars = new ArrayList<>();
        cars.add(car);
        cars.add(car);

        when(registryService.showCarsByPrice(anyDouble(), anyDouble())).thenReturn(CompletableFuture.completedFuture(cars));

        when(carMapper.toResponse(any(Car.class))).thenReturn(carResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showCarsByPriceRange/0.0/2.0"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(carMapper, times(2)).toResponse(any(Car.class));
    }
    @Test
    void showCarsByBrandId_test_success() throws Exception {

        Integer brandId = 1;

        List<Car> cars = new ArrayList<>();
        cars.add(car);
        cars.add(car);

        when(registryService.showBrandById(any(Integer.class))).thenReturn(brand);
        when(registryService.showCarsByBrandId(any(Integer.class))).thenReturn(CompletableFuture.completedFuture(cars));

        when(carMapper.toResponse(any(Car.class))).thenReturn(carResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showCarsByBrandId/{brandId}", brandId))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(carMapper, times(2)).toResponse(any(Car.class));
    }
    @Test
    void showCarsByBrandId_test_ko() throws Exception {

        Integer brandId = 1;

        List<Car> cars = new ArrayList<>();
        cars.add(car);
        cars.add(car);

        when(registryService.showBrandById(any(Integer.class))).thenThrow(new NullPointerException());
        when(registryService.showCarsByBrandId(any(Integer.class))).thenReturn(CompletableFuture.completedFuture(cars));

        when(carMapper.toResponse(any(Car.class))).thenReturn(carResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showCarsByBrandId/{brandId}", brandId));
        verify(carMapper, times(0)).toResponse(any(Car.class));
    }
    @Test
    void showCarsByBrandIdAndByPriceRange_test_failure() throws Exception {

        Integer brandId = 1;
        Double price1 = 3.0;
        Double price2 = 2.0;

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showCarsByBrandIdAndByPriceRange/{brandId}/{price1}/{price2}", brandId, price1, price2));
        verify(carMapper, times(0)).toResponse(any(Car.class));
    }
    @Test
    void showCarsByBrandIdAndByPriceRange_test_failure2() throws Exception {

        Integer brandId = 1;
        Double price1 = 2.0;
        Double price2 = 3.0;

        when(registryService.showBrandById(any(Integer.class))).thenThrow(new NullPointerException());
        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showCarsByBrandIdAndByPriceRange/{brandId}/{price1}/{price2}", brandId, price1, price2));
        verify(carMapper, times(0)).toResponse(any(Car.class));
    }
    @Test
    void showCarsByBrandIdAndByPriceRange_test_success() throws Exception {

        Integer brandId = 1;
        Double price1 = 2.0;
        Double price2 = 3.0;

        List<Car> cars = new ArrayList<>();
        cars.add(car);
        cars.add(car);

        when(registryService.showCarsByBrandIdAndByPriceRange(any(Integer.class), anyDouble(), anyDouble())).thenReturn(CompletableFuture.completedFuture(cars));

        when(carMapper.toResponse(any(Car.class))).thenReturn(carResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/registry/showCarsByBrandIdAndByPriceRange/{brandId}/{price1}/{price2}", brandId, price1, price2));
        verify(carMapper, times(2)).toResponse(any(Car.class));
    }
    @Test
    void deleteCarById_test_success() throws Exception {

        Integer carId = car.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/registry/deleteCarById/{carId}",carId))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(registryService).deleteCarById(carId);
    }
    @Test
    void deleteCarById_test_failure() throws Exception {

        Integer carId = car.getId();

        doThrow(new NullPointerException()).when(registryService).deleteCarById(carId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/registry/deleteCarById/{carId}",carId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteBrandById_test_success() throws Exception {

        Integer brandId = brand.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/registry/deleteBrandById/{brandId}",brandId))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(registryService).deleteBrandById(brandId);
    }
    @Test
    void deleteBrandById_test_failure() throws Exception {

        Integer brandId = brand.getId();

        doThrow(new NullPointerException()).when(registryService).deleteBrandById(brandId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/registry/deleteBrandById/{brandId}",brandId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    void deleteBrandById_test_failure2() throws Exception {

        Integer brandId = brand.getId();

        doThrow(new IllegalArgumentException()).when(registryService).deleteBrandById(brandId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/registry/deleteBrandById/{brandId}",brandId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateCar_test_success() throws Exception {

        when(carMapper.toCar(any(CarRequest.class))).thenReturn(car);
        when(registryService.updateCar(any(Car.class))).thenReturn(car);
        when(carMapper.toResponse(car)).thenReturn(carResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/registry/updateCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(carResponseJson));
    }

    @Test
    void updateCar_test_failure() throws Exception {

        when(registryService.updateCar(any(Car.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/registry/updateCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void updateBrand_test_success() throws Exception {

        when(brandMapper.toBrand(any(BrandRequest.class))).thenReturn(brand);
        when(registryService.updateBrand(any(Brand.class))).thenReturn(brand);
        when(brandMapper.toResponse(brand)).thenReturn(brandResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/registry/updateBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brandRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(brandResponseJson));
    }

    @Test
    void updateBrand_test_failure() throws Exception {

        when(brandMapper.toBrand(any(BrandRequest.class))).thenReturn(brand);
        when(registryService.updateBrand(any(Brand.class))).thenThrow(new NullPointerException());

        mockMvc.perform(MockMvcRequestBuilders.put("/registry/updateBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brandRequestJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void addCars_test_success() throws Exception {
        List<CarRequest> carsRequest = new ArrayList<>();
        carsRequest.add(carRequest);
        List<Car> cars = new ArrayList<>();
        cars.add(car);

        when(carMapper.toCar(any(CarRequest.class))).thenReturn(car);
        when(registryService.saveCars(anyList())).thenReturn(CompletableFuture.completedFuture(cars));
        when(carMapper.toResponse(any(Car.class))).thenReturn(carResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/registry/addCars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(carsRequest)));
                verify(registryService).saveCars(anyList());
                verify(carMapper, times(1)).toResponse(car);
    }
    @Test
    void deleteAll_test_success() throws Exception {

        doNothing().when(registryService).deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.delete("/registry/deleteAll"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(registryService).deleteAll();
    }
}