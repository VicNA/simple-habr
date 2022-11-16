angular.module('HabrApp').controller('moderatorController', function ($scope, $http, $localStorage) {
    const contextPath = 'http://localhost:8189/habr/';

    console.log('moderatorController')

    $scope.getArticlesModeration = function() {
       $http.get(contextPath + 'api/v1/articles/status/moderating')
           .then(function (response) {
               $scope.articlesModeration = response.data;
           });
    }

    $scope.getArticlesModeration();
});
