package com.example.viivi.models.cart;

import com.example.viivi.models.users.UserModel;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "carts")
public class CartModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserModel user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItemModel> items;




    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    // Constructors
    public CartModel() {
        this.items = new ArrayList<>();  // Ensure it's initialized in the default constructor
    }

    public CartModel(UserModel user, List<CartItemModel> items, double totalPrice) {
        this.user = user;
        this.items = (items != null) ? items : new ArrayList<>();  // Initialize if null
        this.totalPrice = totalPrice;
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

    public List<CartItemModel> getItems() {
        return items;
    }

    public void setItems(List<CartItemModel> items) {
        this.items = (items != null) ? items : new ArrayList<>();
        calculateTotalPrice();  // Automatically calculate total price when items are set
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Utility methods
    public void addItem(CartItemModel item) {
        if (items == null) {
            items = new ArrayList<>();  // Ensure list is initialized before adding items
        }
        items.add(item);
        item.setCart(this);
        calculateTotalPrice();
    }

    public void removeItem(CartItemModel item) {
        if (items != null) {
            items.remove(item);
            item.setCart(null);
            calculateTotalPrice();
        }
    }

    public void calculateTotalPrice() {
        this.totalPrice = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
    }

    // Optional: toString method for debugging or logging
    @Override
    public String toString() {
        return "CartModel{" +
                "id=" + id +
                ", user=" + user +
                ", totalPrice=" + totalPrice +
                ", items=" + items +
                '}';
    }
}
