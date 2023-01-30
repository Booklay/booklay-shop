package com.nhnacademy.booklay.server.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "view_count")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewCount {

    @Id
    @Column(name = "view_count_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_no")
    private Product product;

    @Column(name = "saved_date")
    private LocalDate savedDate;

    @Column
    private int count;

    @Builder
    public ViewCount(Product product, LocalDate savedDate, int count) {
        this.product = product;
        this.savedDate = savedDate;
        this.count = count;
    }
}
