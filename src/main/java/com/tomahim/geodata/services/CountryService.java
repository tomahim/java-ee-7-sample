package com.tomahim.geodata.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.tomahim.geodata.dao.interfaces.ICountryDao;
import com.tomahim.geodata.entities.Country;

@Stateless
public class CountryService {
	
	@Inject
	protected ICountryDao countryDao;

	public List<Country> getAll() {
		return countryDao.findAll();
	}

	public Country getById(Integer id) {
		return countryDao.findById(id);
	}
 }
