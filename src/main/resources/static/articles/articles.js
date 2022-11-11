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
            const rootPath = 'http://localhost:8189/habr/';
            const articlesPath = 'api/v1/articles';
            const categoryPath = '/category/';
            var path;

            $rootScope.article = {
                id: -1,
                title: "Все статьи"
            };

            $scope.setArticles = function () {
                if($rootScope.category.id == -1) {
                    path = rootPath + articlesPath;
                } else {
                    path = rootPath + articlesPath + categoryPath + $rootScope.category.id;
                }

                $http
                    .get(path)
                    .then(
                        function (response) {
                            $scope.articles = response.data;
                        }
                    )
                ;
            }

            $scope.setArticle = function (index) {
                 $rootScope.article = $scope.articles[index];
            }

            $scope.setArticles();
        }
    )
;
