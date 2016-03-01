var app = angular.module('logout' , []);

app.controller('logoutController' , function($scope,$http){

  $scope.logout = function(){

  var request = {
    method: "POST",
    url: "/GameSpot/logout",
    data:{}
  };
  var successFunction = function(response){
  };
  var failureFunction = function(response){
    window.alert("Error");
  };
    window.alert("url: "+response.url);
  $http(request).then(successFunction,failureFunction);
  };
});
