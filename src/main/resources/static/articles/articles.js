angular
    .module('HabrApp')
    .controller(
        'articlesController',
        function (
            $rootScope,
            $scope,
            $http,
            $location,
            $localStorage,
            $sessionStorage
        ) {
            const rootPath = 'http://' + window.location.host + '/habr/';
            const articlesPath = 'api/v1/articles';
            const categoryPath = '/category';
            var path = rootPath + articlesPath;

            $scope.currentPage = 1;
            totalPages = 1;

            if(!$sessionStorage.articleId) {
                $sessionStorage.articleId = -1;
            }

            // TODO Конфликтная функция, необходимо проверить
            $scope.setArticles = function (pageIndex) {
                var params = {
                    page: pageIndex,
                    sort: "dtPublished,desc"
                };

                if (pageIndex != $scope.currentPage) {
                    $scope.currentPage = pageIndex;
                }

                if($rootScope.category.id > -1) {
                    path = path + categoryPath
                    params.id = $rootScope.category.id;
                }

                $http({
                    url: path,
                    method: 'GET',
                    params: params
                }).then(function (response) {
                       $scope.articles = response.data.content;
                       totalPages = response.data.totalPages;
                       $scope.paginationArray = $scope.generatePagesIndexes(1, totalPages);
                       console.log(response);
                       console.log($scope.articles);
                });
            }

            $scope.setArticle = function (index) {
                $sessionStorage.articleId = index;
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
