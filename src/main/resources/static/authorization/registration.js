angular.module('HabrApp').controller('registrationController', function ($scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:8189/habr/';

     $scope.functionRegistration = function () {
            $http.post(contextPath + 'api/v1/registration', $scope.newUser).then(function (response) {
                    if (response.data.token) {
                        $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                        $localStorage.localUser = {username: $scope.newUser.username, token: response.data.token};
                        $localStorage.newUser = null;
                        $location.path("/");
                    }
                });
           }

});