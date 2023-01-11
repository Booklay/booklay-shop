package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.repository.product.ProductTagRepository;
import com.nhnacademy.booklay.server.service.product.ProductTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductTagServiceImpl implements ProductTagService {
  private final ProductTagRepository productTagRepository;

}
