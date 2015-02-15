angular.module('geodata', ['ui.router', 'restangular', 'geodata-utils'])
.config(function($stateProvider, $urlRouterProvider) {
	
	  $urlRouterProvider.otherwise("/countries");
	  
	  var mainMapView = {
		templateUrl : 'views/main-map.html',
		controller : 'MapCtrl'
	  };
	  
	  $stateProvider
	    .state('countries', {
	      url: "/countries",
	      views : {
	    	  "LeftContainer" : {
	    	      templateUrl: "views/countries.html",
	    	      controller: "CountriesCtrl"
	    	  },
	    	  "RightContainer" : mainMapView
	      }
	  });
});