package com.nhnacademy.booklay.server.entity.document;

import com.nhnacademy.booklay.server.entity.Category;
import java.util.Optional;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "category")
@Setting(settingPath = "elastic/analyzer-setting.json")
@Mapping(mappingPath = "elastic/category-mapping.json")
public class CategoryDocument {


    @Id
    private Long id;

    private Long parentCategoryId;

    private String name;

    private Long depth;

    private Boolean isExposure;

    @Builder
    public CategoryDocument(Long id, Long parentCategoryId, String name, Long depth,
                            Boolean isExposure) {
        this.id = id;
        this.parentCategoryId = parentCategoryId;
        this.name = name;
        this.depth = depth;
        this.isExposure = isExposure;
    }

    public static CategoryDocument fromCategory(Category category) {

        Optional<Category> parent = Optional.ofNullable(category.getParent());


        return CategoryDocument.builder()
            .id(category.getId())
            .parentCategoryId(parent.map(Category::getId).orElse(null))
            .name(category.getName())
            .depth(category.getDepth())
            .isExposure(category.getIsExposure())
            .build();
    }
}

