package com.nhnacademy.booklay.server.entity.document;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;

import com.nhnacademy.booklay.server.entity.Product;
import java.time.LocalDateTime;
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

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "booklay_product")
@Setting(settingPath = "elastic/analyzer-setting.json")
@Mapping(mappingPath = "elastic/product-mapping.json")
public class ProductDocument {


    @Id
    private Long id;

    private String title;

    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime createdAt;

    private Long price;

    private Long pointRate;

    private String shortDescription;

    private String longDescription;

    private boolean isDeleted;

    public static ProductDocument fromEntity(Product product) {

        return ProductDocument.builder()
            .id(product.getId())
            .createdAt(product.getCreatedAt())
            .isDeleted(product.isDeleted())
            .pointRate(product.getPointRate())
            .shortDescription(product.getShortDescription())
            .longDescription(product.getLongDescription())
            .title(product.getTitle())
            .price(product.getPrice())
            .build();

    }
}
