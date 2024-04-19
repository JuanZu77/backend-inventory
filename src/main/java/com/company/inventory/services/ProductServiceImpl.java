package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.util.Util;

@Service
public class ProductServiceImpl implements IProductService {
	
	//INYECTAR DEPENDENCIAS con @Autowired
	//@Autowired
	//private ICategoryDao categoryDao;
	
	
	//Inyectar Dependencias con CONSTRUCTOR
	private ICategoryDao categoryDao;
	private IProductDao productDao;
	
	public ProductServiceImpl (ICategoryDao categoryDao, IProductDao productDao) {
		
		super();
		this.categoryDao = categoryDao;
		this.productDao = productDao;
	}
		
	/**
	 * Implementation of the Save Product method
	 */
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
	
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			//Search Category to set in the product
			Optional<Category> category = categoryDao.findById(categoryId);
			
			if(category.isPresent()) {
				product.setCategory(category.get());
			}else {
				response.setMetadata("Error", "-1", "Categoria No encontrada");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
			//Save Product
			Product productSaved =  productDao.save(product);
			
			if(productSaved != null) {
				list.add(productSaved);
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta OK", "00", "Producto Guardado");
			}else {
				response.setMetadata("Error", "-1", "Error al Guardar Producto");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
			
		} 
		catch(Exception e){
			e.getStackTrace(); //más info del error
			response.setMetadata("Error", "-1", "Error al consultar");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);

	}


	/**
	 * Implementation of the Search Product by ID method
	 */
	@Override
	@Transactional(readOnly=true)
	public ResponseEntity<ProductResponseRest> searchById(Long id) {
	
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			//Search product by id
			Optional<Product> product = productDao.findById(id);
			
			if(product.isPresent()) {
				//Descomprimimos la imagen y la seteamos a product.get()
				byte[] imageDescompressed = Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(imageDescompressed);
				
				list.add(product.get());
				
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta Ok", "00", "Producto encontrado");
				
			}else {
				response.setMetadata("Error", "-1", "Producto No encontrado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
					
		} 
		catch(Exception e){
			e.getStackTrace(); //más info del error
			response.setMetadata("Error", "-1", "Error al consultar por Producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	};

	
	
	
	
};
