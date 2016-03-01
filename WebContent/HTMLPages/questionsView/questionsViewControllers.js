var app=angular.module('questionsView' , []);

app.controller('questionsController' , function($scope,$http,$timeout){
  $scope.showAllQuestions = 0;
  $scope.toggle = 0;
  $scope.questions = {};
  $scope.votes;

  $scope.elementsIndex = -20;
  $scope.checkUser = function(username,index){
    $timeout(showTime, 3000);
    if(username == localStorage.user){
      function showTime() {
        $scope.questions[index].showError =  true;
      }
      $scope.questions[index].showError =  false;
    }
    $scope.questions[index].showError =  false;
  }

  $scope.getVotes = function(){
    $scope.username = localStorage.user;

    var request = {
      method: "GET",
      url: "/GameSpot/question/votes/" + $scope.username,
      params: {
        data : {
          questions : $scope.questions
        }
      }
    };
    var successFunction = function(response){
      var votes = response.data;
      var questions = $scope.questions;

      for(var i = 0; i<votes.length; i++){
        for(var j = 0; j<questions.length; j++){
          if(votes[i].questionId == questions[j].id){
            questions[i].value = votes[j].value;
            questions[i]
          }
        }
      }
    };
    var failureFunction = function(response){
      window.alert("Error!");
    };
    $http(request).then(successFunction,failureFunction);
  }

  $scope.getQuestions = function(value){
    var temp = $scope.elementsIndex;
     temp += value;
    var date = new Date();
    var request = {
      method: "GET",
      url: "/GameSpot/question/newquestions",
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
          $scope.questions = response.data;
          $scope.getVotes();
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
  $scope.getQuestions(20);
  $scope.arrayToString = function(string){
      return string.join(", ");
  };

  $scope.sendAnswer = function(description,id){

    var request = {
      method: "PUT",
      url: "/GameSpot/answer/add",
      data:{
        questionId: id,
        text: description,
        timestamp: Date.now()
      }
    };
    var successFunction = function(response){
      $scope.getQuestions();
    };
    var failureFunction = function(response){
      window.alert("Error");
    };

    $http(request).then(successFunction,failureFunction);
  };

  $scope.upVote=function(index,id){
    if($scope.questions[index].value == 1){
        $scope.vote(id,0);
        $scope.questions[index].value = 0;
        $scope.questions[index].voteCount -= 1;
    }
    else{
        $scope.vote(id,1);
        if($scope.questions[index].value == -1)
          $scope.questions[index].voteCount += 1;
        $scope.questions[index].voteCount += 1;
        $scope.questions[index].value = 1;
    }
  };
  $scope.downVote=function(index,id){
    if($scope.questions[index].value == -1){
      $scope.vote(id,0);
      $scope.questions[index].value = 0;
      $scope.questions[index].voteCount += 1;
    }
    else{
      $scope.vote(id,-1);
      if($scope.questions[index].value == 1)
        $scope.questions[index].voteCount -= 1;
      $scope.questions[index].voteCount -= 1;
      $scope.questions[index].value = -1;
    }
  };

  $scope.vote = function(id , value, index){
    var request = {
      method: "PUT",
      url: "/GameSpot/question/vote",
      data : {
        value : value,
        id : id
      }
    };
    var successFunction = function(response){
      $scope.getQuestion(id,index);
      //response.data;
    };
    var failureFunction = function(response){
      window.alert("Error");
    };

    $http(request).then(successFunction,failureFunction);
  };
  $scope.getQuestion = function(id, index){
    var request = {
      method: "GET",
      url: "/GameSpot/question/"+id,
      data :{}
    };
    var successFunction = function(response){
      $scope.questions[index] = response.data;
    };
    var failureFunction = function(response){
      window.alert("Error");
    };

    $http(request).then(successFunction,failureFunction);
  };
});
