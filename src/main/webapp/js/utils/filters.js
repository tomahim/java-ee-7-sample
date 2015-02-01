angular.module('geodata-utils').filter('slice', function() {
  return function(arr, start, end) {
    return (arr || []).slice(start, end);
  };
}).filter('regionFilter', function() {
  return function(arr, regionId) {
	  if(regionId === 'all') {
		  return arr;
	  }
	  return _.where(arr, function(country) {
		  return regionId === country.region.id;
	  });	  
  };
});