package com.rgutierrez.carregistry.repository;

import com.rgutierrez.carregistry.repository.entities.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CarRepository extends JpaRepository<CarEntity,Integer> {
    @Query("SELECT COUNT(c) FROM CarEntity c WHERE c.brand.id = :brandId")
    Integer countCarsByBrandId(@Param("brandId") Integer id);
    @Query("SELECT c.id FROM CarEntity c")
    Set<Integer> findCarEntitiesIds();
    @Query("SELECT c FROM CarEntity c WHERE c.price BETWEEN :price1 AND :price2")
    List<CarEntity> findCarsByPriceRange(@Param("price1") Double price1, @Param("price2") Double price2);
    @Query("SELECT c FROM CarEntity c WHERE c.brand.id = :brandId")
    List<CarEntity> findCarsByBrandId(@Param("brandId") Integer id);
    @Query("SELECT c FROM CarEntity c WHERE c.brand.id = :brandId AND c.price BETWEEN :price1 AND :price2")
    List<CarEntity> findCarsByBrandIdAndByPriceRange(@Param("brandId") Integer id, @Param("price1") Double price1, @Param("price2") Double price2);
}
