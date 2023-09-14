package com.bstirbat.hotelmanagement.layeredarchitecture.repository;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
