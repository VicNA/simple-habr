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
            const ratingPath = rootPath + articlesPath + '/rating';
            var path = rootPath + articlesPath;

            $scope.currentPage = 1;
            totalPages = 1;

            $scope.getArticles = function (pageIndex) {
                var urlParamData;

                if (pageIndex != $scope.currentPage) {
                    $scope.currentPage = pageIndex;
                }

                if($rootScope.category.id == -1) {
                    urlParamData =
                        {
                            "page": pageIndex
                        }
                    ;
                } else {
                   path = path + categoryPath;
                   urlParamData =
                       {
                            "id": $rootScope.category.id,
                            "page": pageIndex
                       }
                   ;
                }

                $http
                    .get(
                        path,
                        {
                            params: urlParamData
                        }
                    )
                    .then(
                        function (response) {
                            $scope.articles = response.data.content;
                            totalPages = response.data.totalPages;
                            $scope.paginationArray = $scope.generatePagesIndexes(1, totalPages);
//                            console.log($scope.articles);
                        }
                    )
                ;
            }

            $scope.generatePagesIndexes = function (startPage, endPage) {
                let arr = [];
                for (let i = startPage; i < endPage + 1; i++) {
                    arr.push(i);
                }
                return arr;
            }

            $scope.loadRatingArticles = function() {
                $http.get(ratingPath).then(function(response) {
                    $scope.ratingArticles = response.data.content;
                });
            }


            $scope.isPreviousPage = function () {
                return ($scope.currentPage == 1) ? false : true;
            }

            $scope.isNextPage = function () {
                return ($scope.currentPage == totalPages) ? false : true;
            }

            $scope.getSourceImage = function(imagePath){
                if(imagePath!=null){
                   return rootPath.concat('files/',imagePath);
               }
               return null;
            };

            $scope.getArticles($scope.currentPage);
            $scope.loadRatingArticles();
  });
;
