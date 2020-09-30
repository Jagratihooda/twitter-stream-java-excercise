angular.module('tweetApp', [])
	.controller('tweet', function($scope, $http) {
		$scope.loadingVar=true;
		$scope.warnVar=false;
		$scope.tableVar=false;
		$scope.preLoadVal=false;	var req = {
			method: 'GET',
			url: 'http://localhost:8081/tweetstreaming/process-tweets',
	}
	$http(req).
	then(function mySuccess(response) {
		$scope.preLoadVal=true;
		$scope.loadingVar=false;
		$scope.warnVar=false;
		$scope.tableVar=true;
		$scope.tweets = response.data;
	},function myError(response) {
		$scope.preLoadVal=true;
		$scope.loadingVar=false;
		$scope.warnVar=true;
		$scope.tableVar=false;
	});
});
