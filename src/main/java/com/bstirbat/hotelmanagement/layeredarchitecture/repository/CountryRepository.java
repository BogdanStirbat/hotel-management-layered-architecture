package com.bstirbat.hotelmanagement.layeredarchitecture.repository;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {

}
