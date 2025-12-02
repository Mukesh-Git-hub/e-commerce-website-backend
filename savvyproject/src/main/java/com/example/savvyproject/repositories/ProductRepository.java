package com.example.savvyproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.savvyproject.entities.Product;
import java.util.List;
import java.util.Locale.Category;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByCategory_CategoryId(Integer id);

	
	@Query("SELECT p.category.categoryName FROM Product p WHERE p.productId=:productId")
	String findByCategoryNameByProductId(int productId);

}
