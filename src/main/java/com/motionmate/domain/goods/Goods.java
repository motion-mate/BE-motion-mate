package com.motionmate.domain.goods;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String imageUrl;

    @Column(nullable = false)
    private Integer stock = 0;

    private Integer price;

    private boolean isLimited;
    private String category;
    private String subCategory;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String colorsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String sizesJson;

    public Goods(String name, String description, String imageUrl, Integer stock, Integer price,
                 boolean isLimited, String category, String subCategory,
                 String colorsJson, String sizesJson) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.price = price;
        this.isLimited = isLimited;
        this.category = category;
        this.subCategory = subCategory;
        this.colorsJson = colorsJson;
        this.sizesJson = sizesJson;
    }

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고 부족");
        }
        this.stock -= quantity;
    }
}
