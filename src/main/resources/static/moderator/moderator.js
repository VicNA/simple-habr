angular.module('HabrApp').controller('moderatorController', function($rootScope, $scope, $http, $localStorage) {
    const contextPathArticle = 'http://' + window.location.host + '/habr/api/v1/articles';
    const contextPathUser = 'http://' + window.location.host + '/habr/api/v1/user';

    $scope.getArticlesModeration = function() {
       $http.get(contextPathArticle + '/moderation')
           .then(function(response) {
               $scope.articlesModeration = response.data;
           });
    }

    $scope.updateStatus = function(articleId, statusName) {
        $http({
            url: contextPathArticle + '/moderation/' + articleId +  '/updateStatus',
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

    $scope.addBanUser = function(banUser) {
        $http.post(contextPathUser + '/moderation/ban', banUser)
               .then(function successCallback (response) {
                  alert("Пользователь " + banUser.username + " забанен. Количество дней: " + banUser.daysBan    );
                  delete $scope.banUser;
              }, function failureCallback (response) {
                      console.log(response);
                      alert(response.data.message);
              });
    }
});
