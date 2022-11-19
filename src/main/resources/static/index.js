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
                    '/article',
                    {
                        templateUrl: 'article/article.html',
                        controller: 'articleController'
                    }
                )
                .when(
                    '/profile',
                    {
                        templateUrl: 'profile/profile.html',
                        controller: 'profileController'
                    }
                )
                .when('/moderator', {
                    templateUrl: 'moderator/moderator.html',
                    controller: 'moderatorController'
                    })
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
                .when(
                    '/registration',
                    {
                        templateUrl: 'authorization/registration.html',
                        controller: 'registrationController'
                    }
                )
                .otherwise(
                    {
                        redirectTo: '/'
                    }
                )
            ;
        }

    function run($rootScope, $http, $localStorage) {
        if ($localStorage.localUser) {
            try {
                let jwt = $localStorage.localUser.token;
                let payload = JSON.parse(atob(jwt.split('.')[1]));
                let currentTime = parseInt(new Date().getTime() / 1000);
                if (currentTime > payload.exp) {
                    console.log("Token is expired!!!");
                    delete $localStorage.localUser;
                    $http.defaults.headers.common.Authorization = '';
                } else {
                    $http.post('http://localhost:8189/habr/api/v1/refreshToken', $localStorage.localUser.token).then(function (response) {
                         $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                         $localStorage.localUser = {username: $scope.jwtResp.username, token: response.data.token};
                    });
                }

            } catch (e) {
            }
           }
        }
})();
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

            $scope.tryToLogout = function () {
                $scope.clearUser();
                $location.path('/');
            };

            $scope.clearUser = function () {
                delete $localStorage.localUser;
                $http.defaults.headers.common.Authorization = '';
            };

            $scope.isUserLoggedIn = function () {
                if ($localStorage.localUser) {
                    return true;
                } else {
                    return false;
                }
            };
        }
    )
;