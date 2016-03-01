var app=angular.module('questionsView' , []);

app.controller('questionsController' , function($scope,$http){
  $scope.showAllQuestions = 0;
  $scope.toggle = 0;
  $scope.questions = {};
  $scope.answers ={};
  $scope.votes;

  $scope.elementsIndex = -20;

  $scope.getQuestions = function(value){
    var temp = $scope.elementsIndex;
     temp += value;
    var date = new Date();
    var request = {
      method: "GET",
      url: "/GameSpot/question/existingquestions",
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

  $scope.arrayToString = function(string){
      return string.join(", ");
  };
  $scope.sendAnswer = function(description,id){
    alert("In sendAnswer,Text:" + description);
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

  $scope.getAnswersToQuestion = function(i){
    var questionId = $scope.questions[i].id;
    var request = {
      method: "GET",
      url: "/GameSpot/question/"+questionId+"/answers",
      params:{}
    };
    var successFunction = function(response){
      //alert(response.data.text);
      $scope.questions[i].answers = response.data;
    };
    var failureFunction = function(response){
      window.alert("Error");
    };
    //alert("in getAnswersToQuestion method");
    $http(request).then(successFunction,failureFunction);
  };

  $scope.getAnswers = function(){
    for(i=0;i<$scope.questions.length;i++){
      $scope.getAnswersToQuestion(i);
    }
  }

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

  $scope.vote = function(id , value){
    var request = {
      method: "PUT",
      url: "/GameSpot/question/vote",
      data : {
        value : value,
        id : id
      }
    };
    var successFunction = function(response){

      response.data;
    };
    var failureFunction = function(response){
      window.alert("Error");
    };

    $http(request).then(successFunction,failureFunction);
  };
  $scope.answerUpVote = function(questionIndex,answerIndex,answerId){
    if($scope.questions[questionIndex].answers[answerIndex].value == 1){
      $scope.answerVote(answerId,0);
      $scope.questions[questionIndex].answers[answerIndex].value = 0;
      $scope.questions[questionIndex].answers[answerIndex].rating -= 1;
    }
    else{
      $scope.answerVote(answerId,1);
      if($scope.questions[questionIndex].answers[answerIndex].value == -1)
        $scope.questions[questionIndex].answers[answerIndex].rating += 1;
      $scope.questions[questionIndex].answers[answerIndex].rating += 1;
      $scope.questions[questionIndex].answers[answerIndex].value = 1;
    }
  }
  $scope.answerDownVote=function(questionIndex,answerIndex,answerId){
    if($scope.questions[questionIndex].answers[answerIndex].value == -1){
      $scope.answerVote(answerId,0);
      $scope.questions[questionIndex].answers[answerIndex].value = 0;
      $scope.questions[questionIndex].answers[answerIndex].rating += 1;
    }
    else{
      $scope.answerVote(answerId,-1);
      if($scope.questions[questionIndex].answers[answerIndex].value == 1)
        $scope.questions[questionIndex].answers[answerIndex].rating -= 1;
      $scope.questions[questionIndex].answers[answerIndex].rating -= 1;
      $scope.questions[questionIndex].answers[answerIndex].value = -1;
    }
  };


  $scope.answerVote = function(id , value){
    var request = {
      method: "PUT",
      url: "/GameSpot/answer/vote",
      data : {
        value : value,
        id : id
      }
    };
    var successFunction = function(response){

    };
    var failureFunction = function(response){
      window.alert("Error");
    };
    window.alert("Url: " + request.url);
    $http(request).then(successFunction,failureFunction);
  };
});
