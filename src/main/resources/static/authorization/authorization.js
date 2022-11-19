angular.module('HabrApp').controller('authorizationController', function ($scope, $http, $location, $localStorage, $rootScope) {
    const contextPath = 'http://localhost:8189/habr/';

     $scope.functionAuthorization = function () {
            $http.post(contextPath + 'api/v1/authorization', $scope.jwtResp).then(function successCallback (response) {
                    alert('Вы успешно авторизованы');
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.localUser = {username: $scope.jwtResp.username, token: response.data.token};

                    $scope.jwtResp.username = null;
                    $scope.jwtResp.password = null;

                    $location.path('/');
                    window.location.reload();

                }, function failureCallback (response) {
                                console.log(response);
                                alert(response.data.message);
                            });
           }
        $scope.clearUser = function () {
            delete $localStorage.localUser;
            $http.defaults.headers.common.Authorization = '';
        };


});