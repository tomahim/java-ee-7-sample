angular.module('geodata').factory('CountryDA', function($q, Restangular) {
	return {
		getAll : function() {
			return Restangular.service('geodata/rest/countries').getList();
		},
		getFeatureGeometry : function(cca2) {
			var  deferred = $q.defer();
			Restangular.one('geodata/data/countries/countries_boundaries.geo.json').get().then(function(featuresCollection) {
				var feature = _.where(featuresCollection.features, function(item) {
					return item.id === cca2;
				});
				if(feature[0]) {
					deferred.resolve(feature[0]);
				} else {
					deferred.reject();
				}
			});
			return deferred.promise;
		}
	};
});