package com.tomahim.geodata.api.google;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

@Stateless
public class GoogleGeocodeApi {

	private GeocodeResponse getGeocodeFromAddress(String address) {
		final Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).setLanguage("fr").getGeocoderRequest();
		try {
			return geocoder.geocode(geocoderRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public GeocodeResponse searchByName(String name) {
		return getGeocodeFromAddress(name);
	}
	
	public Map<String, BigDecimal> getLatAndLng(String address) {
		Map<String, BigDecimal> resultMap = new HashMap<String, BigDecimal>();
		List<GeocoderResult> results = getGeocodeFromAddress(address).getResults();
		if(results.size() > 0) {
			resultMap.put("lat", results.get(0).getGeometry().getLocation().getLat());
			resultMap.put("lng", results.get(0).getGeometry().getLocation().getLng());
		}
		return resultMap;		
	}
	
}
