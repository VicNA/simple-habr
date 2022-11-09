angular.module('HabrApp').controller('mainController', function ($scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:8189/habr/';

    $scope.getAllArticles = function () {
                       $http
                           .get(contextPath + 'api/v1/articles')
                           .then(
                               function (response) {
                                   $scope.articles = response.data;
                               }
                           );
   }

   $scope.setArticle = function (index) {
                      $scope.article = $scope.articles[index];
   }

   $scope.getAllArticles();
});
