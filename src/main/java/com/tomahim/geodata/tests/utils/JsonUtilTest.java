package com.tomahim.geodata.tests.utils;

import static org.junit.Assert.*;

import javax.json.JsonObject;

import org.junit.Test;

import com.tomahim.geodata.utils.jsonUtil.JsonUtil;

public class JsonUtilTest {
	
	private class Person {
		private Integer id;
		
		private String name;

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
	}

	@Test
	public void testFullJavaObjectToJson() {
		Person p = new Person();
		p.setId(1);
		p.setName("Toto");
		JsonObject jsonObject = JsonUtil.toJson(p);
		
		assertNotNull(jsonObject);
	}
	
}
