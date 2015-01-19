package com.tomahim.geodata.entities;

public class Country {

	public Integer id;
	public String name;
	
	public Country(int id, String name) {
		setId(id);
		setName(name);
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
}
