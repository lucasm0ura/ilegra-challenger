package com.lucasmoura.ilegra.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasmoura.ilegra.model.Item;
import com.lucasmoura.ilegra.model.Person;
import com.lucasmoura.ilegra.model.Sale;
import com.lucasmoura.ilegra.util.AplicationProperties;

@Service
public class ReadFileServiceImpl {
	
    Logger logger = LoggerFactory.getLogger(ReadFileServiceImpl.class);
	
    @Autowired
    private AplicationProperties applicationProperties;    
    
	private List<Person> personList = new ArrayList<Person>();
	private List<Sale> saleList = new ArrayList<Sale>();
	private List<Item> itemList = new ArrayList<Item>();
	
    public void process(String inputPath) {    	
	       	try {

	   			final Reader content = new FileReader(inputPath);
	   			
	   	        try (BufferedReader reader = new BufferedReader(content)) {
	   	        	String fullLine = "";
	   	            String line = reader.readLine();
	   	            String nextLine = reader.readLine();
	   				while(line != null) {
	   					fullLine += line;
	   					if(fullLine.split("ç").length == 4) {	   						
	   						String[] lineSplited = fullLine.split("ç");
	   						
	   						if(lineSplited[0].equals("001") || lineSplited[0].equals("002")) {
	   							setPersonList(lineSplited);
	   						}else {
	   							setSalesList(lineSplited);
	   						}
	   						fullLine = "";	   					
	   					}
	   					
	   					line = nextLine;
	   					nextLine = reader.readLine();
	   				}
	   				
	   				long countCustomers = personList
	   					  .stream()
	   					  .filter(c -> c.getId() == 2)
	   					  .count();
	   				
	   				long countSalerspeople = personList
		   					  .stream()
		   					  .filter(c -> c.getId() == 1)
		   					  .count();	
	   				
	   		    	Item itemMax = Collections.max(itemList, 
	   		    			Comparator.comparing(item -> item.getTotal()));  
	   		    	
	   				Sale bestSale = saleList
		   					  .stream()
		   					  .filter(sale -> sale.getItems().contains(itemMax))
		   					  .findAny()
		   					  .orElse(null);	   		    	
	   		    	
	   		    	Sale worstSeller = Collections.min(saleList, 
	   		    			Comparator.comparing(item -> item.getTotal())); 
	   				  				

	   		 	 final String fileOut = this.applicationProperties.getHomePath() +
	   	                this.applicationProperties.getDirectoryPathOut() + this.applicationProperties.getFileNameOut();	  
	   		 	 
		   		  try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileOut))) {
		   			  
		   			  writer.write("Amount of Customers are: " + countCustomers+"\n");
		   			  writer.write("Amount of salespeople are: " + countSalerspeople+"\n");
		   			  writer.write("Best Sale ID is: " + bestSale.getIdSale()+"\n");
		   			  writer.write("The Worst Seller is: " + worstSeller.getSalesman().getName()+"\n");
		   			  
		   			  writer.close();
						
					} catch (Exception e) {
						logger.error(e.getLocalizedMessage());
					}
	   	            

	   	        } catch (IOException e) {
	   	            logger.error(e.getLocalizedMessage());
	   	        }	   			
	   			
	   		} catch (IOException e) {
	   			e.printStackTrace();
	   		}			
		
    	 
    }
    
    
    private void setPersonList(String[] line) {
    	
    	Person person = new Person();
    	person.setId(Integer.parseInt(line[0]));
    	person.setName(line[2]);
    	
		if(line[0].equals("001")) {					
			person.setCnpj(line[1]);
			person.setSalary(Float.parseFloat(line[3]));
		}else {
			person.setCpf(line[1]);
			person.setArea(line[3]);
    	}
    	this.personList.add(person);
    }
    
	private void setSalesList(String[] line) {
    	Sale sale = new Sale();
    	
    	sale.setId(Integer.parseInt(line[0]));
    	sale.setIdSale(Integer.parseInt(line[1]));
    	sale.setItems(setItemList(line[2]));
    	
    	Person obj = personList.stream()
			.filter(person -> line[3].equals(person.getName()))
			.findAny()
			.orElse(null);
    	
		sale.setSalesman(obj);
		
		float total = (float) sale.getItems()
				.stream()
				.filter(item -> item.getTotal() != 0)
				.mapToDouble(item -> item.getTotal())
				.sum();		
		
		sale.setTotal(total);

    	this.saleList.add(sale);
    }
    
    
    private List<Item> setItemList(String line){
    	
		  	String stringItems = line.replace("[", "").replace("]", "");
		  	String[] stringSplited = stringItems.split(",");
		  	List<Item> itemsFromSale = new ArrayList<Item>();
		  	
		  	for (String string : stringSplited) {
		  		Item item = new Item();
		  		String[] itemSplited = string.split("-");
		  		item.setId(Integer.parseInt(itemSplited[0]));
		  		item.setQuantity(Integer.parseInt(itemSplited[1]));
		  		item.setPrice(Float.parseFloat(itemSplited[2]));
		  		Float total = Float.parseFloat(itemSplited[2]) * Integer.parseInt(itemSplited[1]); 
		  		item.setTotal(total);
		  		
		  		itemsFromSale.add(item);
		  		this.itemList.add(item);
			}
		  
		  	return itemsFromSale;
    }	

}
