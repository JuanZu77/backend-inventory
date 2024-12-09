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
			response.setMetadata("Error", "-1", "Internal Error: No se pudo Guardar el Producto ");
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
				//Descomprimimos la foto, la seteamos a "picture" y la agregamos a la lista
				byte[] imageDescompressed = Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(imageDescompressed);
				
				list.add(product.get());
				
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta Ok", "00", "Producto encontrado");
				
			}else {
				response.setMetadata("Error", "-1", "No existen Productos con el ID indicado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
					
		} 
		catch(Exception e){
			e.getStackTrace(); //más info del error
			response.setMetadata("Error", "-1", "Error al consultar Producto por Id");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	
	
	
	/**
	 * Implementation of the Search Product by name method
	 */
	@Override
	@Transactional(readOnly=true)
	public ResponseEntity<ProductResponseRest> searchByName(String name) {
		
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();
		
		try {
			//Search product by Name
			listAux = productDao.findByNameContainingIgnoreCase(name);
			
			if(listAux.size() > 0) {
			
				listAux.stream().forEach((p)->{
					
					//Descomprimimos la foto, la seteamos a "picture" y la agregamos a la lista
					byte[] imageDescompressed = Util.decompressZLib(p.getPicture());
					p.setPicture(imageDescompressed);
					list.add(p);
								
				});		
				
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta Ok", "00", "Productos encontrados");
				
			}else {
				response.setMetadata("Error", "-1", "No existen Productos con el nombre indicado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
					
		} 
		catch(Exception e){
			e.getStackTrace(); //más info del error
			response.setMetadata("Error", "-1", "Error al consultar Producto por Nombre");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	
	
	/**
	 * Implementation of the Delete Product by ID
	 */
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> deleteById(Long id) {

		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			//Delete product by id
			 productDao.deleteById(id);
			
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta Ok", "00", "Producto Eliminado");
							
		} 
		catch(Exception e){
			e.getStackTrace(); //más info del error
			response.setMetadata("Error", "-1", "Error al intentar Eliminar el Producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	
	
	/**
	 * Search all products
	 */
	@Override
	@Transactional(readOnly=true)
	public ResponseEntity<ProductResponseRest> search() {

		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();
		
		try {
			//Search all products
			listAux = (List<Product>)productDao.findAll();
			
			if(listAux.size() > 0) {
			
				listAux.stream().forEach((p)->{
					
					//Descomprimimos la foto, la seteamos a "picture" y la agregamos a la lista
					byte[] imageDescompressed = Util.decompressZLib(p.getPicture());
					p.setPicture(imageDescompressed);
					list.add(p);								
				});
							
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta Ok", "00", "Productos Encontrados");
				
			}else {
				response.setMetadata("Error", "-1", "No exiten productos para mostrar");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
					
		} 
		catch(Exception e){
			e.getStackTrace(); //más info del error
			response.setMetadata("Error", "-1", "Error al buscar Productos");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	
	
	/**
	 * update product
	 */
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> update(Product product, Long categoryId, Long id) {
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
			
			//Search Product to update
			Optional<Product> productSearch =  productDao.findById(id);
			
			if(productSearch != null) {
				
				//UPDATE product
				productSearch.get().setAccount(product.getAccount());
				productSearch.get().setName(product.getName());
				productSearch.get().setPrice(product.getPrice());
				productSearch.get().setCategory(product.getCategory());
				productSearch.get().setPicture(product.getPicture());
				
				Product productToSave = productDao.save(productSearch.get());
				
				if(productToSave != null) {
					list.add(productToSave);
					response.getProductResponse().setProducts(list);
					response.setMetadata("Respuesta OK", "00", "Producto Actualizado");
				}else {
					response.setMetadata("Respuesta OK", "00", "Producto NO Actualizado");
					return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
				}
				
				
			}else {
				response.setMetadata("Error", "-1", "Error al Actualizar al Producto");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
			
		} 
		catch(Exception e){
			e.getStackTrace(); //más info del error
			response.setMetadata("Error", "-1", "Internal Error: Producto NO Actualizado");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);

	};

	
	
	
	
};
