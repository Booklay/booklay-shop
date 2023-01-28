package com.nhnacademy.booklay.server.service;

import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public ObjectFile createImage(ObjectFile objectFile) {
        return imageRepository.save(objectFile);
    }
}
