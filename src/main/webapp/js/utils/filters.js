angular.module('geodata-utils').filter('slice', function() {
  return function(arr, start, end) {
    return (arr || []).slice(start, end);
  };
});