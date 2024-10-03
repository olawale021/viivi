package com.example.viivi.models.products;


import com.example.viivi.models.category.CategoryModel;
import com.example.viivi.models.products.ProductModel;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByCategory(CategoryModel category);

    List<ProductModel> findByIsActiveTrue();
    
    List<ProductModel> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}
