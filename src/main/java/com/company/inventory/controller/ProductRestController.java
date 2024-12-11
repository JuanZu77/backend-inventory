package com.company.inventory.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.services.IProductService;
import com.company.inventory.util.ProductExcelExporter;
import com.company.inventory.util.Util;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = {"http://localhost:4200"})
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
    
    
    /**
     * Delete Product
     * @param id
     * @return
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<ProductResponseRest> delete(@PathVariable Long id){
    	
    	ResponseEntity<ProductResponseRest> response = productService.deleteById(id);
    	return response;
    	
    };
    
   
    
    /**
     * Get all products
     * @return
     */
    @GetMapping("/products")
   	public ResponseEntity<ProductResponseRest> search(){
       	
       	ResponseEntity<ProductResponseRest> response = productService.search();
       	return response;
       	
       };
    
    
       
       /**
        * Update products
        * @param picture
        * @param name
        * @param price
        * @param account
        * @param categoryId
        * @param id
        * @return
        * @throws IOException
        */
       @PutMapping("/products/{id}")
   	public ResponseEntity<ProductResponseRest> update(
   			
   			@RequestParam("picture") MultipartFile picture,
   			@RequestParam("name") String name,
   	        @RequestParam("price") float price,
               @RequestParam("account") int account,
               @RequestParam("categoryId") Long categoryId,
               @PathVariable Long id
               ) throws IOException
   	        {
   		
   	Product product = new Product();	
   	product.setName(name);
   	product.setPrice(price);
   	product.setAccount(account);
   	product.setPicture(Util.compressZLib(picture.getBytes()));
   	
   	ResponseEntity<ProductResponseRest> response = productService.update(product, categoryId, id);
   	return response;
   		
   	};
    
   	/**
   	 * export product in excel file 
   	 * @param response
   	 * @throws IOException
   	 */
   	@GetMapping("/products/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		response.setContentType("application/octet-stream"); //tipo a exportar
		String headerKey = "Content-Disposition";
		String HeaderValue = "attachment; filename=result_products.xlsx";
		response.setHeader(headerKey, HeaderValue);
		
		//llamar a todos los productos existentes en la BD
		ResponseEntity<ProductResponseRest> products = productService.search();
		
		//add data excel
		ProductExcelExporter excelExporter = new ProductExcelExporter(products.getBody().getProductResponse().getProducts());
		
		//llamo al metodo export de ProductExcelExporter y le pasamos la data
		excelExporter.export(response);
	}
	
}; //Product controller
