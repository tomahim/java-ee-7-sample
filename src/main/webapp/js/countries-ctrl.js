angular.module('geodata').controller('CountriesCtrl', function($scope, CountryDA, CacheSvc) {
	CountryDA.getAll().then(function(countries) {
		$scope.countries = countries;
	});
	
	var map = L.map('map').setView([51.505, -0.09], 2);
	
	$scope.selectCountry = function(country) {
		CountryDA.getFeatureGeometry(country.cca2).then(function(feature) {			
			$scope.feature = feature;
			L.geoJson(feature).addTo(map);
		});
	};
	
	L.tileLayer("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
	    attribution: '',
	    maxZoom: 18
	}).addTo(map)
	
});