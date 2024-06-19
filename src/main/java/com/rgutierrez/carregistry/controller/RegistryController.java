
package com.rgutierrez.carregistry.controller;

import com.rgutierrez.carregistry.controller.dtos.BrandRequest;
import com.rgutierrez.carregistry.controller.dtos.BrandResponse;
import com.rgutierrez.carregistry.controller.dtos.CarRequest;
import com.rgutierrez.carregistry.controller.dtos.CarResponse;
import com.rgutierrez.carregistry.controller.mapper.BrandMapper;
import com.rgutierrez.carregistry.controller.mapper.CarMapper;
import com.rgutierrez.carregistry.domain.Brand;
import com.rgutierrez.carregistry.domain.Car;
import com.rgutierrez.carregistry.registryservice.RegistryService;
import com.rgutierrez.carregistry.registryservice.impl.RegistryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequiredArgsConstructor
@RequestMapping("/registry")
public class RegistryController {
    private final RegistryService registryService;
    private final CarMapper carMapper;
    private final BrandMapper brandMapper;
    @PostMapping("/addCar")
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @Operation(description = "Saves the car. If the brand does not exist in the database, it will be added. If the brand exists, its data will be updated.  \n"
            +"NECESSARY ROLE: ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "The car was successfully added.")
    @ApiResponse(responseCode = "400", description = "The specified ID is in use.",content = @Content())
    //Saves the car.
    //If the brand does not exist in the database, it will be added.
    //If the brand exists, its data will be updated.
    public ResponseEntity<CarResponse> addCar(@RequestBody CarRequest carRequest){
        try{
            Car savedCar = registryService.saveCar(carMapper.toCar(carRequest));
            return ResponseEntity.ok().body(carMapper.toResponse(savedCar));
        }catch (IllegalArgumentException e){
            // Return a bad request response if the id is in use.
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/addBrand")
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @Operation(description = "Saves the brand. If the brand exists, will return a bad response.  \n"
            +"NECESSARY ROLE: ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "The brand was successfully added.")
    @ApiResponse(responseCode = "400", description = "The specified ID is in use.", content = @Content())
    //Saves the brand. If the brand exists, will return a bad response.
    public ResponseEntity<BrandResponse> addBrand(@RequestBody BrandRequest brandRequest){
        try{
            Brand savedBrand = registryService.saveBrand(brandMapper.toBrand(brandRequest));
            return ResponseEntity.ok().body(brandMapper.toResponse(savedBrand));
        }catch (IllegalArgumentException e){
            // Returns a bad request response if the brand ID is in use.
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/showCarById/{carId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_VENDOR')")
    @Operation(description = "Shows the car with the corresponding id.  \n"
            +"NECESSARY ROLE: ROLE_CLIENT or ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "The car with the specified ID was not found.", content = @Content())
    //Shows the car with the corresponding id.
    public ResponseEntity<CarResponse> showCarById(@PathVariable Integer carId){
        try{
            Car showedCar = registryService.showCarById(carId);
            return ResponseEntity.ok().body(carMapper.toResponse(showedCar));
        }catch (NullPointerException e) {
            // Return a not found response if the id is in used.
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/showBrandById/{brandId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_VENDOR')")
    @Operation(description = "Shows the brand with the corresponding id.  \n"
            +"NECESSARY ROLE: ROLE_CLIENT or ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "The brand with the specified ID was not found.", content = @Content())
    //Shows the brand with the corresponding id.
    public ResponseEntity<BrandResponse> showBrandById(@PathVariable Integer brandId){
        try{
            Brand showedBrand = registryService.showBrandById(brandId);
            return ResponseEntity.ok().body(brandMapper.toResponse(showedBrand));
        }catch (NullPointerException e) {
            // Return a not found response if the id is in use.
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/showCars")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_VENDOR')")
    @Operation(description = "Shows all cars stored in the database.  \n"
            +"NECESSARY ROLE: ROLE_CLIENT or ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Error during the process.", content = @Content())
    //Shows all cars stored in the database.
    public CompletableFuture<ResponseEntity<List<CarResponse>>> showCars(){
        try{
            return registryService.showCars().thenApply(cars -> cars.stream()
                            .map(carMapper::toResponse)
                            .toList())
                    .thenApply(ResponseEntity::ok);
        }catch(Exception e){
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }
    @GetMapping("/showBrands")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_VENDOR')")
    @Operation(description = "Shows all brands stored in the database.  \n"
            +"NECESSARY ROLE: ROLE_CLIENT or ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Error during the process.", content = @Content())
    //Shows all brands stored in the database.
    public CompletableFuture<ResponseEntity<List<BrandResponse>>> showBrands(){
        try{
            return registryService.showBrands().thenApply(brands -> brands.stream()
                            .map(brandMapper::toResponse)
                            .toList())
                    .thenApply(ResponseEntity::ok);
        }catch(Exception e){
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }
    @GetMapping("/showCarsByPriceRange/{price1}/{price2}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_VENDOR')")
    @Operation(description = "Shows all cars in the corresponding price range.  \n"
            +"NECESSARY ROLE: ROLE_CLIENT or ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "price1 must be less or equal than price2", content = @Content())
    @ApiResponse(responseCode = "500", description = "Error during the process.", content = @Content())
    //Shows all cars in the corresponding price range.
    public CompletableFuture<ResponseEntity<List<CarResponse>>> showCarsByPriceRange(@PathVariable Double price1, @PathVariable Double price2){
        if(price2<price1){
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        try{
            return registryService.showCarsByPrice(price1,price2).thenApply(cars -> cars.stream()
                            .map(carMapper::toResponse)
                            .toList())
                    .thenApply(ResponseEntity::ok);
        }catch(Exception e){
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }
    @GetMapping("/showCarsByBrandId/{brandId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_VENDOR')")
    @Operation(description = "Shows all cars that shares a given brand.  \n"
            +"NECESSARY ROLE: ROLE_CLIENT or ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "The brand with the specified ID was not found.", content = @Content())
    @ApiResponse(responseCode = "500", description = "Error during the process.", content = @Content())
    //Shows all cars that shares a given brand.
    public CompletableFuture<ResponseEntity<List<CarResponse>>> showCarsByBrandId(@PathVariable Integer brandId){
        try{
            //Checks if the brand exists.
            registryService.showBrandById(brandId);
        }catch (NullPointerException e){
            //Returns a not found response if the brand does not exist.
            return  CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        try{
            return registryService.showCarsByBrandId(brandId).thenApply(cars -> cars.stream()
                            .map(carMapper::toResponse)
                            .toList())
                    .thenApply(ResponseEntity::ok);
        }catch(Exception e){
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }
    @GetMapping("/showCarsByBrandIdAndByPriceRange/{brandId}/{price1}/{price2}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_VENDOR')")
    @Operation(description = "Shows all cars in the corresponding price range that shares a given brand.  \n"
            +"NECESSARY ROLE: ROLE_CLIENT or ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "price1 must be less or equal than price2", content = @Content())
    @ApiResponse(responseCode = "404", description = "The brand with the specified ID was not found.", content = @Content())
    @ApiResponse(responseCode = "500", description = "Error during the process.", content = @Content())
    //Shows all cars in the corresponding price range that shares a given brand.
    public CompletableFuture<ResponseEntity<List<CarResponse>>> showCarsByBrandIdAndByPriceRange(@PathVariable Integer brandId, @PathVariable Double price1, @PathVariable Double price2){
        if(price2<price1){
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        try{
            //Check if the brand exists.
            registryService.showBrandById(brandId);
        }catch (NullPointerException e){
            //Return a not found response if the brand does not exist.
            return  CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        try{
            return registryService.showCarsByBrandIdAndByPriceRange(brandId, price1, price2).thenApply(cars -> cars.stream()
                            .map(carMapper::toResponse)
                            .toList())
                    .thenApply(ResponseEntity::ok);
        }catch(Exception e){
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }
    @DeleteMapping("/deleteCarById/{carId}")
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @Operation(description = "Deletes the car with the corresponding id. It also deletes the associated brand if there are no other cars assigned to it.  \n"
            +"NECESSARY ROLE: ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "The car was successfully deleted.")
    @ApiResponse(responseCode = "404", description = "The car with the specified ID was not found.", content = @Content())
    //Deletes the car with the corresponding id.
    //It also deletes the associated brand if there are no other cars assigned to it.
    public ResponseEntity<Void> deleteCarById(@PathVariable Integer carId){
        try{
            registryService.deleteCarById(carId);
            return  ResponseEntity.ok().build();
        }catch (NullPointerException e){
            //Return a not found response if there is no car with that id.
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/deleteBrandById/{brandId}")
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @Operation(description = "Deletes the brand with the corresponding ID if there are no cars assigned to it.  \n"
            +"NECESSARY ROLE: ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "The brand was successfully deleted.")
    @ApiResponse(responseCode = "404", description = "The brand with the specified ID was not found.", content = @Content())
    @ApiResponse(responseCode = "400", description = "There are cars assigned to the corresponding brand", content = @Content())
    //Deletes the brand with the corresponding id if there are no cars assigned to it.
    public ResponseEntity<Void> deleteBrandById(@PathVariable Integer brandId){
        try{
            registryService.deleteBrandById(brandId);
            return  ResponseEntity.ok().build();
        }catch (NullPointerException e){
            //Return a not found response if there is no brand with that id.
            return ResponseEntity.notFound().build();
        }catch (IllegalArgumentException e){
            //Return a bad request response if there are cars assigned to the corresponding brand.
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/updateCar")
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @Operation(description = "Updates the car whose id is the same as the request. It does not update its corresponding brand.  \n"
            +"NECESSARY ROLE: ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "The car was successfully updated.")
    @ApiResponse(responseCode = "404", description = "The car with the specified ID was not found.", content = @Content())
    //Updates the car whose id is the same as the request. It does not update its corresponding brand.
    public ResponseEntity<CarResponse> updateCar(@RequestBody CarRequest carRequest){
        try{
            Car updatedCar = registryService.updateCar(carMapper.toCar(carRequest));
            return ResponseEntity.ok().body(carMapper.toResponse(updatedCar));
        }catch (NullPointerException e){
            // Return a not found response if the id is in use.
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/updateBrand")
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @Operation(description = "Updates the brand whose id is the same as the request.  \n"
            +"NECESSARY ROLE: ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "The brand was successfully updated.")
    @ApiResponse(responseCode = "404", description = "The brand with the specified ID was not found.", content = @Content())
    //Updates the brand whose id is the same as the request.
    public ResponseEntity<BrandResponse> updateBrand(@RequestBody BrandRequest brandRequest){
        try{
            Brand updatedBrand = registryService.updateBrand(brandMapper.toBrand(brandRequest));
            return ResponseEntity.ok().body(brandMapper.toResponse(updatedBrand));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/addCars")
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @Operation(description = "Saves a list of cars and updates its brands. If one of the corresponding brands does not " +
            "exist in the database, it will be added. Also, the brands may be updated. Car IDs must not be in use  \n"
            +"NECESSARY ROLE: ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "The list of was successfully added.")
    @ApiResponse(responseCode = "400", description = "Shows the list of used IDs.", content = @Content())
    @ApiResponse(responseCode = "500", description = "Error during the process.", content = @Content())
    //Saves a list of cars and updates its brands. If one of the corresponding brands does not exist in the database,
    //it will be added. Also, the brands may be updated. Car IDs must not be in use.
    public CompletableFuture<ResponseEntity<List<CarResponse>>> addCars(@RequestBody List<CarRequest> carsRequest){
        try{
            List<Car> cars = new ArrayList<>();
            carsRequest.forEach(carRequest -> cars.add(carMapper.toCar(carRequest)));
            return registryService.saveCars(cars)
                    .thenApply(savedCars -> savedCars
                            .stream()
                            .map(carMapper::toResponse)
                            .toList())
                    .thenApply(ResponseEntity::ok);
        }catch(Exception e){
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().build());
        }
    }
    @ExceptionHandler(RegistryServiceImpl.CarIdInUseException.class)
    //Handle the exception occurred in the above method if some of the car request IDs are in use.
    public ResponseEntity<Object> handleRuntimeException(RegistryServiceImpl.CarIdInUseException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasRole('ROLE_VENDOR')")
    @Operation(description = "Deletes all cars and brands stored in the database.  \n"
            +"NECESSARY ROLE: ROLE_VENDOR",
            security = {@SecurityRequirement(name = "JavaInUseSecurityScheme")})
    @ApiResponse(responseCode = "200", description = "Everything was successfully deleted.")
    @ApiResponse(responseCode = "500", description = "Error during the process.", content = @Content())
    //Deletes all cars and brands stored in the database.
    public ResponseEntity<Void> deleteAll(){
        try{
            registryService.deleteAll();
            return  ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}

