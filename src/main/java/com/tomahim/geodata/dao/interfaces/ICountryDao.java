package com.tomahim.geodata.dao.interfaces;

import java.util.List;

import com.tomahim.geodata.entities.Country;

public interface ICountryDao {
	public List<Country> findAll();
}
