package com.tomahim.geodata.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.ws.rs.Produces;

import org.hibernate.Session;

import com.tomahim.geodata.dao.interfaces.IRegionDao;
import com.tomahim.geodata.entities.Country;
import com.tomahim.geodata.entities.Region;
import com.tomahim.geodata.utils.HibernateUtil;

@Stateless
@Produces("application/json")
public class RegionDao extends GenericDao<Region, Integer> implements IRegionDao {
	
	Session session;
	
	public RegionDao() {
		session = HibernateUtil.getSessionFactory().openSession();
	}
	
	public List<Region> findAll() {
	      return session.createCriteria(Region.class).list();
	}

}
