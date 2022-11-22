angular.module('HabrApp').controller('registrationController', function ($scope, $http, $location, $localStorage) {
    const contextPath = 'http://' + window.location.host + '/habr/';

     $scope.functionRegistration = function () {
            $http.post(contextPath + 'api/v1/registration', $scope.newUser).then(function successCallback (response) {
                    if (response.data.token) {
                        alert('Вы успешно зарегистрированы');
                        $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                        $localStorage.localUser = {username: $scope.newUser.username, token: response.data.token};
                        $localStorage.newUser = null;

                        $location.replace();
                        $location.path('/');
                    }
                }, function failureCallback (response) {
                        console.log(response);
                        alert(response.data.message);
                });
           }

});