package com.example.viivi.controller.products;

import java.util.List;
import java.util.Optional;



import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.viivi.models.category.CategoryModel;
import com.example.viivi.models.category.CategoryRepository;
import com.example.viivi.models.products.ProductModel;
import com.example.viivi.models.products.ProductPhotosModel;
import com.example.viivi.models.products.ProductPhotosRepository;
import com.example.viivi.models.products.ProductRepository;

import java.util.stream.Collectors;


@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPhotosRepository productPhotosRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String showAllProducts(Model model) {
        List<ProductModel> products = productRepository.findAll();
        List<CategoryModel> categories = categoryRepository.findAll();

         // Fetch the primary photo for each product
        java.util.Map<Long, String> productImages = products.stream().collect(Collectors.toMap(
                ProductModel::getId,
                product -> {
                    ProductPhotosModel primaryPhoto = productPhotosRepository.findByProductIdAndIsPrimaryTrue(product.getId());
                    return (primaryPhoto != null) ? primaryPhoto.getPhotoUrl() : "/images/default-product.png";  // Default image if none found
                }
        ));
        model.addAttribute("categoryList", categories);
        model.addAttribute("productImages", productImages);
        model.addAttribute("products", products);
        return "all-products";
    }

     // Show all active products
    @GetMapping("/active")
    public String showActiveProducts(Model model) {
        List<ProductModel> activeProducts = productRepository.findByIsActiveTrue();
        model.addAttribute("products", activeProducts);
        return "active-products";
    }

    // Show product details by ID
    @GetMapping("/{id}")
    public String showProductById(@PathVariable Long id, Model model) {
        // Fetch the product from the database
        Optional<ProductModel> productOptional = productRepository.findById(id);
    
        // Check if the product is present
        if (productOptional.isEmpty()) {
            return "error/product-not-found";  // Handle case if product is not found
        }
    
        // Fetch the product and primary photo if the product is found
        ProductModel product = productOptional.get();
        ProductPhotosModel primaryPhoto = productPhotosRepository.findByProductIdAndIsPrimaryTrue(id);
        String productImage = (primaryPhoto != null) ? primaryPhoto.getPhotoUrl() : "/images/default-product.png";  // Default image if none found
    
        // Add product and image to the model
        model.addAttribute("product", product);
        model.addAttribute("productImage", productImage);
    
        // Return the product-details template
        return "product-details";
    }
    


    // Show products by category
    @GetMapping("/category/{categoryId}")
    public String showProductsByCategory(@PathVariable Long categoryId, Model model) {
        CategoryModel category = new CategoryModel();
        category.setId(categoryId);
        List<ProductModel> products = productRepository.findByCategory(category);
        model.addAttribute("products", products);
        return "category-products";  // This should map to the "category-products.html" template
    }

    
}
