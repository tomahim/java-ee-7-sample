package com.tomahim.geodata.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import com.tomahim.geodata.dao.interfaces.ICountryDao;
import com.tomahim.geodata.entities.Country;

@Stateless
public class CountryService {
	
	@EJB
	protected ICountryDao countryDao;

	@Transactional
	public List<Country> getAll() {
		return countryDao.findAll();
	}

	@Transactional
	public Country getById(Integer id) {
		return countryDao.findById(id);
	}
 }
