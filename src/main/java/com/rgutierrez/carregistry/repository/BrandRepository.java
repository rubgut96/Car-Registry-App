package com.rgutierrez.carregistry.repository;

import com.rgutierrez.carregistry.repository.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<BrandEntity,Integer> {

}
