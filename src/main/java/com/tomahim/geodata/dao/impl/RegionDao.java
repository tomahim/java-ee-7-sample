package com.tomahim.geodata.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.ws.rs.Produces;

import com.tomahim.geodata.dao.interfaces.IRegionDao;
import com.tomahim.geodata.entities.Region;

@Stateless
@Produces("application/json")
public class RegionDao extends GenericDao<Region, Integer> implements IRegionDao {
	
	public List<Region> findAll() {
    	TypedQuery<Region> query = entityManager.createNamedQuery("Region.findAll", entityClass);
		List<Region> results = query.getResultList();
	    return results;
	}

}