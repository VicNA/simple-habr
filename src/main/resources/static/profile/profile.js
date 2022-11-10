angular.module('HabrApp').controller('profileController', function ($scope, $http, $localStorage) {
    const contextPath = 'http://localhost:8189/habr/';
    const user1 = 'bob';

    $scope.getUserInfo = function (){
       $http.get(contextPath + 'api/v1/user/' + user1)
           .then(function successCallback (response) {
               $scope.userInf = response.data;
           }, function failureCallback (response) {
               console.log(response);
               alert(response.data.message);
           });
    }

    $scope.updateUser = function (){
          alert("Дата = " + $scope.userInf.dtBirth);
       $http.put(contextPath + 'api/v1/user/update', $scope.userInf)
          .then(function successCallback (response) {
              alert('Данные о пользователе сохранены');
          }, function failureCallback (response) {
             alert(response.data.message);
          });
    }

    $scope.getUserArticles = function (){
           $http.get(contextPath + 'api/v1/articles/username/' + user1)
               .then(function successCallback (response) {
                   $scope.userArticles = response.data;
               }, function failureCallback (response) {
                   console.log(response);
                   alert(response.data.message);
               });
    }

    $scope.setArticle = function (index) {
           $scope.userArticles = $scope.userArticles[index];
    }

    $scope.getUserInfo();
    $scope.getUserArticles();
});
