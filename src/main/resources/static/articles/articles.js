angular
    .module('HabrApp')
    .controller(
        'articlesController',
        function (
            $rootScope,
            $scope,
            $http,
            $location,
            $localStorage
        ) {
            const rootPath = 'http://' + window.location.host + '/habr/';
            const articlesPath = 'api/v1/articles';
            const categoryPath = '/category';
            var path;
            $scope.currentPage = 1;
            totalPages = 1;

            $rootScope.article = {
                id: -1,
                title: "Все статьи"
            };

            $scope.setArticles = function (pageIndex) {
                if (pageIndex != $scope.currentPage) {
                                   $scope.currentPage = pageIndex;
                }

                if($rootScope.category.id == -1) {
                   path = rootPath + articlesPath + '?page=' + pageIndex;
                } else {
                   path = rootPath + articlesPath + categoryPath + '?id='+ $rootScope.category.id + '&page=' + pageIndex;
                }

                $http
                     .get(path)
                     .then(
                           function (response) {
                           $scope.articles = response.data.content;
                           totalPages = response.data.totalPages;
                           $scope.paginationArray = $scope.generatePagesIndexes(1, totalPages);
                           console.log(response);
                           console.log($scope.articles);
                }
                     )
                     ;
            }

            $scope.setArticle = function (index) {
                 $rootScope.article = $scope.articles[index];
            }

            $scope.generatePagesIndexes = function (startPage, endPage) {
                let arr = [];
                for (let i = startPage; i < endPage + 1; i++) {
                    arr.push(i);
                }
                return arr;
            }


            $scope.isPreviousPage = function () {
                return ($scope.currentPage == 1) ? false : true;
            }

            $scope.isNextPage = function () {
                return ($scope.currentPage == totalPages) ? false : true;
            }

            $scope.setArticles($scope.currentPage);
  });
;
