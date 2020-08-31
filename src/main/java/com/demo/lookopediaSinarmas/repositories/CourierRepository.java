package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Courier;

@Repository
public interface CourierRepository extends CrudRepository<Courier, Long>{

}
