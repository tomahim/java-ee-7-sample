package com.tomahim.geodata.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import com.tomahim.geodata.dao.interfaces.ICityDao;
import com.tomahim.geodata.entities.City;

@Stateless
public class CityDao extends GenericDao<City, Integer> implements ICityDao {

	@Override
	public City getCapitalByCountryId(Integer countryId) {
		return null;
	}

	@Override
	public List<City> getCitiesByCountryId(Integer countryId) {
		return null;
	}

}
