var app = angular.module('leaderboard' , []);

app.controller('leaderboardController' , function($scope,$http){

  $scope.topUsers = {};

  $scope.getLeaderboards = function(){
    var date = new Date();
    var request = {
      method: "GET",
      url: "/GameSpot/user/leaderboard",
      params: {}
    };
    var successFunction = function(response){
      $scope.topUsers = response.data;
    };
    var failureFunction = function(response){
      window.alert("Error!");
    };
    $http(request).then(successFunction,failureFunction);
  };

});
