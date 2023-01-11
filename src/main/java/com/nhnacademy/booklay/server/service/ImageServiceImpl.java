package com.nhnacademy.booklay.server.service;

import com.nhnacademy.booklay.server.entity.Image;
import com.nhnacademy.booklay.server.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{
  private final ImageRepository imageRepository;

  @Override
  public Image createImage(Image image) {
    return imageRepository.save(image);
  }
}
