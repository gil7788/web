var app=angular.module('questionsView' , []);

app.controller('questionsController' , function($scope,$http){
  $scope.showAllQuestions = 0;
  $scope.toggle = 0;
  $scope.questions = {
    qs: [
    {
      timestamp:12/2/23,
      text:'TUs0ZyLobZtpaVSglrIhcJMkYEdCq91Ii9mdmlXWrNLl7z9jfgepjElDmFuNNNIt6i5PPH1JqiPxl9EjflgjS91reemGwN7dgQRSdtQXc50W2g5qGPNkLeWEIjqDWo5dauXKtky9Z5b9EXgGc81izcSJn1v6ULmU9dKPv2ir8W0bsBBIeMBH7T7oIA1N8we8emFDZXqaTABe1sxSuBeYPvp7XzpDNBQguHlRVhm9Jz50iCOcJw8jr68UwFhdmK0Waf7wCGhb0cq6FovAh00g83693HEpPKH62ot0LE9jZqVa',
      topic:'Test',
      usersNickname:'Gil',
      rating:1189,
      answers: [
        {
          timestamp:2288323623026,
          text:'GVWafX28j9lci7whxTy73Z817spYKk5waeUS9MnpphgrQJpnNJtIjDAEf9wzs1V61Os9bctRfE4xHUlygAd1mLd4CBO4ZMZzEd3wvGf94pKr3Z1ED9bt3kXk8QpU0OfAWXu30EQ0stT3uy9UWQ2RQzS5OIvoQzjXw65MabN5o6MoQbBErN76TcWwOFfqxdqOPUCVvcisvUlniBAcHiZdSsCRbIApi9xHmjLXLmFo2mBb2KX9dljmi95ANWzcT2lfqwis3odsmdh706Qd6OQ8Dwnt269SiFpKQzuAqFq7OlKP',
          usersNickname:'Tesla',
          rating:9001,
        },
        {
          timestamp:2288323823026,
          text:'GVWafX28j11ci7whxTy73Z817spYKk5waeUS9MnpphgrQJpnNJtIjDAEf9wzs1V61Os9bctRfE4xHUlygAd1mLd4CBO4ZMZzEd3wvGf94pKr3Z1ED9bt3kXk8QpU0OfAWXu30EQ0stT3uy9UWQ2RQzS5OIvoQzjXw65MabN5o6MoQbBErN76TcWwOFfqxdqOPUCVvcisvUlniBAcHiZdSsCRbIApi9xHmjLXLmFo2mBb2KX9dljmi95ANWzcT2lfqwis3odsmdh706Qd6OQ8Dwnt269SiFpKQzuAqFq7OlKP',
          usersNickname:'Turing',
          rating:101,
        },
      ]
    },
    {
      timestamp:1288323623006,
      text:'blablabla',
      topic:'Topic',
      usersNickname:'Me',
      rating:5,
      answers: [
        {
          timestamp:2288323623026,
          text:'GVWafX28j9lci7whxTy73Z817spYKk5waeUS9MnpphgrQJpnNJtIjDAEf9wzs1V61Os9bctRfE4xHUlygAd1mLd4CBO4ZMZzEd3wvGf94pKr3Z1ED9bt3kXk8QpU0OfAWXu30EQ0stT3uy9UWQ2RQzS5OIvoQzjXw65MabN5o6MoQbBErN76TcWwOFfqxdqOPUCVvcisvUlniBAcHiZdSsCRbIApi9xHmjLXLmFo2mBb2KX9dljmi95ANWzcT2lfqwis3odsmdh706Qd6OQ8Dwnt269SiFpKQzuAqFq7OlKP',
          usersNickname:'Tesla',
          rating:9001,
        },
        {
          timestamp:2288323823026,
          text:'GVWafX28j11ci7whxTy73Z817spYKk5waeUS9MnpphgrQJpnNJtIjDAEf9wzs1V61Os9bctRfE4xHUlygAd1mLd4CBO4ZMZzEd3wvGf94pKr3Z1ED9bt3kXk8QpU0OfAWXu30EQ0stT3uy9UWQ2RQzS5OIvoQzjXw65MabN5o6MoQbBErN76TcWwOFfqxdqOPUCVvcisvUlniBAcHiZdSsCRbIApi9xHmjLXLmFo2mBb2KX9dljmi95ANWzcT2lfqwis3odsmdh706Qd6OQ8Dwnt269SiFpKQzuAqFq7OlKP',
          usersNickname:'Turing',
          rating:101,
        },
        {
          timestamp:2288323623026,
          text:'GVWafX28j9lci7whxTy73Z817spYKk5waeUS9MnpphgrQJpnNJtIjDAEf9wzs1V61Os9bctRfE4xHUlygAd1mLd4CBO4ZMZzEd3wvGf94pKr3Z1ED9bt3kXk8QpU0OfAWXu30EQ0stT3uy9UWQ2RQzS5OIvoQzjXw65MabN5o6MoQbBErN76TcWwOFfqxdqOPUCVvcisvUlniBAcHiZdSsCRbIApi9xHmjLXLmFo2mBb2KX9dljmi95ANWzcT2lfqwis3odsmdh706Qd6OQ8Dwnt269SiFpKQzuAqFq7OlKP',
          usersNickname:'Tesla',
          rating:9001,
        },
        {
          timestamp:2288323823026,
          text:'GVWafX28j11ci7whxTy73Z817spYKk5waeUS9MnpphgrQJpnNJtIjDAEf9wzs1V61Os9bctRfE4xHUlygAd1mLd4CBO4ZMZzEd3wvGf94pKr3Z1ED9bt3kXk8QpU0OfAWXu30EQ0stT3uy9UWQ2RQzS5OIvoQzjXw65MabN5o6MoQbBErN76TcWwOFfqxdqOPUCVvcisvUlniBAcHiZdSsCRbIApi9xHmjLXLmFo2mBb2KX9dljmi95ANWzcT2lfqwis3odsmdh706Qd6OQ8Dwnt269SiFpKQzuAqFq7OlKP',
          usersNickname:'Turing',
          rating:101,
        }
      ]
    },
    {
      timestamp:3,
      text:'blablabla',
      topic:'Topic',
      usersNickname:'Me',
      rating:5

    }
  ]
}.qs;
  $scope.upVote=function(index){
    $scope.questions[index].rating += 1;
    $scope.update(index);
  };
  $scope.downVote=function(index){
    $scope.questions[index].rating -= 1;
    $scope.update(index);
  };
  $scope.update = function(index){};
  $scope.updateAll = function(){};
  $scope.sendAnswer = function(questionsIndex,answer){
  };
  $scope.validate = function(questionsIndex,answer){
    if(answer && answer.length > 0 && answer.length < 300){
      $scope.sendAnswer(questionsIndex,answer);
    }
  };
  $scope.getQuestions = function(){
    var request = {
      method: "GET",
      url: "/GameSpot/ViewNewQuestions",
      params: {
        orderBy: "timestamp",
        index: 1
      }
    };
    var successFunction = function(response){
      window.alert(response.data);
      window.alert("OK");
    };
    var failureFunction = function(response){
      window.alert(response.data);
      window.alert("Error");
    };
    window.alert("The URL is: '" + request.url + "'");
    $http(request).then(successFunction,failureFunction);
  };
});
