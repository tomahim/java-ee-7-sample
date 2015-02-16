angular.module('geodata').factory('LoginDA', function(Restangular) {
	return {
		login : function(username, password) {
			return Restangular.service('geodata/LoginServlet').post({
				username : 'tom',
				password : 'azerty'
			});
		}
	};
});