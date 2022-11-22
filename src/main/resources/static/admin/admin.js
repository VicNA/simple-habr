angular.module('HabrApp').controller('adminController', function($rootScope, $scope, $http, $localStorage) {
    const contextPath = 'http://' + window.location.host + '/habr/api/v1/admin';

    $scope.getListModerators = function() {
       $http.get(contextPath + '/view/moderators')
           .then(function successCallback (response) {
               $scope.moderators = response.data;
           }, function failureCallback (response) {
               console.log(response);
               alert(response.data.message);
           );
    }

    $scope.getListModerators();

    $scope.deleteModerator = function(username) {
        updateRole(username, 'ROLE_USER')
    }

    $scope.deleteModerator = function(username) {
        updateRole(username, 'ROLE_MODERATOR')
    }

    function updateRole(username, newRole) {
       $http({
             url: contextPath + '/update/role',
             method: 'PUT',
             params: {
                 username: username,
                 role: newRole
             }
       }).then(function successCallback (response) {
           $scope.getListModerators();
       });
    }
});
