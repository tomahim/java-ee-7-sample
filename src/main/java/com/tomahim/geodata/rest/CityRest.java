package com.tomahim.geodata.rest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.code.geocoder.model.GeocodeResponse;
import com.tomahim.geodata.api.google.GoogleGeocodeApi;
import com.tomahim.geodata.entities.City;
import com.tomahim.geodata.services.CityService;
import com.tomahim.jsonUtils.api.JsonUtils;

@Path("cities")
@Produces("application/json")
public class CityRest {
	
	@Inject
	CityService cityService;
	
	@Inject
	protected GoogleGeocodeApi geocodeApi;
	
	@GET
	public JsonArray getAll() {
		List<City> cities = cityService.getAll();
		return JsonUtils.toJsonArray(cities);
	}
	
	@GET
	@Path("/{id}")
	public JsonObject getById(@PathParam("id") Integer id) {
		City city = cityService.getById(id);
		return JsonUtils.toJson(city);
	}
	
	@GET
	@Path("/search/{name}")
	public JsonArray searchCityByName(@PathParam("name") String name) {
		GeocodeResponse gr = geocodeApi.searchByName(name);
		Map<String, String> map = new HashMap<String, String>();
		return JsonUtils.toJsonArray(gr.getResults(), 1);
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
