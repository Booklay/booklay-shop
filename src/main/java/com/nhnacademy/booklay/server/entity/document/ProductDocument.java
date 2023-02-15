package com.nhnacademy.booklay.server.entity.document;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;

import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * 엘라스틱 상품 Docs 필드.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "booklay_product")
@Setting(settingPath = "elastic/analyzer-setting.json")
@Mapping(mappingPath = "elastic/product-mapping.json")
public class ProductDocument {

    /**
     * 필수 정보.
     * 아이디, 타이틀, 짧은 제목, 긴 제목
     * 가격
     */

    // uri 정보
    @Id
    private Long id;
    private Long thumbnail;
    private String title;
    private String shortDescription;
    private String longDescription;

    private String publisher;
    private Long price;
    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime createdAt;
    private List<AuthorInfo> authors;
    private List<TagInfo> tags;

    private List<CategoryInfo> categories;
    private Boolean isDeleted;
    private Boolean isSelling;

    /**
     * 엘라스틱 상품 Docs 필드를 Query DSL 을 이용해 획득한 정보에서 얻어 생성.
     */
    public static ProductDocument fromEntity(ProductAllInOneResponse product) {

        List<AuthorInfo> authorInfos = new ArrayList<>();
        List<TagInfo> tagInfos = new ArrayList<>();
        List<CategoryInfo> categoryInfos = new ArrayList<>();

        product.getAuthors()
            .forEach(x ->
                authorInfos.add(new AuthorInfo(x.getAuthorNo(), x.getName())));

        product.getTags()
            .forEach(x ->
                tagInfos.add(new TagInfo(x.getId(), x.getName())));

        product.getCategories()
            .forEach(x ->
                categoryInfos.add(new CategoryInfo(x.getId(), x.getName())));

        String publisher = Objects.nonNull(product.getDetail())
            ? product.getDetail().getPublisher() : product.getSubscribe().getPublisher();

        return ProductDocument.builder()

            .id(product.getInfo().getId())
            .thumbnail(product.getInfo().getObjectFileId())
            .title(product.getInfo().getTitle())
            .shortDescription(product.getInfo().getShortDescription())
            .longDescription(product.getInfo().getLongDescription())

            .publisher(publisher)
            .price(product.getInfo().getPrice())
            .authors(authorInfos)
            .tags(tagInfos)
            .categories(categoryInfos)

            .createdAt(product.getInfo().getCreatedAt())
            .isDeleted(product.getInfo().isDeleted())
            .isSelling(product.getInfo().isSelling())

            .build();

    }
}
