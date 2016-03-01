var app = angular.module('questionForm' , []);

app.controller('questionFormController' , function($scope,$http){
  $scope.sendQuestion = function(){
    var date = new Date();
    var request = {
      method: "PUT",
      url: "/GameSpot/question/add",
      data :{
        text : $scope.text,
        topics : [$scope.topics],
        timestamp : Date.now()
      }
    };
    var successFunction = function(response){

    };
    var failureFunction = function(response){

    };
    
    $http(request).then(successFunction,failureFunction);
  };
});
