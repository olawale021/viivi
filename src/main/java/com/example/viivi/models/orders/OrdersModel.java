package com.example.viivi.models.orders;

import com.example.viivi.models.users.UserModel;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

import java.util.List;

@Entity
@Table(name = "orders")
public class OrdersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "placed_at", nullable = false)
    private Timestamp placedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemsModel> items;
    


    // Default constructor (required by JPA)
    public OrdersModel() {
    }

    public OrdersModel(UserModel user, BigDecimal totalPrice, String status, Timestamp placedAt) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
        this.placedAt = placedAt;
    }

    public OrdersModel(UserModel user, BigDecimal totalPrice, String status, Timestamp placedAt, List<OrderItemsModel> items) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
        this.placedAt = placedAt;
        this.items = items;
    }


    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(Timestamp placedAt) {
        this.placedAt = placedAt;
    }
    public List<OrderItemsModel> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemsModel> items) {
        this.items = items;
    }
}
