(function() {

  var app = angular.module('indexControllers' , []);

  app.controller('panelController' , function($scope,$http,$location,$log){
    /*Panel Controller:*/
    $scope.panel = 0;

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
        url: "/GameSpot/SignIn",
        data: " { userName: '" + $scope.signInUserName + "', password: '" + $scope.signInPassword + "' }",
      };
      $http(request).success(function () {
        $log.log("success");
        window.location.replace("/GameSpot/main.html");
      });
    };
    /*Register Request*/
    $scope.sendRegisterRequest = function(){
      var successFunction = function(response){
        //C:\\Users\\student\\Desktop\\Univesity\\SemesterA2016\\Web\\index.html
        $scope.panel = 0;
      };
      var failureFunction = function(response){
        /*$scope.authenticationError = 1;*/
      };
      var request = {
        method: "POST",
        url: "/GameSpot/SignUp",
        data: " { username: '" + $scope.userName + "', password: '" + $scope.password +
         "', nickname:'" + $scope.nickName  + "' , description: '" + $scope.description + "' , photo:'" + $scope.photo + "' }",
      };
      $http(request).then(successFunction,failureFunction);
    };
  });
})();
