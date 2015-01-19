angular.module('geodata').controller('CountriesCtrl', function($scope, CountryDA) {
	CountryDA.getAll().then(function(countries) {
		$scope.countries = countries;
	});
});