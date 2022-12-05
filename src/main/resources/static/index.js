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
                        controller: 'authorizationController',
                    }
                )
                .when(
                    '/registration',
                    {
                        templateUrl: 'authorization/registration.html',
                        controller: 'registrationController'
                    }
                )
                .when(
                    '/admin',
                    {
                        templateUrl: 'admin/admin.html',
                        controller: 'adminController'
                    }
                )
                .when(
                    '/help',
                    {
                        templateUrl: 'help/help.html'
                    }
                )
                .otherwise(
                    {
                        redirectTo: '/'
                    }
                )
            ;
        }

    function run($http, $localStorage) {
        if ($localStorage.localUser) {
            $http.defaults.headers.common.Authorization = '';
            try {
                let jwt = $localStorage.localUser.token;
                let payload = JSON.parse(atob(jwt.split('.')[1]));
                let currentTime = parseInt(new Date().getTime() / 1000);
                if (currentTime > payload.exp) {
                    console.log("Token is expired!!!");
                    delete $localStorage.localUser;
                }
                else {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
                    $http.post('http://' + window.location.host + '/habr/api/v1/refreshToken', jwt)
                    .then(function successCallback (response) {
                        $localStorage.localUser.token = response.data.token;
                        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.localUser.token;
                    }, function failureCallback (response) {
                         console.log(response);
                         alert(response.data.message);
                        delete $localStorage.localUser;
                        $http.defaults.headers.common.Authorization = '';
                    });
                }
            } catch (e) {
               console.log('Ошибка обновления токена' + e);
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
            const rootPath = 'http://' + window.location.host + '/habr/';
            const categoriesPath = 'api/v1/categories';
            const contextPathNotification = 'http://' + window.location.host + '/habr/api/v1/notifications';
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

            $scope.isModeratorLoggedIn = function () {
                if ($scope.isUserLoggedIn) {
                let roles = getRoles();
                    for (var i = 0; i < roles.length; i++) {
                        if (roles[i] == 'ROLE_MODERATOR') {
                    return true;
                        }
                    }
                }

                return false;
            };

            $scope.isAdminLoggedIn = function () {
                if ($scope.isUserLoggedIn) {
                    let roles = getRoles();
                    for (var i = 0; i < roles.length; i++) {
                        if (roles[i] == 'ROLE_ADMIN') {

                            return true;
                        }
                    }
                }

                return false;
            };

            function getRoles() {
            let roles = {};
            if ($localStorage.localUser) {
                    try {
                        let jwt = $localStorage.localUser.token;
                        let payload = JSON.parse(atob(jwt.split('.')[1]));
                        roles = payload.roles;
                    } catch (e) {
                       console.log('Ошибка чтения токена' + e);
                    }
                        }
            return roles;
            }

            //функция для отправки уведомления
            $scope.sendNotification = function (text, recipient) {
                notification = {
                                "recipient": recipient,
                                "sender": $localStorage.localUser.username,
                                "text": text
                };

                $http
                     .post(contextPathNotification + "/create", notification)
                     .then(
                         function (response) {}
                )
            };



        }
    )
;

