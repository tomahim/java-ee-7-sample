package com.tomahim.geodata.rest;

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

import com.tomahim.geodata.entities.City;
import com.tomahim.geodata.entities.Country;
import com.tomahim.geodata.services.CityService;
import com.tomahim.geodata.services.CountryService;
import com.tomahim.geodata.utils.jsonUtil.JsonUtil;

@Path("countries") 
@Produces("application/json")
public class CountryRest {
	
	@Inject
	CountryService countryService;

	@Inject
	CityService cityService;
	
	@GET
	public JsonArray getAll() {
		List<Country> countries = countryService.getAll();
		return JsonUtil.toJsonArray(countries);
	}
	
	@GET
	@Path("/{id}")
	public JsonObject getById(@PathParam("id") Integer id) {
		Country country = countryService.getById(id);
		Map<String, String> selection  = new HashMap<String, String>();
		selection.put("id", "id");
		selection.put("name", "name");
		selection.put("region.id", "region.id");
		selection.put("region.name", "region.name");
		selection.put("cities.name", "cities.name");
		selection.put("cities.id", "cities.id");
		selection.put("regionId", "region.id");
		return JsonUtil.toJson(country, selection);
	}
		
	@GET
	@Path("/{id}/capital")
	public JsonObject getCapitalByCountryId(@PathParam("id") Integer id) {
		City city = cityService.getCapitalByCountryId(id);
		return JsonUtil.toJson(city);
	}
	
	@GET
	@Path("/{id}/cities")
	public JsonArray getCitiesByCountryId(@PathParam("id") Integer id) {
		List<City> cities = cityService.getCitiesByCountryId(id);
		return JsonUtil.toJsonArray(cities);
	}
}
