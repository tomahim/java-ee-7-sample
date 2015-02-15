angular.module('geodata').controller('MapCtrl', function(MapSvc) {
	console.log('mapCtrl')
	var map = L.map('map').setView([51.505, -0.09], 2);
	
	MapSvc.initMap(map);
	
	L.tileLayer("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
	    attribution: '',
	    maxZoom: 18
	}).addTo(map);

}).service('MapSvc', function() {
	
	this.map = null;
		
	this.initMap = function(map) {
		this.map = map;
	}	
	
});