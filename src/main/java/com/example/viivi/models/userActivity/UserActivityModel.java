package com.example.viivi.models.userActivity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity")
public class UserActivityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "activity_type", nullable = false)
    private String activityType;

    @Column(name = "activity_timestamp", nullable = false)
    private LocalDateTime activityTimestamp = LocalDateTime.now();

    @Column(name = "product_category_id")
    private Long productCategoryId;

    @Column(name = "activity_duration")
    private Integer activityDuration;


    @Column(name = "min_price_filter")
    private Double minPriceFilter;


    @Column(name = "max_price_filter")
    private Double maxPriceFilter;

    @Column(name = "category_filter")  
    private Long categoryFilter;

    @Column(name = "top_category1")  
    private Long topCategory1;

    @Column(name = "top_category2")  
    private Long topCategory2;

    @Column(name = "top_category3")  
    private Long topCategory3;

    @Column(name = "search_filter")
    private String searchFilter; 

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public LocalDateTime getActivityTimestamp() {
        return activityTimestamp;
    }

    public void setActivityTimestamp(LocalDateTime activityTimestamp) {
        this.activityTimestamp = activityTimestamp;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Integer getActivityDuration() {
        return activityDuration;
    }

    public void setActivityDuration(Integer activityDuration) {
        this.activityDuration = activityDuration;
    }

    public Double getMinPriceFilter() {
        return minPriceFilter;
    }

    public void setMinPriceFilter(Double minPriceFilter) {
        this.minPriceFilter = minPriceFilter;
    }

    public Double getMaxPriceFilter() {
        return maxPriceFilter;
    }

    public void setMaxPriceFilter(Double maxPriceFilter) {
        this.maxPriceFilter = maxPriceFilter;
    }

    public Long getCategoryFilter() {
        return categoryFilter;
    }

    public void setCategoryFilter(Long categoryFilter) {
        this.categoryFilter = categoryFilter;
    }

    public Long getTopCategory1() {
        return topCategory1;
    }

    public void setTopCategory1(Long topCategory1) {
        this.topCategory1 = topCategory1;
    }

    public Long getTopCategory2() {
        return topCategory2;
    }

    public void setTopCategory2(Long topCategory2) {
        this.topCategory2 = topCategory2;
    }

    public Long getTopCategory3() {
        return topCategory3;
    }

    public void setTopCategory3(Long topCategory3) {
        this.topCategory3 = topCategory3;
    }

    public String getSearchFilter() {
        return searchFilter;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }
}
