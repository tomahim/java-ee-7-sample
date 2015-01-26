angular.module('geodata', ['ui.router', 'restangular', 'geodata-utils'])
.config(function($stateProvider, $urlRouterProvider) {
	
	  $urlRouterProvider.otherwise("/countries");
	  
	  $stateProvider
	    .state('countries', {
	      url: "/countries",
	      templateUrl: "views/countries.html",
	      controller: "CountriesCtrl"
	  });
});