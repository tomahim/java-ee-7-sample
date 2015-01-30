package com.tomahim.geodata.dao.interfaces;

import java.util.List;

import com.tomahim.geodata.dao.interfaces.IGenericDao;
import com.tomahim.geodata.entities.City;

public interface ICityDao extends IGenericDao<City, Integer>{

	public City getCapitalByCountryId(Integer countryId);
	
	public List<City> getCitiesByCountryId(Integer countryId);
}
