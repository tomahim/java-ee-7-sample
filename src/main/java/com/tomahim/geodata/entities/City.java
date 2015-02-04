package com.tomahim.geodata.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the CITY database table.
 * 
 */
@Entity
@Table(name="CITY")
@NamedQuery(name="City.findAll", query="SELECT c FROM City c")
public class City implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="is_capital")
	private byte isCapital;

	private Float lat;

	private Float lng;

	private String name;

	//bi-directional many-to-one association to Country
	@ManyToOne
	private Country country;

	public City() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getIsCapital() {
		return this.isCapital;
	}

	public void setIsCapital(byte isCapital) {
		this.isCapital = isCapital;
	}

	public Float getLat() {
		return this.lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLng() {
		return this.lng;
	}

	public void setLng(Float lng) {
		this.lng = lng;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

}