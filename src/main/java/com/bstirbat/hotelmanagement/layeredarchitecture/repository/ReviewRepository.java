package com.bstirbat.hotelmanagement.layeredarchitecture.repository;

import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
