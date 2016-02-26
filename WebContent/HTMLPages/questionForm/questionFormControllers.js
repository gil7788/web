var app = angular.module('questionForm' , []);

app.controller('questionFormController' , function($scope,$http){
  $scope.sendQuestion = function(){
    var date = new Date();
    var request = {
      method: "POST",
      url: "http://localhost:8080/GameSpot/AddQuestion",
      data: " { text: '" + $scope.text + "', topics: '" + $scope.topics + "', timestamp:'" + Date.now() + "' }"
    };
    var successFunction = function(response){
      window.alert("Recived Data:" + response.data);
    };
    var failureFunction = function(response){
      window.alert("Recived Data:" + response.data);
    };
    window.alert(request.data);
    $http(request).then(successFunction,failureFunction);
  };
});
