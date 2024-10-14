package com.example.viivi.models.cart;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.viivi.models.users.UserModel;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartModel, Long> {
    @Query("SELECT c FROM CartModel c LEFT JOIN FETCH c.items WHERE c.user = :user")
    Optional<CartModel> findByUserWithItems(@Param("user") UserModel user);

    @Query("SELECT c FROM CartModel c LEFT JOIN FETCH c.items WHERE c.user.id = :userId")
    Optional<CartModel> findByUserIdWithItems(@Param("userId") Long userId);

    Optional<CartModel> findByUserId(Long userId);
}
