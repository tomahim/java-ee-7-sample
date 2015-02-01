angular.module('geodata').factory('RegionDA', function($q, Restangular) {
	return {
		getAll : function() {
			return Restangular.service('geodata/rest/regions').getList();
		}
	};
});