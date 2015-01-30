package com.tomahim.geodata.dao.impl;

import javax.ejb.Stateless;

import com.tomahim.geodata.dao.interfaces.ISubregionDao;
import com.tomahim.geodata.entities.Subregion;

@Stateless
public class SubregionDao extends GenericDao<Subregion, Integer> implements ISubregionDao {

}
