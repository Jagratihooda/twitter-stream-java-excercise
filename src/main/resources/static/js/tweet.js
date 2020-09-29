angular.module('tweetApp', [])
.controller('tweet', function($scope, $http) {
	$scope.warnVar=true;
	$scope.tableVar=true;
	var req = {
			method: 'POST',
			url: 'http://localhost:8081/tweetstreaming/process-tweets',
	}
	$http(req).
	then(function mySuccess(response) {
		$scope.warnVar=false;
		$scope.tableVar=true;
		$scope.tweets = response.data;
	},function myError(response) {
		$scope.warnVar=true;
		$scope.tableVar=false;
	});
});
