angular.module('geodata').controller('CountriesCtrl', function($scope, $q, CountryDA, RegionDA) {
	
	$scope.activeRegion = 'all';
	
	$q.all({
		countries : CountryDA.getAll(),
		regions : RegionDA.getAll()
	}).then(function(result) {
		$scope.countries = result.countries;
		$scope.regions = result.regions;
	});
	
	var map = L.map('map').setView([51.505, -0.09], 2);
	
	L.tileLayer("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
	    attribution: '',
	    maxZoom: 18
	}).addTo(map);

	var layer = L.geoJson();
	$scope.selectCountry = function(country) {
		CountryDA.getFeatureGeometry(country.cca2).then(function(feature) {	
			layer.clearLayers();
			layer.addData(feature);
			layer.addTo(map);
		});
	};
	
	$scope.next = function() {
		$scope.start += 20;
		$scope.end += 20;
	};
	
	$scope.prev = function() {
		$scope.start -= 20;
		$scope.end -= 20;
	};
	
	$scope.$watch('activeRegion', function(active) {
		if(active) {
			$scope.start = 0;
			$scope.end = 20;
		}
	});
	
	$scope.setActive = function(regionId) {
		$scope.activeRegion = regionId;
	};
	
	$scope.isActive = function(regionId) {
		return $scope.activeRegion === regionId;
	};
	
});