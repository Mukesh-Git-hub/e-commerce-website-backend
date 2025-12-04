package com.example.savvyproject.controllers;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.savvyproject.entities.Product;
import com.example.savvyproject.entities.User;
import com.example.savvyproject.services.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
     @GetMapping
     public ResponseEntity<Map<String, Object>>  getProducts(@RequestParam(required = false) String Category,HttpServletRequest request){
    	 try {
    		 User authenticatedUser = (User) request.getAttribute("authenticatedUser");
    		 if(authenticatedUser == null) {
    			 return ResponseEntity.status(401).body(Map.of("error","Unauthorized access"));
    		 }
    		 //to store the category
    		 List<Product> products  =productService.getProductsByCategory(Category);
    		 
    		 Map<String,Object> response = new HashMap<>();
    		 
    		 Map<String ,String> userInfo= new HashMap<>();
    		 
    		 userInfo.put("name", authenticatedUser.getUsername());
    		 userInfo.put("role", authenticatedUser.getRole().name());
    		 response.put("user",userInfo);
    		  List<Map<String,Object>> productList= new ArrayList<>();
    		 for(Product p:products) {
    			 Map<String , Object> productDetails = new HashMap<>();
        		 productDetails.put("product_id", p.getProductId());
        		 productDetails.put("name", p.getName());
        		 productDetails.put("description", p.getDescription());
        		 productDetails.put("price", p.getPrice());
        		 productDetails.put("stock", p.getStock());
        		 List<String> images=productService.getProductImages(p.getProductId());
        		 productDetails.put("images", images);
        		 productList.add(productDetails);
        		 
        		 
    		 }
    		response.put("products",productList);
    		return ResponseEntity.ok(response);
    		  
    	 }
    	 catch(RuntimeException e) {
    		 return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
    	 }
     }
}
