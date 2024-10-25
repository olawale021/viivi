package com.example.viivi.models.products;


import com.example.viivi.models.category.CategoryModel;
import com.example.viivi.models.products.ProductModel;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByCategory(CategoryModel category);

    List<ProductModel> findByIsActiveTrue();
    
    List<ProductModel> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    @Override
    @NonNull
    Page<ProductModel> findAll(@NonNull Pageable pageable);

    @Query("SELECT p FROM ProductModel p WHERE "
    + "(COALESCE(:categoryIds, NULL) IS NULL OR p.category.id IN :categoryIds) "
    + "AND (:minPrice IS NULL OR p.price >= :minPrice) "
    + "AND (:maxPrice IS NULL OR p.price <= :maxPrice) "
    + "AND (:searchQuery IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
Page<ProductModel> findAllWithFiltersAndSearch(
    @Param("categoryIds") List<Long> categoryIds,
    @Param("minPrice") Double minPrice,
    @Param("maxPrice") Double maxPrice,
    @Param("searchQuery") String searchQuery,
    Pageable pageable);

}
