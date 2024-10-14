package com.example.viivi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.viivi.models.cart.CartItemModel;
import com.example.viivi.models.cart.CartModel;
import com.example.viivi.models.cart.CartRepository;
import com.example.viivi.models.users.UserModel;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CartRepository cartRepository;

    @ModelAttribute("cartQuantity")
    public int cartQuantity(@AuthenticationPrincipal UserModel user) {
        if (user != null) {
            Optional<CartModel> cartOptional = cartRepository.findByUserId(user.getId());
            if (cartOptional.isPresent()) {
                CartModel cart = cartOptional.get();
                return cart.getItems().stream().mapToInt(CartItemModel::getQuantity).sum();
            }
        }
        return 0; // Default to 0 if the user has no cart or is not logged in
    }
}
