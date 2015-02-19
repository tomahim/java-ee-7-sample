angular.module('geodata').controller('LoginCtrl', function($scope, $state, LoginDA) {
	
	
	$scope.login = function(username, password) {
		LoginDA.login(username, password).then(function() {
			$state.go('countries');
		}, function() {
			$scope.loginError = true;
		});
	};
	
});