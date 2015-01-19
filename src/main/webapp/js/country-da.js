angular.module('geodata').factory('CountryDA', function(Restangular) {
	return {
		getAll : function() {
			return Restangular.service('geodata/rest/countries').getList();
		}
	};
});