package com.company.inventory.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.services.IProductService;
import com.company.inventory.util.Util;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("api/v1")
public class ProductRestController {
	
	private IProductService productService;
	
	public ProductRestController(IProductService productService) {
		super();
		this.productService = productService;
		
	}
	
	
	/**
	 * Save Product
	 * @param picture
	 * @param name
	 * @param price
	 * @param account
	 * @param categoryId
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/products")
	public ResponseEntity<ProductResponseRest> save(
			
			@RequestParam("picture") MultipartFile picture,
			@RequestParam("name") String name,
	        @RequestParam("price") float price,
            @RequestParam("account") int account,
            @RequestParam("categoryId") Long categoryId
            ) throws IOException
	        {
		
	Product product = new Product();	
	product.setName(name);
	product.setPrice(price);
	product.setAccount(account);
	product.setPicture(Util.compressZLib(picture.getBytes()));
	
	ResponseEntity<ProductResponseRest> response = productService.save(product, categoryId);
	return response;
		
	};
	
	
	
	/**
	 * Search by id
	 * @param id
	 * @return
	 */
    @GetMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> searchById(@PathVariable Long id){
    	
    	ResponseEntity<ProductResponseRest> response = productService.searchById(id);
    	return response;
    	
    };
    
    
    /**
     * Search by name
     * @param name
     * @return
     */
    @GetMapping("/products/filter/{name}")
	public ResponseEntity<ProductResponseRest> searchByName(@PathVariable String name){
    	
    	ResponseEntity<ProductResponseRest> response = productService.searchByName(name);
    	return response;
    	
    };
    
    
    
    
    
	
}; //Product controller
