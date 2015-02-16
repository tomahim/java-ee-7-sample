angular.module('geodata', ['ui.router', 'restangular', 'geodata-utils'])
.config(function($stateProvider, $urlRouterProvider) {
	
	  $urlRouterProvider.otherwise("/login");
	  
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
	  })
	  .state('login', {
		  url: "/login",
	      views : {
	    	  "LeftContainer" : {
	    	      templateUrl: "views/login.html",
	    	      controller: "LoginCtrl"
	    	  },
	    	  "RightContainer" : mainMapView
	      }
	  });
});