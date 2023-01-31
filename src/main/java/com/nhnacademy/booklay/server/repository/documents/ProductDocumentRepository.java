package com.nhnacademy.booklay.server.repository.documents;

import com.nhnacademy.booklay.server.entity.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument,Long> {
}
