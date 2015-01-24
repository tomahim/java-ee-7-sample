package com.tomahim.geodata.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.tomahim.geodata.utils.JsonUtil;
import com.tomahim.geodata.entities.City;
import com.tomahim.geodata.entities.Region;
import com.tomahim.geodata.services.CityService;

@Path("cities")
public class CityRest {
	
	@EJB
	CityService cityService;
	
	@GET
	public JsonArray getAll() {
		List<City> cities = cityService.getAll();
		return JsonUtil.createJsonArrayFromJavaList(cities);
	}
	
	@GET
	@Path("/{id}")
	public JsonObject getById(@PathParam("id") Integer id) {
		City city = cityService.getById(id);
		return JsonUtil.createJsonObjectFromJavaObject(city);
	}
	
}
