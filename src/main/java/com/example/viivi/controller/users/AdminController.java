package com.example.viivi.controller.users;


import com.example.viivi.models.products.ProductModel;
import com.example.viivi.models.products.ProductRepository;
import com.example.viivi.models.users.UserRepository;
import com.example.viivi.models.category.CategoryRepository;
import com.example.viivi.models.category.CategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    // private final OrderRepository orderRepository;
    // private final UserRepository userRepository;

    @Autowired
    public AdminController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        // this.orderRepository = orderRepository;
        // this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Querying the repository to get total counts for dashboard
        long totalProducts = productRepository.count();
        long totalCategories = categoryRepository.count();
        // long totalOrders = orderRepository.count();
        // long totalUsers = userRepository.count();
        // Assuming you have a method to calculate total revenue
        // double totalRevenue = orderRepository.calculateTotalRevenue(); // Optional

        // Add the values to the model
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalCategories", totalCategories);
        // // model.addAttribute("totalOrders", totalOrders);
        // model.addAttribute("totalUsers", totalUsers);
        // model.addAttribute("totalRevenue", totalRevenue);

        return "admin-dashboard"; // Maps to the Thymeleaf template `admin-dashboard.html`
    }

    // Display all products
    @GetMapping("/products")
    public String listProducts(Model model) {
        List<ProductModel> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "admin-product-list"; // Assuming this maps to a Thymeleaf template or a view for listing products
    }

    // Show form for creating a new product
    @GetMapping("/add-product")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new ProductModel());
        model.addAttribute("categories", categoryRepository.findAll());
        return "add-product"; // Assuming this maps to a form for creating a new product
    }

    // Handle the creation of a new product
    @PostMapping("/add-product")
    public String createProduct(@ModelAttribute ProductModel product) {
        productRepository.save(product);
        return "redirect:/admin/products"; // Redirect to product listing after creating
    }

    // Show form for editing an existing product
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        Optional<ProductModel> product = productRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "/admin/products/edit"; // Assuming this maps to a form for editing the product
        } else {
            return "redirect:/admin/products"; // Redirect if the product is not found
        }
    }

    // Handle updating an existing product
    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute ProductModel product) {
        Optional<ProductModel> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            ProductModel updatedProduct = existingProduct.get();
            updatedProduct.setName(product.getName());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setCategoryId(product.getCategoryId());
            updatedProduct.setStockQuantity(product.getStockQuantity());
            updatedProduct.setIsActive(product.getIsActive());
            updatedProduct.setTags(product.getTags());
            updatedProduct.setRating(product.getRating());
            productRepository.save(updatedProduct);
        }
        return "redirect:/admin/products"; // Redirect to product listing after updating
    }

    // Handle deleting a product
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin/products"; // Redirect to product listing after deletion
    }

    // API to get product details by ID (Optional - for AJAX or API purposes)
    @GetMapping("/products/{id}")
    public String getProductById(@PathVariable("id") Long id, Model model) {
        Optional<ProductModel> product = productRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product-details"; // Assuming this is the name of your product details view
        } else {
            return "redirect:/admin/products/list"; // Redirect to product listing if not found
        }
    }

    // GET: Display the form to add a new category
    @GetMapping("/add-category")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new CategoryModel());  // Adds a new empty Category object for the form
        return "add-category";  // Refers to the Thymeleaf template
    }

    // POST: Handle the form submission to add a new category// POST: Handle the form submission for adding a new category
    @PostMapping("/add-category")
    public String addCategory(@ModelAttribute CategoryModel category) {
        categoryRepository.save(category);  // Save the new category
        return "redirect:/admin/categories";  // Redirect to the list of categories (or another page)
    }

    // GET: Display all categories
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());  // Fetch all categories
        return "categories";  // Refers to the Thymeleaf template to display the categories
    }
}
