angular.module('geodata').controller('LoginCtrl', function($scope, LoginDA) {
	
	console.log('ctrl')
	LoginDA.login();
	
});