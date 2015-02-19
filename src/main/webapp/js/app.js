angular.module('geodata', ['ui.router', 'restangular', 'geodata-utils'])
.config(function($stateProvider, $urlRouterProvider) {
	
	  $urlRouterProvider.otherwise("/login");
	  
	  var mainMapView = {
		templateUrl : 'views/main-map.html',
		controller : 'MapCtrl'
	  };
	  
	  $stateProvider.state('app', {/*
		 resolve : {
	    	  isLoggedIn: ['$q', '$rootScope', '$state', 'LoginDA', function($q, $rootScope, $state, LoginDA) {
	    		  var defered = $q.defer();
	    		  LoginDA.getPrincipal().then(function(principal) {
	    			  $rootScope.loginError = false;
	    			  $rootScope.user = principal;
	    			  defered.resolve();
	    		  }, function() {
	    			  $rootScope.loginError = true;
	    			  $state.go('login');
	    			  defered.resolve();
	    		  });
	    		  
	    		 return defered.promise;
	    	  }]
	      }
	  */});
	  
	  $stateProvider
	    .state('countries', {
	      url: "/countries",
	      views : {
	    	  "LeftContainer" : {
	    	      templateUrl: "views/countries.html",
	    	      controller: "CountriesCtrl"
	    	  },
	    	  "RightContainer" : mainMapView
	      },
	      resolve : {
	    	  isLoggedIn: ['$q', '$rootScope', '$state', 'LoginDA', function($q, $rootScope, $state, LoginDA) {
	    		  var defered = $q.defer();
	    		  LoginDA.getPrincipal().then(function(principal) {
	    			  $rootScope.loginError = false;
	    			  $rootScope.user = principal;
	    			  defered.resolve();
	    		  }, function() {
	    			  $rootScope.loginError = true;
	    			  $state.go('login');
	    			  defered.resolve();
	    		  });
	    		  
	    		 return defered.promise;
	    	  }]
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
	      },
    	  resolve : {
	    	  isLogged: ['$q', '$rootScope', '$state', 'LoginDA', function($q, $rootScope, $state, LoginDA) {
	    		  var defered = $q.defer();
	    		  LoginDA.getPrincipal().then(function(principal) {
	    			  $rootScope.loginError = false;
	    			  $rootScope.user = principal;
	    			  $state.go('countries');
	    			  defered.resolve();
	    		  }, function() {
	    			  $rootScope.loginError = false;
	    			  defered.resolve();
	    		  });
	    		 return defered.promise;
	    	  }]
	      }
	  });
});