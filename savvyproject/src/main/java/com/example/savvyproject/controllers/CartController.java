package com.example.savvyproject.controllers;

import com.example.savvyproject.entities.User;
import com.example.savvyproject.services.CartService;
import com.example.savvyproject.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    // 1️⃣ Get all cart items of logged-in user
    @GetMapping("/items")
    public ResponseEntity<Map<String, Object>> getCartItems(HttpServletRequest request) {

    	User user = (User) request.getAttribute("authenticatedUser");

    	if (user == null) {
    	    return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
    	}

    	// Now it's safe
    	Map<String, Object> cartData = cartService.getCartItems(user.getUserId());

    	return ResponseEntity.ok(cartData);

    }

    // 2️⃣ Add product to cart
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> body,
                                       HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        int productId = (int) body.get("productId");
        int quantity = (int) body.get("quantity");

        cartService.addToCart(user.getUserId(), productId, quantity);

        return ResponseEntity.ok(Map.of("message", "Product added to cart"));
    }

    // 3️⃣ Update cart item quantity
    @PutMapping("/update")
    public ResponseEntity<?> updateQuantity(@RequestBody Map<String, Object> body,
                                            HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        int productId = (int) body.get("productId");
        int quantity = (int) body.get("quantity");

        cartService.updateCartItemQuantity(user.getUserId(), productId, quantity);

        return ResponseEntity.ok(Map.of("message", "Quantity updated"));
    }

    // 4️⃣ Remove item from cart
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeCartItem(@PathVariable int productId,
                                            HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");

        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        cartService.deleteCartItem(user.getUserId(), productId);

        return ResponseEntity.ok(Map.of("message", "Item removed from cart"));
    }
}
