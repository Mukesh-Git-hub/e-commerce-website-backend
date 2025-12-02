package com.example.savvyproject.repositories;

import com.example.savvyproject.entities.CartItem;
import com.example.savvyproject.entities.Product;
import com.example.savvyproject.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Integer> {

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    @Query("""
        SELECT DISTINCT c 
        FROM CartItem c
        JOIN FETCH c.product p
        LEFT JOIN FETCH p.productImages img
        WHERE c.user.userId = :userId
    """)
    List<CartItem> findCartItemsWithProductDetails(int userId);
}
