package com.bstirbat.hotelmanagement.layeredarchitecture.repository;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Street;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreetRepository extends JpaRepository<Street, Long> {

}
