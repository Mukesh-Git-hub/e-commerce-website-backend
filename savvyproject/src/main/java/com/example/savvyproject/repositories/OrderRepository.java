package com.example.savvyproject.repositories;

import com.example.savvyproject.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    // Custom query methods can be added here if needed
}
