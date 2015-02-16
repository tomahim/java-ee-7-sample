angular.module('geodata').controller('LoginCtrl', function($scope, LoginDA) {
	
	
	$scope.login = function(username, password) {
		LoginDA.login(username, password);
	};
	
	LoginDA.login();
	
});