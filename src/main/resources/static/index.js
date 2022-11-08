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
            .otherwise({
                redirectTo: '/'
            });
    }

    function run($rootScope, $http, $localStorage) {

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