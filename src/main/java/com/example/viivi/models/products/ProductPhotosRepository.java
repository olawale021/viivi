package com.example.viivi.models.products;


import com.example.viivi.models.products.ProductPhotosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPhotosRepository extends JpaRepository<ProductPhotosModel, Long> {

    List<ProductPhotosModel> findByProductId(Long productId);

    // Additional query method to get the primary photo of a product
    ProductPhotosModel findByProductIdAndIsPrimaryTrue(Long productId);
}

