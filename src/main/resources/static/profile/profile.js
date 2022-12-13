angular.module('HabrApp').controller('profileController', function ($rootScope,$scope, $http, $localStorage, $location) {
    const contextPath = 'http://' + window.location.host + '/habr/';
    const user1 = $localStorage.localUser.username;
    $scope.curPage = 1;
    totalPages = 1;
    $scope.countNotifications = 0;

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
               alert(response.data.message);
           });
    }

    $scope.updateUser = function (){
       $http.put(contextPath + 'api/v1/user/update', $scope.userInf)
          .then(function successCallback (response) {
              alert('Данные о пользователе сохранены');
          }, function failureCallback (response) {
             alert(response.data.message);
             location.reload();
          });
    }

    $scope.getUserArticles = function (pageIndex){
            if (pageIndex != $scope.curPage) {
                $scope.curPage = pageIndex;
            }

            if (pageIndex > totalPages) {
                pageIndex = totalPages;
                curPage = totalPages;
            }

           $http.get(contextPath + 'api/v1/articles/user?username=' + user1 + '&page=' + pageIndex)
               .then(function successCallback (response) {
                   $scope.userArticles = response.data.content;
                   totalPages = response.data.totalPages;
                   $scope.paginationArrayLK = $scope.generatePagesIndexesLK(1, totalPages);

                   if ($scope.paginationArrayLK.length == 0){
                       document.getElementById("previewBtnLK").hidden = "true";
                       document.getElementById("nextBtnLK").hidden = "true";
                   }
               }, function failureCallback (response) {
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
        return ($scope.curPage >= totalPages) ? false : true;
    }


    $scope.setArticle = function (index) {
        $rootScope.article = $scope.userArticles[index];
    }

    $scope.setArticleForDel = function (index) {
        $scope.articleDel = index;
    }

    $scope.closeNotification = function (){
         setTimeout(() => $("#notificationModal [data-bs-dismiss=modal]").trigger({ type: "click" }), 0);
    }

    $scope.deleteArticle = function (articleId){
        if (articleId != null) {
               $http.delete(contextPath + 'api/v1/articles/' + articleId)
                   .then(function successCallback (response) {
                       setTimeout(() => $("#notificationModal [data-bs-dismiss=modal]").trigger({ type: "click" }), 0);
                       $scope.getUserArticles($scope.curPage);
                   }, function failureCallback (response) {
                       console.log(response);
                       alert(response.data.message);
                   });
        }
    }

    $scope.getCountNotifications = function (){
        $http.get(contextPath + 'api/v1/notifications/count?username=' + user1)
             .then(function successCallback (response) {
                 $scope.countNotifications = response.data;
             }, function failureCallback (response) {
                 console.log(response);
        });
    }

    $scope.getNotifications = function () {
            $http.get(contextPath + 'api/v1/notifications?username=' + user1).then(function (response) {
                $scope.notifications = response.data;
            });
    }

    $scope.deleteNotifications = function (){
        $http.delete(contextPath + 'api/v1/notifications?username=' + user1)
            .then(function successCallback (response) {
                setTimeout(() => $("#notificationModal [data-bs-dismiss=modal]").trigger({ type: "click" }), 0);
                $scope.getCountNotifications();
        });
    }

    $scope.isNotifications = function () {
        return ($scope.countNotifications == 0) ? false : true;
    }

    $scope.isPublished = function (status) {
        return (status == 3) ? true : false;
    }

    $scope.getLinkContent = function (not){

        if(not.contentType === "article"){
            $("#notificationModal [data-bs-dismiss=modal]").trigger({ type: "click" });
            $location.path('/article/'.concat(not.contentId));
        }

        if(not.contentType === "comment"){
            $("#notificationModal [data-bs-dismiss=modal]").trigger({ type: "click" });
            $location.path('/article/comment/'.concat(not.contentId));
        }
    }

    $scope.getUserInfo();
    $scope.getUserArticles($scope.curPage);
    $scope.getCountNotifications();
    $scope.getNotifications();
});