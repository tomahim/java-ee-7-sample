package com.tomahim.geodata.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.tomahim.geodata.dao.interfaces.IRegionDao;
import com.tomahim.geodata.entities.Region;

@Stateless
public class RegionService {
	
	@EJB
	protected IRegionDao regionDao;
	
	public List<Region> getAll() {
		return regionDao.findAll();
	}

	public Region getById(Integer id) {
		return regionDao.findById(id);
	}
 }
