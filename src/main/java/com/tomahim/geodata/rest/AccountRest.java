package com.tomahim.geodata.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.tomahim.jsonUtils.api.JsonUtils;

@Path("account")
@Produces("application/json")
public class AccountRest {
	
	@GET
	@Path("principal")
	public Response getPrincipal(@Context HttpServletRequest servletRequest) {
		if(servletRequest.getUserPrincipal() == null) {
			return Response.serverError().build();
		} else {
			return Response.ok(JsonUtils.toJson(servletRequest.getUserPrincipal(), "name")).build();			
		}
	}
}
