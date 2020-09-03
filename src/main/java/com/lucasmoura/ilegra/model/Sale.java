package com.lucasmoura.ilegra.model;

import java.util.List;

public class Sale {
	
	private int id;
	
	private int idSale;
	
	private List<Item> items;
	
	private Person salesman;
	
	private float total;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdSale() {
		return idSale;
	}

	public void setIdSale(int idSale) {
		this.idSale = idSale;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Person getSalesman() {
		return salesman;
	}

	public void setSalesman(Person salesman) {
		this.salesman = salesman;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}
	
	

}
