angular.module('HabrApp').controller('moderatorController', function($rootScope, $scope, $http, $localStorage) {
    const contextPath = 'http://' + window.location.host + '/habr/api/v1/articles';
    const contextPathNotification = 'http://' + window.location.host + '/habr/api/v1/notifications';
    $scope.curPageM = 1;
    totalPages = 1;
    var notification;

    $scope.getArticlesModeration = function(pageIndex) {
       if (pageIndex != $scope.curPageM) {
           $scope.curPageM = pageIndex;
       }

       $http.get(contextPath + '/moderation?page=' + pageIndex)
           .then(function successCallback (response) {
               $scope.articlesModeration = response.data.content;
               totalPages = response.data.totalPages;
               $scope.paginationArrayM = $scope.generatePagesIndexesM(1, totalPages);
           }, function failureCallback (response) {
               alert(response.data.message);
           });
    }

    $scope.updateStatus = function(articleId, recipient, statusName) {
        notification = {
                        "recipient": recipient,
                        "sender": $localStorage.localUser.username,
                        "text": "Ваша статья опубликована"
                       };

        $http
            .post(contextPathNotification + "/create", notification)
            .then(
            function (response) {}
        )

        $http({
            url: contextPath + '/moderation/' + articleId +  '/updateStatus',
            method: 'PUT',
            params: {
                status: statusName
            }
        }).then(function(response) {
            $scope.getArticlesModeration($scope.curPageM);
        });
    }

    $scope.setArticle = function(index) {
        console.log('index: ' + index);
        $rootScope.article = $scope.articlesModeration[index];
    }

     $scope.generatePagesIndexesM = function (startPage, endPage) {
         let arr = [];
         for (let i = startPage; i < endPage + 1; i++) {
             arr.push(i);
         }
         return arr;
     }


     $scope.isPreviousPageM = function () {
         return ($scope.curPageM == 1) ? false : true;
     }

     $scope.isNextPageM = function () {
         return ($scope.curPageM == totalPages) ? false : true;
     }



    $scope.getArticlesModeration($scope.curPageM);
});
