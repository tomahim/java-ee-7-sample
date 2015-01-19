package com.tomahim.geodata.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("countries")
public class CountryRest {

	@GET
	public Response getCountries() {
		return Response.ok("countries").build();
	}
	
}
