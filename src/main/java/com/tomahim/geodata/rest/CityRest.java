package com.tomahim.geodata.rest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.hibernate.mapping.Map;

import com.tomahim.geodata.utils.JsonUtil;
import com.tomahim.geodata.api.google.GoogleGeocodeApi;
import com.tomahim.geodata.entities.City;
import com.tomahim.geodata.entities.Region;
import com.tomahim.geodata.services.CityService;

@Path("cities")
@Produces("application/json")
public class CityRest {
	
	@EJB
	CityService cityService;
	
	@EJB
	protected GoogleGeocodeApi geocodeApi;
	
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
	
	@GET
	@Path("test")
	public void test() {
		List<City> cities = cityService.getAll();	
		String address = null;
		for(City city : cities) {
			if((city.getLat() == null || city.getLng() == null)) {
				address = city.getName() + " " + city.getCountry().getName();	
				if(address != null) {
					HashMap<String, BigDecimal> mapResults = (HashMap<String, BigDecimal>) geocodeApi.getLatAndLng(address);
					if(mapResults.get("lat") != null) {
						city.setLat(mapResults.get("lat").floatValue());
						city.setLng(mapResults.get("lng").floatValue());
						cityService.update(city);
					}
				}
			}
		}
	}
}
