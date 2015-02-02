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

	var layerGeoJson = L.geoJson();
	var capitalMarker = null;
	$scope.selectCountry = function(country) {
		if(capitalMarker != null) {
			map.removeLayer(capitalMarker);
		}

		var capital = _.where(country.cities, function(city) {
			return city.isCapital;
		})[0];
		
		var capitalCoordinates = [capital.lat, capital.lng];
		map.setView(capitalCoordinates, 5);
		
		capitalMarker = L.marker(capitalCoordinates);
		capitalMarker.bindPopup(
				  '<img src="images/flags/png/' + country.cca2 + '-16.png" />'
				+ '<b>' + country.name + '</b>'
				+ '<br>'
				+ capital.name).openPopup();
		capitalMarker.addTo(map);
		
		CountryDA.getFeatureGeometry(country.cca2).then(function(feature) {	
			layerGeoJson.clearLayers();
			layerGeoJson.addData(feature);
			layerGeoJson.addTo(map);
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