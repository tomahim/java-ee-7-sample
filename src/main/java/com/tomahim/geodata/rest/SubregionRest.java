package com.tomahim.geodata.rest;

import java.util.List;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.tomahim.geodata.entities.Subregion;
import com.tomahim.geodata.services.SubregionService;
import com.tomahim.jsonUtils.api.JsonUtils;

@Path("subregions")
@Produces("application/json")
public class SubregionRest {
	
	@Inject
	SubregionService subregionService;
	
	@GET
	public JsonArray getAll() {
		List<Subregion> subregions = subregionService.getAll();
		return JsonUtils.toJsonArray(subregions);
	}
	
	@GET
	@Path("/{id}")
	public JsonObject getById(@PathParam("id") Integer id) {
		Subregion subregion = subregionService.getById(id);
		return JsonUtils.toJson(subregion);
	}
	
}
