(function () {
    angular
        .module('HabrApp', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'main/main.html',
                controller: 'mainController'
            })
            .when('/profile', {
                templateUrl: 'profile/profile.html',
                controller: 'profileController'
            })
            .when('/authorization', {
                            templateUrl: 'authorization/authorization.html',
                            controller: 'authorizationController'
                        })
            .when('/registration', {
                            templateUrl: 'authorization/registration.html',
                            controller: 'registrationController'
                        })
                        .otherwise({
                redirectTo: '/'
            });
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
                } else{
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


angular.module('HabrApp').controller('indexController', function ($rootScope, $scope, $http, $location, $localStorage) {
   const contextPath = 'http://localhost:8189/habr/';
   $scope.category = {name: "Все категории"};

   $scope.getAllCategories = function () {
                   $http
                       .get(contextPath + 'api/v1/categories')
                       .then(
                           function (response) {
                               $scope.categories = response.data;
                           }
                       );
   }

   $scope.setCategory = function (index) {
                 $scope.category = $scope.categories[index];
   }

   $scope.getAllCategories();
});