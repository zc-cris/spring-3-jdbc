package com.zc.cris.SpringJDBC;


public class Order {
	
	private Integer id;
	private String name;
	private  Customer customer;
	
	private Integer customerId;
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + ", customer=" + customer + "]";
	}
	
	
	
	
	
}
