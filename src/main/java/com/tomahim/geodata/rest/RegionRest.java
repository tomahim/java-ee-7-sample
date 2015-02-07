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

import com.tomahim.geodata.entities.Region;
import com.tomahim.geodata.services.RegionService;
import com.tomahim.geodata.utils.JsonUtil;

@Path("regions")
@Produces("application/json")
public class RegionRest {
	
	@Inject
	RegionService regionService;
	
	@GET
	public JsonArray getAll() {
		List<Region> regions = regionService.getAll();
		return JsonUtil.toJsonArray(regions);
	}
	
	@GET
	@Path("/{id}")
	public JsonObject getById(@PathParam("id") Integer id) {
		Region region = regionService.getById(id);
		//return JsonUtil.createJsonObjectFromJavaObject(region);
		Map<String, String> selection  = new HashMap<String, String>();
		selection.put("id", "id");
		selection.put("name2", "name");
		return JsonUtil.toJson(region, selection);
	}
	
}
