package com.example.savvyproject.services;

import com.example.savvyproject.entities.CartItem;
import com.example.savvyproject.entities.Product;
import com.example.savvyproject.entities.ProductImage;
import com.example.savvyproject.entities.User;
import com.example.savvyproject.repositories.CartRepository;
import com.example.savvyproject.repositories.ProductImageRepository;
import com.example.savvyproject.repositories.ProductRepository;
import com.example.savvyproject.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    // 1️⃣ Add item to cart
    public void addToCart(int userId, int productId, int quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartRepository.save(cartItem);
        } else {
            CartItem newItem = new CartItem(user, product, quantity);
            cartRepository.save(newItem);
        }
    }

    // 2️⃣ Update quantity
    public void updateCartItemQuantity(int userId, int productId, int quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();

            if (quantity == 0) {
                cartRepository.delete(cartItem);
            } else {
                cartItem.setQuantity(quantity);
                cartRepository.save(cartItem);
            }
        }
    }

    // 3️⃣ Remove item
    public void deleteCartItem(int userId, int productId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(user, product);

        existingItem.ifPresent(cartRepository::delete);
    }

    // 4️⃣ Get Cart Items
    public Map<String, Object> getCartItems(int userId) {

        List<CartItem> cartItems = cartRepository.findCartItemsWithProductDetails(userId);

        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        response.put("username", user.getUsername());
        response.put("role", user.getRole().toString());

        List<Map<String, Object>> products = new ArrayList<>();

        int overallTotalPrice = 0;

        for (CartItem cartItem : cartItems) {

            Map<String, Object> productDetails = new HashMap<>();

            Product product = cartItem.getProduct();

            List<ProductImage> images =
                    productImageRepository.findByProduct_ProductId(product.getProductId());

            String imageUrl = images.isEmpty() ? "default-image-url" : images.get(0).getImageUrl();

            productDetails.put("product_id", product.getProductId());
            productDetails.put("image_url", imageUrl);
            productDetails.put("name", product.getName());
            productDetails.put("description", product.getDescription());
            productDetails.put("price_per_unit", product.getPrice());
            productDetails.put("quantity", cartItem.getQuantity());

            double totalPrice =
                    cartItem.getQuantity() * product.getPrice().doubleValue();

            productDetails.put("total_price", totalPrice);

            products.add(productDetails);

            overallTotalPrice += totalPrice;
        }

        Map<String, Object> cart = new HashMap<>();
        cart.put("products", products);
        cart.put("overall_total_price", overallTotalPrice);

        response.put("cart", cart);

        return response;
    }

	

	
}
