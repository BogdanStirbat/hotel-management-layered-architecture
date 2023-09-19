package com.bstirbat.hotelmanagement.layeredarchitecture.service.impl;

import com.bstirbat.hotelmanagement.layeredarchitecture.exceptions.ResourceNotFoundException;
import com.bstirbat.hotelmanagement.layeredarchitecture.mapper.ImageReferenceMapper;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.dto.request.ImageReferenceCreateDto;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.ImageReference;
import com.bstirbat.hotelmanagement.layeredarchitecture.model.entity.RoomType;
import com.bstirbat.hotelmanagement.layeredarchitecture.repository.ImageReferenceRepository;
import com.bstirbat.hotelmanagement.layeredarchitecture.service.ImageReferenceService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ImageReferenceServiceImpl implements ImageReferenceService {

  private final ImageReferenceRepository imageReferenceRepository;

  @Autowired
  public ImageReferenceServiceImpl(ImageReferenceRepository imageReferenceRepository) {
    this.imageReferenceRepository = imageReferenceRepository;
  }

  @Override
  public ImageReference create(ImageReferenceCreateDto createDto) {
    ImageReference imageReference = ImageReferenceMapper.INSTANCE.toEntity(createDto);

    return imageReferenceRepository.save(imageReference);
  }

  @Override
  public ImageReference getById(@NotNull Long id) {

    return imageReferenceRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(String.format("Could not find image reference with id %s", id)));
  }

  @Override
  public Page<ImageReference> findAll(@NotNull Pageable pageable) {

    return imageReferenceRepository.findAll(pageable);
  }
}
