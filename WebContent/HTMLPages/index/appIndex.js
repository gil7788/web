(function() {

  var app = angular.module('indexControllers' , []);

  app.controller('panelController' , function($scope,$http,$location,$log){
    /*Panel Controller:*/
    $scope.panel = 0;
    $scope.authenticationError = 0;

    $scope.setPanel=function(value){
      $scope.panel = value;
    };

    $scope.isSet=function(value){
      return $scope.panel == value;
    };
    /*Login Request:*/
    $scope.sendLoginRequest = function(){
      var request = {
        method: "POST",
        url: "/GameSpot/user/signin",
        data: {
          username: $scope.signInUserName,
          password: $scope.signInPassword
        }
      };
      var successFunction = function(response){
        localStorage.setItem("user", $scope.signInUserName);
        if(response.data === "success"){
          window.location.replace("/GameSpot/main.html");
        }
        else
          $scope.authenticationError = 1;
      };
      var failureFunction = function(){

      };
      $http(request).then(successFunction,failureFunction);
    };

    /*Register Request*/
    $scope.sendRegisterRequest = function(){
      var successFunction = function(response){
        $scope.panel = 0;
      };
      var failureFunction = function(response){
        /*$scope.authenticationError = 1;*/
      };
      var request = {
        method: "PUT",
        url: "/GameSpot/user/signup",
         data:{
           username : $scope.userName,
           password : $scope.password,
           nickname : $scope.nickName,
           description : $scope.description,
           photo : $scope.photo
         }
      };
      $http(request).then(successFunction,failureFunction);
    };
  });
})();
