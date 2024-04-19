package com.company.inventory.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table (name="product")
public class Product implements Serializable{

	private static final long serialVersionUID = -7461389651533509262L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private float price;
	private int account;
	
	// Referenciar Entidad Categoria
	// Establecer la Relacion entre Categoria y Producto
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
	private Category category;
	
	//foto en base 64bytes
	@Lob
	@Column(name="picture", columnDefinition = "longblob")
	private byte[] picture;
	
}
