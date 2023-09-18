package com.bstirbat.hotelmanagement.layeredarchitecture.repository;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

}
