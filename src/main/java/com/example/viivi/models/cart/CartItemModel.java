package com.example.viivi.models.cart;

import com.example.viivi.models.products.ProductModel;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartModel cart;


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    // Constructors
    public CartItemModel() {
    }

    public CartItemModel(CartModel cart, ProductModel product, int quantity, BigDecimal price) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartModel getCart() {
        return cart;
    }

    public void setCart(CartModel cart) {
        this.cart = cart;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // Optional: toString method for logging or debugging
    @Override
    public String toString() {
        return "CartItemModel{" +
                "id=" + id +
                ", cart=" + cart +
                ", product=" + product +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
