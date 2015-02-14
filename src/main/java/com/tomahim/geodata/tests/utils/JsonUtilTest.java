package com.tomahim.geodata.tests.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;
import javax.json.JsonValue.ValueType;

import org.junit.Ignore;
import org.junit.Test;

import com.tomahim.geodata.utils.jsonUtil.JsonUtil;

public class JsonUtilTest {
	
	private class Person {
		private Integer id;
		
		private String name;
		
		private String privateInfo;
		
		private Boolean isMale;
		
		private List<Person> friends;
		
		public Person() {
			this.friends = new ArrayList<JsonUtilTest.Person>();
		}

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

		public List<Person> getFriends() {
			return friends;
		}

		public void setFriends(List<Person> friends) {
			this.friends = friends;
		}		
		
		public void addFriends(Person friend) {
			this.friends.add(friend);
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
	
	@Test
	public void testJsonContructionDepth1() {
		Person p1 = new Person();
		p1.setId(1);
		p1.setName("Toto");
		p1.setIsMale(true);
		
		Person p2 = new Person();
		p2.setId(2);
		p2.setName("Julia");
		p2.setIsMale(false);
		
		p1.addFriends(p2);
		
		JsonObject jsonObject = JsonUtil.toJson(p1, 1);
		
		assertTrue(jsonObject.containsKey("friends"));		
		assertTrue(jsonObject.get("friends").getValueType().equals(ValueType.ARRAY));
	}
	
}
