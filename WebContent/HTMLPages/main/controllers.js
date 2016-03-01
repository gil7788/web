var app = angular.module('mainUX' , []);


app.controller('TabController' , function($scope){
  $scope.primaryTab = 0;
  $scope.secondaryTab = 0;
  $scope.finalTab = $scope.primaryTab + $scope.secondaryTab;
  $scope.setPrimaryTab = function(finalValue){
    $scope.primaryTab = finalValue;
    $scope.secondaryTab = 0;
    $scope.finalTab = $scope.primaryTab;
  };

  $scope.setSecondaryTab = function(finalValue){
    $scope.secondaryTab = finalValue;
    $scope.finalTab = $scope.primaryTab + $scope.secondaryTab;
  };
  $scope.isSet = function(v){
    for(i =0;i<v.length;i++){
      if(v[i] == $scope.finalTab){
        return true;
      }
    }
    return false;
  };
});


app.controller('UserController' , function($scope,$http){
  $scope.username = localStorage.user;
  $scope.user;
  $scope.getUser = function(){
  var request = {
    method: "GET",
    url: "/GameSpot/user/"+$scope.username+"/data",
    data:{}
  };
  var successFunction = function(response){
    $scope.user = response.data;
  };
  var failureFunction = function(response){
    window.alert("Error");
  };
  $http(request).then(successFunction,failureFunction);
  };
  $scope.getUser();
});
