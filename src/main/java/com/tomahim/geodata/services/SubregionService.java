package com.tomahim.geodata.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.tomahim.geodata.dao.interfaces.ISubregionDao;
import com.tomahim.geodata.entities.Subregion;

@Stateless
public class SubregionService {
	
	@Inject
	protected ISubregionDao subregionDao;
	
	public List<Subregion> getAll() {
		return subregionDao.findAll();
	}

	public Subregion getById(Integer id) {
		return subregionDao.findById(id);
	}
 }
