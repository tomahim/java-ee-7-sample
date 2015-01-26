angular.module('geodata-utils', []).service('CacheSvc', function() {
	this.cachedData = {};
	
	//array is optional
	this.createCache = function(name, array) {
		this.cachedData[name] = array ? array : [];
	};
	
	this.put = function(cacheName, key, value) {
		if(!this.cachedData[cacheName]) {
			this.cachedData[cacheName] = [];
		}
		this.cachedData[cacheName].push({key : key, value : value});
	};
	
	this.get = function(cacheName, key) {
		var search = _.where(this.cachedData[cacheName], function(item) {
			return item.key === key;
		});
		if(search[0]) {
			return search;
		}
		return null;
	};
});