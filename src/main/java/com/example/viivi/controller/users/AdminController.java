package com.example.viivi.controller.users;


import com.example.viivi.models.products.ProductModel;
import com.example.viivi.models.products.ProductPhotosModel;
import com.example.viivi.models.products.ProductPhotosRepository;
import com.example.viivi.models.products.ProductRepository;
import com.example.viivi.models.orders.OrderItemsRepository;
import com.example.viivi.models.orders.OrdersModel;
import com.example.viivi.models.orders.OrdersRepository;
import com.example.viivi.models.users.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import com.example.viivi.models.category.CategoryRepository;
import com.example.viivi.models.category.CategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.viivi.config.SaveImageFile;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductPhotosRepository productPhotosRepository;
    private final SaveImageFile saveImageFile;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    private final OrderItemsRepository orderItemsRepository;

    @Autowired
    public AdminController(ProductRepository productRepository, CategoryRepository categoryRepository,
    ProductPhotosRepository productPhotosRepository, SaveImageFile saveImageFile, OrdersRepository ordersRepository, UserRepository userRepository, OrderItemsRepository orderItemsRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.ordersRepository = ordersRepository;
        this.userRepository = userRepository;
        this.productPhotosRepository = productPhotosRepository;
        this.saveImageFile = saveImageFile;
        this.orderItemsRepository = orderItemsRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpServletRequest request, Model model) {

        String currentUri = request.getRequestURI();

        // Querying the repository to get total counts for dashboard
        
        long totalProducts = productRepository.count();
        long totalCategories = categoryRepository.count();
        long totalOrders = ordersRepository.count();
        long totalUsers = userRepository.count();
        // Assuming you have a method to calculate total revenue
        // double totalRevenue = orderRepository.calculateTotalRevenue(); // Optional

        // Add the values to the model
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("currentUri", currentUri);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalUsers", totalUsers);
        // model.addAttribute("totalRevenue", totalRevenue);

        return "admin-dashboard"; // Maps to the Thymeleaf template `admin-dashboard.html`
    }

    // Get all orders for the admin view
    @GetMapping("/view-orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", ordersRepository.findAll());
        return "admin-orders";  // This is the Thymeleaf template you will create
    }

    // Update order status
    @PostMapping("/update-order/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        Optional<OrdersModel> orderOptional = ordersRepository.findById(orderId);
        
        if (orderOptional.isPresent()) {
            OrdersModel order = orderOptional.get();
            order.setStatus(status);
            ordersRepository.save(order);
        }
        
        return "redirect:/admin/view-orders";  // Redirect back to the orders list after updating
    }

    // Get all orders for the admin view
    // @GetMapping("/orders")
    // public String listOrders(Model model) {
    //     List<OrdersModel> orders = ordersRepository.findAll();
    //     model.addAttribute("orders", orders);
    //     return "admin-orders";
    // }

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
    // @GetMapping("/products/edit/{id}")
    // public String showEditProductForm(@PathVariable("id") Long id, Model model) {
    //     Optional<ProductModel> product = productRepository.findById(id);
    //     if (product.isPresent()) {
    //         model.addAttribute("product", product.get());
    //         return "edit-product"; // Assuming this maps to a form for editing the product
    //     } else {
    //         return "redirect:/admin/products"; // Redirect if the product is not found
    //     }
    // }

    // Handle updating an existing product
    // @PostMapping("/products/update/{id}")
    // public String updateProduct(@PathVariable("id") Long id, @ModelAttribute ProductModel product) {
    //     Optional<ProductModel> existingProduct = productRepository.findById(id);
    //     if (existingProduct.isPresent()) {
    //         ProductModel updatedProduct = existingProduct.get();
    //         updatedProduct.setName(product.getName());
    //         updatedProduct.setDescription(product.getDescription());
    //         updatedProduct.setPrice(product.getPrice());
    //         updatedProduct.setCategoryId(product.getCategoryId());
    //         updatedProduct.setStockQuantity(product.getStockQuantity());
    //         updatedProduct.setIsActive(product.getIsActive());
    //         updatedProduct.setTags(product.getTags());
    //         updatedProduct.setRating(product.getRating());
    //         productRepository.save(updatedProduct);
    //     }
    //     return "redirect:/admin/products"; // Redirect to product listing after updating
    // }

    @GetMapping("/edit-product/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        Optional<ProductModel> product = productRepository.findById(id);
        if (product.isPresent()) {
            List<ProductPhotosModel> productPhotos = productPhotosRepository.findByProductId(id); // Fetch product photos
            model.addAttribute("product", product.get());
            model.addAttribute("productPhotos", productPhotos); // Pass the photos to the model
            model.addAttribute("categories", categoryRepository.findAll()); // Add categories to the model
            return "edit-product"; // Thymeleaf template for editing the product
        } else {
            return "redirect:/admin/products"; // Redirect if the product is not found
        }
    }
    

    @PostMapping("/edit-product/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute ProductModel product) {
        Optional<ProductModel> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            product.setId(id);  // Set the ID to ensure the update happens
            productRepository.save(product);  // Save the updated product
            return "redirect:/admin/products";
        } else {
            return "redirect:/admin/products";  // Redirect if product not found
        }
    }

   // Set a photo as the primary photo
    @GetMapping("/set-primary-photo/{photoId}")
    public String setPrimaryPhoto(@PathVariable("photoId") Long photoId) {
        Optional<ProductPhotosModel> photo = productPhotosRepository.findById(photoId);
        if (photo.isPresent()) {
            ProductPhotosModel productPhoto = photo.get();
            Long productId = productPhoto.getProduct().getId();

            // Reset the previous primary photo
            ProductPhotosModel existingPrimaryPhoto = productPhotosRepository.findByProductIdAndIsPrimaryTrue(productId);
            if (existingPrimaryPhoto != null) {
                existingPrimaryPhoto.setIsPrimary(false);
                productPhotosRepository.save(existingPrimaryPhoto);
            }

            // Set the new primary photo
            productPhoto.setIsPrimary(true);
            productPhotosRepository.save(productPhoto);

            return "redirect:/admin/edit-product/" + productId;  // Redirect to edit page
        } else {
            return "redirect:/admin/products";  // Redirect if photo not found
        }
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

    // GET: Show form to add a photo to a product
    @GetMapping("/add-product-photo/{productId}")
    public String showAddProductPhotoForm(@PathVariable("productId") Long productId, Model model) {
        Optional<ProductModel> product = productRepository.findById(productId);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("productPhoto", new ProductPhotosModel());  // New empty ProductPhoto object for form binding
            return "add-product-photo";  // Assuming you have this Thymeleaf template
        } else {
            return "redirect:/admin/products";  // Redirect if product not found
        }
    }

    // POST: Add a new photo to a product
    // @PostMapping("/add-product-photo/{productId}")
    // public String addProductPhoto(@PathVariable("productId") Long productId,
    //                               @ModelAttribute ProductPhotosModel productPhoto) {
    //     Optional<ProductModel> product = productRepository.findById(productId);
    //     if (product.isPresent()) {
    //         ProductModel existingProduct = product.get();

    //         // If the new photo is marked as primary, reset the previous primary
    //         if (productPhoto.getIsPrimary()) {
    //             ProductPhotosModel existingPrimaryPhoto = productPhotosRepository.findByProductIdAndIsPrimaryTrue(productId);
    //             if (existingPrimaryPhoto != null) {
    //                 existingPrimaryPhoto.setIsPrimary(false);  // Set the old primary to false
    //                 productPhotosRepository.save(existingPrimaryPhoto);
    //             }
    //         }

    //         productPhoto.setProduct(existingProduct);
    //         productPhotosRepository.save(productPhoto);  // Save the new photo

    //         return "redirect:/admin/products";  // Redirect after adding the photo
    //     } else {
    //         return "redirect:/admin/products";  // Redirect if product not found
    //     }
    // }

    @PostMapping("/add-product-photo/{productId}")
    public String addProductPhoto(@PathVariable("productId") Long productId,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                @ModelAttribute ProductPhotosModel productPhoto) {
        Optional<ProductModel> product = productRepository.findById(productId);
        if (product.isPresent()) {
            ProductModel existingProduct = product.get();

            // Save the uploaded image file
            String photoUrl = saveImageFile.saveImageFile(imageFile);  // Save the file and get its URL
            productPhoto.setPhotoUrl(photoUrl);  // Set the photo URL in the model

            // If the new photo is marked as primary, reset the previous primary
            if (productPhoto.getIsPrimary()) {
                ProductPhotosModel existingPrimaryPhoto = productPhotosRepository.findByProductIdAndIsPrimaryTrue(productId);
                if (existingPrimaryPhoto != null) {
                    existingPrimaryPhoto.setIsPrimary(false);  // Set the old primary to false
                    productPhotosRepository.save(existingPrimaryPhoto);
                }
            }

            productPhoto.setProduct(existingProduct);
            productPhotosRepository.save(productPhoto);  // Save the new photo

            return "redirect:/admin/products";  // Redirect after adding the photo
        } else {
            return "redirect:/admin/products";  // Redirect if product not found
        }
    }

    // DELETE: Delete a photo from a product
    @PostMapping("/delete-product-photo/{photoId}")
    public String deleteProductPhoto(@PathVariable("photoId") Long photoId) {
        Optional<ProductPhotosModel> productPhoto = productPhotosRepository.findById(photoId);
        if (productPhoto.isPresent()) {
            productPhotosRepository.delete(productPhoto.get());  // Delete the photo
            return "redirect:/admin/products";  // Redirect after deleting the photo
        } else {
            return "redirect:/admin/products";  // Redirect if photo not found
        }
    }

    @GetMapping("/product-photos/{productId}")
    public String viewProductPhotos(@PathVariable("productId") Long productId, Model model) {
        Optional<ProductModel> product = productRepository.findById(productId);
        if (product.isPresent()) {
        List<ProductPhotosModel> productPhotos = productPhotosRepository.findByProductId(productId);
        model.addAttribute("product", product.get());
        model.addAttribute("productPhotos", productPhotos);
        return "admin/view-product-photos";  // Refers to the Thymeleaf template
    } else {
        return "redirect:/admin/products";  // Redirect if the product is not found
        }
    }

}
