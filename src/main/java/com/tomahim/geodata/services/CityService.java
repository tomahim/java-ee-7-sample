package com.tomahim.geodata.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.tomahim.geodata.dao.interfaces.ICityDao;
import com.tomahim.geodata.entities.City;

@Stateless
public class CityService {
	
	@Inject
	protected ICityDao cityDao;
	
	public List<City> getAll() {
		return cityDao.findAll();
	}

	public City getById(Integer id) {
		return cityDao.findById(id);
	}

	public City getCapitalByCountryId(Integer countryId) {
		return cityDao.getCapitalByCountryId(countryId);
	}
	
	public List<City> getCitiesByCountryId(Integer countryId) {
		return cityDao.getCitiesByCountryId(countryId);
	}
	
	public void update(City city) {
		cityDao.update(city);
	}
 }
