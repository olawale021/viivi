package com.example.viivi.controller.products;

import java.util.List;
import java.util.Optional;
import java.util.Map;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.viivi.models.category.CategoryModel;
import com.example.viivi.models.category.CategoryRepository;
import com.example.viivi.models.favorite.FavoriteModel;
import com.example.viivi.models.favorite.FavoriteRepository;
import com.example.viivi.models.products.ProductModel;
import com.example.viivi.models.products.ProductPhotosModel;
import com.example.viivi.models.products.ProductPhotosRepository;
import com.example.viivi.models.products.ProductRepository;
import com.example.viivi.models.users.UserModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

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

    @Autowired
    private FavoriteRepository favoriteRepository;

    @GetMapping
public String showAllProducts(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "8") int size,  // Show 6 products per page
        @RequestParam(value = "category", required = false) List<Long> categoryIds,
        @RequestParam(value = "minPrice", required = false) Double minPrice,
        @RequestParam(value = "maxPrice", required = false) Double maxPrice,
        @RequestParam(value = "q", required = false) String searchQuery,  // Search query parameter
        Model model) {

    Pageable pageable = PageRequest.of(page, size);

    // Fetch paginated products with filters and search
    Page<ProductModel> productPage = productRepository.findAllWithFiltersAndSearch(categoryIds, minPrice, maxPrice, searchQuery, pageable);
    List<CategoryModel> categories = categoryRepository.findAll();

    Map<Long, String> productImages = productPage.getContent().stream().collect(Collectors.toMap(
            ProductModel::getId,
            product -> {
                ProductPhotosModel primaryPhoto = productPhotosRepository.findByProductIdAndIsPrimaryTrue(product.getId());
                return (primaryPhoto != null) ? primaryPhoto.getPhotoUrl() : "/images/default-product.png";
            }
    ));

    model.addAttribute("categoryList", categories);
    model.addAttribute("productImages", productImages);
    model.addAttribute("products", productPage.getContent());  // Pass paginated content
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", productPage.getTotalPages());

    // Pass the search query back to the view to retain it in the search input
    model.addAttribute("q", searchQuery);

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
    public String showProductById(@PathVariable Long id, Model model, @AuthenticationPrincipal UserModel currentUser) {
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
    
        // Initialize variables for favorite logic
        boolean favoriteAlreadyAdded = false;
        FavoriteModel favorite = null;
    
        // Check if the user is logged in
        if (currentUser != null) {
            // Check if the product is already in the user's favorites
            Optional<FavoriteModel> favoriteOptional = favoriteRepository.findByUserAndProduct(currentUser, product);
            if (favoriteOptional.isPresent()) {
                favoriteAlreadyAdded = true;
                favorite = favoriteOptional.get(); // Get the favorite if it exists
            }
        }
    
        // Add the current authenticated user, favorite status, and favorite to the model
        model.addAttribute("user", currentUser);
        model.addAttribute("favoriteAlreadyAdded", favoriteAlreadyAdded);
        model.addAttribute("favorite", favorite);
    
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
