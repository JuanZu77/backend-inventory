package com.company.inventory.util;

import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;


import com.company.inventory.model.Category;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class CategoryExcelExporter {
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Category> category;
	
	public CategoryExcelExporter(List<Category> categories) {
		
		this.category = categories;
		workbook = new XSSFWorkbook();
		
	}
	
	//Metodo cabecera del excel
	private void writeHeaderLine() {
		sheet = workbook.createSheet("Resultado");
		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16); 
		style.setFont(font);
		
		//fila con sus cabeceras
		createCell(row, 0, "ID", style);
		createCell(row, 1, "Nombre", style);
		createCell(row, 2, "Descripción", style);
	}
	
	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		 sheet.autoSizeColumn(columnCount);
		 Cell cell = row.createCell(columnCount);
		 
		 if(value instanceof Integer) {
			 cell.setCellValue((Integer) value);
		 }else if(value instanceof Boolean) {
			 cell.setCellValue((Boolean) value);
		 }else {
			 cell.setCellValue((String) value);
		 }
		 
		 cell.setCellStyle(style);	 
	}
	
	
	private void writeDataLine() {
		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14); 
		style.setFont(font);
		
		for (Category result : category) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, String.valueOf(result.getId()), style);//ID Category
			createCell(row, columnCount++, String.valueOf(result.getName()), style);//Nombre Category
			createCell(row, columnCount++, String.valueOf(result.getDescription()), style);//Descripcion Category
		}
	}
	
	
	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLine();
		ServletOutputStream servletOutput = response.getOutputStream();
		workbook.write(servletOutput);
		workbook.close();
		servletOutput.close();
	}

}
