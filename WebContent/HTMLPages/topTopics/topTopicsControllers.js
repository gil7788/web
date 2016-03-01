var app = angular.module('topTopics' , []);

app.controller('topicsController' , function($scope,$http){

  $scope.topTopics ={};
  $scope.elementsIndex = -20;

  $scope.getTopTopics = function(value){
    var temp = $scope.elementsIndex;
     temp += value;
    var date = new Date();
    var request = {
      method: "GET",
      url: "/GameSpot/topic/populartopics",
      params: {
        data : temp
      }
    };
    var successFunction = function(response){
      if((temp == -20)){
          temp -= value;
        }
      else{
        if(Object.keys(response.data).length == 0){//if data is empty
          temp -= value;
        }
        else{
          $scope.topTopics = response.data;
        }
      }
      $scope.elementsIndex = temp;
    };
    var failureFunction = function(response){
      window.alert("Error!");
    };
    if(temp >= 0){
      $http(request).then(successFunction,failureFunction);
    }
  };

  $scope.openTopic = function(topic){
    window.location.replace("/GameSpot/topicsPopularQuestionsView.html");
    localStorage.setItem("topic", topic);
  }
});
