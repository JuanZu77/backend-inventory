package com.company.inventory.response;

import java.util.List;
import com.company.inventory.model.Product;
import lombok.Data;

@Data
public class ProductResponse {
	
	private List<Product> products;

}
