package com.motionmate.domain.goods;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goods {

    public enum GoodsStatus {
        FOR_SALE, SOLD_OUT, HIDDEN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoodsStatus status = GoodsStatus.FOR_SALE;

    private String name;
    private String description;
    private String imageUrl;

    @Column(nullable = false)
    private Integer stock = 0;

    private Integer price;

    private boolean isLimited;
    private String category;
    private String subCategory;
    @Column(nullable = false)
    private boolean hidden = false;

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
        this.status = stock == 0 ? GoodsStatus.SOLD_OUT : GoodsStatus.FOR_SALE; // ✅ 자동 상태 설정
    }

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고 부족");
        }
        this.stock -= quantity;
    }

    public void update(String name, String description, int price, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.status = stock == 0 ? GoodsStatus.SOLD_OUT : GoodsStatus.FOR_SALE; // ✅ 상태 동기화

    }

//    public boolean isHidden() {
//        return hidden;
//    }

    public void hide() {
        this.hidden = true;
    }

    public void unhide() {
        this.hidden = false;
    }
}
