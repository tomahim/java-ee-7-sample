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
	
	public List<City> findAll() {
    	TypedQuery<City> query = entityManager.createNamedQuery("City.findAll", entityClass);
		List<City> results = query.getResultList();
	    return results;
	}

	@Override
	public City getCapitalByCountryId(Integer countryId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();		 
		CriteriaQuery<City> q = cb.createQuery(City.class);
		Root<City> city = q.from(City.class);
		
		q.select(city).where(
			cb.and(
				cb.equal(city.get("country").get("id"), countryId),
				cb.equal(city.get("isCapital"), 1)
			)
		);
		
		TypedQuery<City> query = entityManager.createQuery(q);
		return query.getSingleResult();
	}

	@Override
	public List<City> getCitiesByCountryId(Integer countryId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();		 
		CriteriaQuery<City> q = cb.createQuery(City.class);
		Root<City> city = q.from(City.class);
		
		q.select(city).where(
			cb.equal(city.get("country").get("id"), countryId)
		);
		
		TypedQuery<City> query = entityManager.createQuery(q);
		return query.getResultList();
	}

}
