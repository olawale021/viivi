package com.example.viivi.models.favorite;


import com.example.viivi.models.favorite.FavoriteModel;
import com.example.viivi.models.users.UserModel;
import com.example.viivi.models.products.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteModel, Long> {

    Optional<FavoriteModel> findByUserAndProduct(UserModel user, ProductModel product);

    List<FavoriteModel> findAllByUser(UserModel user);

    void deleteByUserAndProduct(UserModel user, ProductModel product);
}
