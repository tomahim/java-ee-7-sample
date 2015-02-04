package com.tomahim.geodata.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the COUNTRY database table.
 * 
 */
@Entity
@Table(name="COUNTRY")
@NamedQuery(name="Country.findAll", query="SELECT c FROM Country c")
public class Country implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String cca2;

	private String cca3;

	private String name;

	//bi-directional many-to-one association to City
	@OneToMany(mappedBy="country")
	private List<City> cities;

	//bi-directional many-to-one association to Region
	@ManyToOne
	private Region region;

	//bi-directional many-to-one association to Subregion
	@ManyToOne
	private Subregion subregion;

	public Country() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCca2() {
		return this.cca2;
	}

	public void setCca2(String cca2) {
		this.cca2 = cca2;
	}

	public String getCca3() {
		return this.cca3;
	}

	public void setCca3(String cca3) {
		this.cca3 = cca3;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<City> getCities() {
		return this.cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public City addCity(City city) {
		getCities().add(city);
		city.setCountry(this);

		return city;
	}

	public City removeCity(City city) {
		getCities().remove(city);
		city.setCountry(null);

		return city;
	}

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Subregion getSubregion() {
		return this.subregion;
	}

	public void setSubregion(Subregion subregion) {
		this.subregion = subregion;
	}

}