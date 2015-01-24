package com.tomahim.geodata.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.tomahim.geodata.dao.interfaces.ISubregionDao;
import com.tomahim.geodata.entities.Subregion;

@Stateless
public class SubregionDao extends GenericDao<Subregion, Integer> implements ISubregionDao {
	
	public List<Subregion> findAll() {
    	TypedQuery<Subregion> query = entityManager.createNamedQuery("Subregion.findAll", entityClass);
		List<Subregion> results = query.getResultList();
	    return results;
	}

}
