package com.example.viivi.models.products;

import jakarta.persistence.*;

@Entity
@Table(name = "product_photos")
public class ProductPhotosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;  // Boolean flag to indicate if this is the primary photo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    // Constructors
    public ProductPhotosModel() {
    }

    public ProductPhotosModel(String photoUrl, Boolean isPrimary, ProductModel product) {
        this.photoUrl = photoUrl;
        this.isPrimary = isPrimary;
        this.product = product;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }
}
