angular.module('HabrApp').controller('profileController', function ($rootScope,$scope, $http, $localStorage, $location) {
    const contextPath = 'http://' + window.location.host + '/habr/';
    const user1 = 'bob';

    $scope.getUserInfo = function (){
       $http.get(contextPath + 'api/v1/user/' + user1)
           .then(function successCallback (response) {
               $scope.userInf = response.data;
               roles = response.data.roles;
               $scope.role = roles[0].name;

               if (roles.length > 1) {
                 let names = roles.map(item => item.name).join(' & ');
                 $scope.role = names;
               };

           }, function failureCallback (response) {
               console.log(response);
               alert(response.data.message);
           });
    }

    $scope.updateUser = function (){
       $http.put(contextPath + 'api/v1/user/update', $scope.userInf)
          .then(function successCallback (response) {
              alert('Данные о пользователе сохранены');
          }, function failureCallback (response) {
             console.log(response);
             alert(response.data.message);
             location.reload();
          });
    }

    $scope.getUserArticles = function (){
           $http.get(contextPath + 'api/v1/articles/username/' + user1)
               .then(function successCallback (response) {
                   $scope.userArticles = response.data;
                   console.log(response);
               }, function failureCallback (response) {
                   console.log(response);
                   alert(response.data.message);
               });
    }


     $scope.setArticle = function (index) {
         $rootScope.article = $scope.userArticles[index];
     }

    $scope.getUserInfo();
    $scope.getUserArticles();
});
