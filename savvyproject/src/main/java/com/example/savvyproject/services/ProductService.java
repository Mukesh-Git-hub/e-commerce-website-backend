package com.example.savvyproject.services;

import com.example.savvyproject.entities.Category;
import com.example.savvyproject.entities.Product;
import com.example.savvyproject.entities.ProductImage;
import com.example.savvyproject.repositories.CategoryRepository;
import com.example.savvyproject.repositories.ProductImageRepository;
import com.example.savvyproject.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getProductsByCategory(String categoryName) {

        // If category is empty, return all products
        if (categoryName == null || categoryName.isEmpty()) {
            return productRepository.findAll();
        }

        // Otherwise look up category by name
        Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);

        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            return productRepository.findByCategory_CategoryId(category.getCategoryId());
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    public List<String> getProductImages(Integer productId) {

        List<ProductImage> productImages =
                productImageRepository.findByProduct_ProductId(productId);

        List<String> imageUrls = new ArrayList<>();

        for (ProductImage image : productImages) {
            imageUrls.add(image.getImageUrl());
        }

        return imageUrls;
    }
}
