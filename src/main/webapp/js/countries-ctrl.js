angular.module('geodata').controller('CountriesCtrl', function($scope, CountryDA, CacheSvc) {
	CountryDA.getAll().then(function(countries) {
		$scope.countries = countries;
	});
	
	$scope.selectCountry = function(country) {
		CountryDA.getFeatureGeometry(country.cca2).then(function(feature) {			
			$scope.feature = feature;
		});
	};
});