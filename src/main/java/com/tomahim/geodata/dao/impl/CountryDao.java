package com.tomahim.geodata.dao.impl;

import java.util.List;

import javax.ejb.Stateless;

import com.tomahim.geodata.dao.interfaces.ICountryDao;
import com.tomahim.geodata.entities.Country;

@Stateless
public class CountryDao extends GenericDao<Country, Integer> implements ICountryDao {

}
