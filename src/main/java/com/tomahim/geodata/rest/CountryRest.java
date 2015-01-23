package com.tomahim.geodata.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.tomahim.geodata.dao.interfaces.ICountryDao;
import com.tomahim.geodata.entities.Country;
import com.tomahim.geodata.utils.JsonUtil;

@Path("countries")
@Produces("application/json")
public class CountryRest {
	
	@EJB
	ICountryDao countryDao;	

	@GET
	public JsonArray getCountries() {
		List<Country> list = new ArrayList<Country>();
		list = countryDao.findAll();
		return JsonUtil.createJsonArrayFromJavaList(list);
	}
	
}
