package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;

@Service
public class CategoryServiceImpl implements ICategoryService{

	@Autowired
	private ICategoryDao categoryDao;
	
	/**
	 * implements service get all the categories
	 */
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> search() {
		
		CategoryResponseRest response = new CategoryResponseRest();
		
		try {
			List<Category> category = (List<Category>) categoryDao.findAll();
		    response.getCategoryResponse().setCategory(category);
		    response.setMetadata("Respuesta OK", "00", "Respuesta Exitosa");
		}
		catch(Exception e) {
			 response.setMetadata("Error", "-1", "Error al consultar");
			 e.getStackTrace();
			 return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
		
	}

	
	
	/**
	 * implements service GET Categories By Id
	 */
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> searchById(Long id) {
		
    CategoryResponseRest response = new CategoryResponseRest();
    List<Category> list = new ArrayList<>();
		
		try {		
			 Optional<Category> category = categoryDao.findById(id);
			 
			 if(category.isPresent()) {
				 list.add(category.get());
				 response.getCategoryResponse().setCategory(list);
				 response.setMetadata("Respuesta OK", "00", "Categoría Encontrada");
			 }else {
				 response.setMetadata("Error al Obtener Categoría", "-1", "Categoria No encontrada");
				 return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
			 }
		}
		catch(Exception e) {
			 response.setMetadata("Error", "-1", "Error al consultar");
			 e.getStackTrace();
			 return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
		
	}
	
	/**
	 * implements service for SAVE categories
	 */
	
	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> save(Category category) {
		
    CategoryResponseRest response = new CategoryResponseRest();
    List<Category> list = new ArrayList<>();
		
		try {		
			Category categorySaved = categoryDao.save(category);
			
			if(categorySaved != null) {
				list.add(categorySaved);
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("Respuesta OK", "00", "Categoría Guardada");
			}
			else {
				response.setMetadata("Error al Guardar", "00", "No se registro la categoria");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
		}
		catch(Exception e) {
			 response.setMetadata("Error", "-1", "Error al consultar");
			 e.getStackTrace();
			 return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}
	
	
	
	/**
	 * UPDATE categories
	 */
	
	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> update(Category category, Long id) {
		
    CategoryResponseRest response = new CategoryResponseRest();
    List<Category> list = new ArrayList<>();
		
		try {		
			Optional<Category> categorySearch = categoryDao.findById(id);
			
			if(categorySearch.isPresent()) {
				categorySearch.get().setName(category.getName());
				categorySearch.get().setDescription(category.getDescription());
				
				Category categoryToUpdate = categoryDao.save(categorySearch.get());
				
				if(categoryToUpdate != null) {
					list.add(categoryToUpdate);
					response.getCategoryResponse().setCategory(list);
					response.setMetadata("Respuesta OK", "00", "Categoria Actualizada");
				}
			}
			else {
				response.setMetadata("Error al Actualizar", "00", "Categoria No encontrada");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
			}
		}
		catch(Exception e) {
			 response.setMetadata("Error", "-1", "Error al consultar");
			 e.getStackTrace();
			 return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}



	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> deleteById(Long id) {
	
		 CategoryResponseRest response = new CategoryResponseRest();
		   				
				try {		
					categoryDao.deleteById(id);
					response.setMetadata("Respuesta OK", "00", "Registro Eliminado");
				}
				catch(Exception e) {
					 response.setMetadata("Error", "-1", "Error al consultar");
					 e.getStackTrace();
					 return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
			}
	
	
	
}
	

