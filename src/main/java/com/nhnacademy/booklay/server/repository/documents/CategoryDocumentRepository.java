package com.nhnacademy.booklay.server.repository.documents;

import com.nhnacademy.booklay.server.entity.document.CategoryDocument;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CategoryDocumentRepository
    extends ElasticsearchRepository<CategoryDocument, Long> {

    List<CategoryDocument> findAllByIdIsIn(List<Long> ids);

}
