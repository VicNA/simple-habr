angular.module('HabrApp').controller('searchController', function($rootScope, $scope, $http, $localStorage) {
    const contextPath = 'http://' + window.location.host + '/habr/api/v1/articles';

    $scope.loadArticles = function () {
//        if (pageIndex != $scope.currentPage) {
//            $scope.currentPage = pageIndex;
//        }

        var title = document.getElementById('inputTitleSearch').value;

        if (title.length > 0) {
            var sort = document.getElementById('selectSearchSort').value +
                ',' + document.querySelector('input[name="radioSort"]:checked').value;

            $http({
                url: contextPath,
                method: 'GET',
                params: {
                    page: pageIndex,
                    title: title,
                    sort: sort
                }
            }).then(function(response) {
                   $scope.articles = response.data;
            });
        } else {
            $scope.articles = []
        }
    }

//    $scope.updateStatus = function(articleId, statusName) {
//        $http({
//            url: contextPath + '/moderation/' + articleId +  '/updateStatus',
//            method: 'PUT',
//            params: {
//                status: statusName
//            }
//        }).then(function(response) {
//            $scope.getArticlesModeration();
//        });
//    }
//
//    $scope.setArticle = function(index) {
//        console.log('index: ' + index);
//        $rootScope.article = $scope.articlesModeration[index];
//    }
//
    $scope.loadArticles();
});
