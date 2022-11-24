angular.module('HabrApp').controller('profileController', function ($rootScope,$scope, $http, $localStorage, $location) {
    const contextPath = 'http://' + window.location.host + '/habr/';
    user1 =$localStorage.localUser.username;
    $scope.curPage = 1;
    totalPages = 1;

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

    $scope.getUserArticles = function (pageIndex){
            if (pageIndex != $scope.curPage) {
                $scope.curPage = pageIndex;
            }

           $http.get(contextPath + 'api/v1/articles/user?username=' + user1 + '&page=' + pageIndex)
               .then(function successCallback (response) {
                   $scope.userArticles = response.data.content;
                   totalPages = response.data.totalPages;
                   $scope.paginationArrayLK = $scope.generatePagesIndexesLK(1, totalPages);
                   console.log(response);
               }, function failureCallback (response) {
                   console.log(response);
                   alert(response.data.message);
               });
    }

    $scope.generatePagesIndexesLK = function (startPage, endPage) {
        let arr = [];
        for (let i = startPage; i < endPage + 1; i++) {
            arr.push(i);
        }
        return arr;
    }


    $scope.isPreviousPageLK = function () {
        return ($scope.curPage == 1) ? false : true;
    }

    $scope.isNextPageLK = function () {
        return ($scope.curPage == totalPages) ? false : true;
    }


    $scope.setArticle = function (index) {
        $rootScope.article = $scope.userArticles[index];
    }

    $scope.setArticleForDel = function (index) {
        $scope.articleDel = index;
    }

    $scope.deleteArticle = function (articleId){
        if (articleId != null) {
               $http.delete(contextPath + 'api/v1/articles/' + articleId)
                   .then(function successCallback (response) {
                       setTimeout(() => $("#exampleModal [data-dismiss=modal]").trigger({ type: "click" }), 0);
                       $scope.getUserArticles($scope.curPage);
                   }, function failureCallback (response) {
                       console.log(response);
                       alert(response.data.message);
                   });
        }
    }


    $scope.getUserInfo();
    $scope.getUserArticles($scope.curPage);
});
