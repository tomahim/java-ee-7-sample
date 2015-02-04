package com.tomahim.geodata.dao.impl;

import javax.ejb.Stateless;

import com.tomahim.geodata.dao.interfaces.IRegionDao;
import com.tomahim.geodata.entities.Region;

@Stateless
public class RegionDao extends GenericDao<Region, Integer> implements IRegionDao {
	
}
