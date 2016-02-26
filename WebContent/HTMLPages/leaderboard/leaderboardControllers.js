window.alert("I am still alive");

var app = angular.module('leaderboard' , []);

app.controller('leaderboardController' , function($scope){

  $scope.topUsers = {
    users : [
       {
        username :"gil",
        password : "1",
        nickname : "1234567890",
        description : "ftW37u4O08e016zUacWXxF5jwug13pyIAMFQ79nLx5tvMyQ2Za6Q79420XOcThVpS1LkvMpKxZKQsD6Xpqcvcpb8RBFOrAOwteuxYRuQzttOYpyQ1SCtHsQXjCSFA7UBlMoDmwqXvKG6ZDprBv6GAcb7g75gMmHO3ymCjuJN2Bls0orSwiek35pa23XW1VOjLeOXmVuVz0bZKTPlR2uyHt2Acgly1zYrMQHTccmNa8BHcqxvy5vWzVwXfRgaMgK5xw0vPm9Ys2NkgTQFxk4SRmkVniGITU0oi2Fff3VPcSoh",
        photo : "https://pbs.twimg.com/profile_images/378800000822867536/3f5a00acf72df93528b6bb7cd0a4fd0c.jpeg",
        rating: "5",
        latestQuestions: [
          {
            timestamp:132135146,
            questionsText:"ftW37u4O08e016zUacWXxF5jwug13pyIAMFQ79nLx5tvMyQ2Za6Q79420XOcThVpS1LkvMpKxZKQsD6Xpqcvcpb8RBFOrAOwteuxYRuQzttOYpyQ1SCtHsQXjCSFA7UBlMoDmwqXvKG6ZDprBv6GAcb7g75gMmHO3ymCjuJN2Bls0orSwiek35pa23XW1VOjLeOXmVuVz0bZKTPlR2uyHt2Acgly1zYrMQHTccmNa8BHcqxvy5vWzVwXfRgaMgK5xw0vPm9Ys2NkgTQFxk4SRmkVniGITU0oi2Fff3VPcSoh",
            questionsTopics:"sdfasdgaerg",
            questionsRating:"2"
          }
        ],
        latestAnswers: [
          {
            timestamp:132135146,
            questionsText:"asgsdfas",
            questionsTopics:"sdfasdgaerg",
            questionsRating:"2",
            /*Answer's data*/
            answersText:"Very goode answer",
            answersRating:"100"
          }
        ]
      },
      {
       username :"gil",
       password : "1",
       nickname : "123",
       description : "",
       photo : "https://pbs.twimg.com/profile_images/378800000822867536/3f5a00acf72df93528b6bb7cd0a4fd0c.jpeg",
       rating: "5",
       latestQuestions: [
         {
           timestamp:132135146,
           questionsText:"asgsdfas",
           questionsTopics:"sdfasdgaerg",
           questionsRating:"2"
         }
       ],
       latestAnswers: [
         {
           timestamp:132135146,
           questionsText:"asgsdfas",
           questionsTopics:"sdfasdgaerg",
           questionsRating:"2",
           /*Answer's data*/
           answersText:"Very goode answer",
           answersRating:"100"
         }
       ]
     },{
      username :"gil",
      password : "1",
      nickname : "123",
      description : "",
      photo : "https://pbs.twimg.com/profile_images/378800000822867536/3f5a00acf72df93528b6bb7cd0a4fd0c.jpeg",
      rating: "5",
      latestQuestions: [
        {
          timestamp:132135146,
          questionsText:"asgsdfas",
          questionsTopics:"sdfasdgaerg",
          questionsRating:"2"
        }
      ],
      latestAnswers: [
        {
          timestamp:132135146,
          questionsText:"asgsdfas",
          questionsTopics:"sdfasdgaerg",
          questionsRating:"2",
          /*Answer's data*/
          answersText:"Very goode answer",
          answersRating:"100"
        }
      ]
    }
    ]
  }.users;
});
