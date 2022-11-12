(
    function () {
        angular
            .module(
                'HabrApp',
                [
                    'ngRoute',
                    'ngStorage'
                ]
            )
            .config(config)
            .run(run);

        function config($routeProvider) {
            $routeProvider
                .when(
                    '/',
                    {
                        templateUrl: 'articles/articles.html',
                        controller: 'articlesController'
                    }
                )
                .when(
                    '/profile',
                    {
                        templateUrl: 'profile/profile.html',
                        controller: 'profileController'
                    }
                )
                .when(
                    '/edit-article/:articleId',
                    {
                        templateUrl: 'edit-article/edit-article.html',
                        controller: 'editArticleController'
                    }
                )
                .when(
                '/create-article',
                {
                    templateUrl: 'create-article/create-article.html',
                    controller: 'createArticleController'
                }
            )
                .when(
                    '/authorization',
                    {
                        templateUrl: 'authorization/authorization.html',
                        controller: 'authorizationController'
                    }
                )
                .otherwise(
                    {
                        redirectTo: '/'
                    }
                )
            ;
        }

        function run($rootScope, $http, $localStorage) {}
    }
) ()
;

angular
    .module('HabrApp')
    .controller(
        'indexController',
        function (
            $rootScope,
            $scope,
            $http,
            $location,
            $localStorage
        ) {
            const rootPath = 'http://localhost:8189/habr/';
            const categoriesPath = 'api/v1/categories';
            const defaultCategory =
                {
                    id: -1,
                    name: "Все категории"
                };
            var path;

            $rootScope.category = defaultCategory;

            $scope.setCategories = function () {
                path = rootPath + categoriesPath;

                $http
                    .get(path)
                    .then(
                        function (response) {
                            $scope.categories = response.data;
                        }
                    )
                ;
            }
            $scope.setCategory = function (index) {
                 $rootScope.category = $scope.categories[index];
            }
            $scope.resetCategory = function () {
                 $rootScope.category = defaultCategory;
            }

            $scope.setCategories();
        }
    )
;