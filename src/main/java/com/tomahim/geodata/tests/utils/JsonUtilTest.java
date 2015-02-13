package com.tomahim.geodata.tests.utils;

import static org.junit.Assert.*;

import javax.json.JsonObject;
import javax.json.JsonValue.ValueType;

import org.junit.Test;

import com.tomahim.geodata.utils.jsonUtil.JsonUtil;

public class JsonUtilTest {
	
	private class Person {
		private Integer id;
		
		private String name;
		
		private String privateInfo;
		
		private Boolean isMale;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Boolean getIsMale() {
			return isMale;
		}

		public void setIsMale(Boolean isMale) {
			this.isMale = isMale;
		}		
	}

	@Test
	public void testFullJavaObjectToJson() {
		Person p = new Person();
		p.setId(1);
		p.setName("Toto");
		p.setIsMale(true);
		
		JsonObject jsonObject = JsonUtil.toJson(p);
		
		/* Key verification */
		assertTrue(jsonObject.containsKey("id"));
		assertTrue(jsonObject.containsKey("name"));
		assertTrue(jsonObject.containsKey("isMale"));
	
		assertFalse(jsonObject.containsKey("privateInfo"));
		assertFalse(jsonObject.containsKey("name2"));
	
		System.out.println(jsonObject.get("id"));
		/* Value verification */
		assertTrue(jsonObject.get("id").equals(p.getId().toString()));
		assertTrue(jsonObject.get("name").equals(p.getName()));
		assertTrue(jsonObject.get("isMale").equals(p.getIsMale()));
		
		assertNotNull(jsonObject);
	}
	
}
