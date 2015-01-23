package com.tomahim.geodata.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the SUBREGION database table.
 * 
 */
@Entity
@Table(name="SUBREGION")
@NamedQuery(name="Subregion.findAll", query="SELECT s FROM Subregion s")
public class Subregion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String name;

	//bi-directional many-to-one association to Country
	@OneToMany(mappedBy="subregion")
	private List<Country> countries;

	public Subregion() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Country> getCountries() {
		return this.countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public Country addCountry(Country country) {
		getCountries().add(country);
		country.setSubregion(this);

		return country;
	}

	public Country removeCountry(Country country) {
		getCountries().remove(country);
		country.setSubregion(null);

		return country;
	}

}