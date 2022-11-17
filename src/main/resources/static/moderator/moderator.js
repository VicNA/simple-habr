angular.module('HabrApp').controller('moderatorController', function($rootScope, $scope, $http, $localStorage) {
    const contextPath = 'http://localhost:8189/habr/api/v1/articles';

    $scope.getArticlesModeration = function() {
       $http.get(contextPath + '/moderation')
           .then(function(response) {
               $scope.articlesModeration = response.data;
           });
    }

    $scope.updateStatus = function(articleId, statusName) {
        $http({
            url: contextPath + '/moderation/' + articleId +  '/updateStatus',
            method: 'PUT',
            params: {
                status: statusName
            }
        }).then(function(response) {
            $scope.getArticlesModeration();
        });
    }

    $scope.setArticle = function(index) {
        console.log('index: ' + index);
        $rootScope.article = $scope.articlesModeration[index];
    }

    $scope.getArticlesModeration();
});
