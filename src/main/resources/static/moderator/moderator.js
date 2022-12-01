angular.module('HabrApp').controller('moderatorController', function($rootScope, $scope, $http, $localStorage) {
    const contextPath = 'http://' + window.location.host + '/habr/api/v1/articles';
    const contextPathNotification = 'http://' + window.location.host + '/habr/api/v1/notifications';

    $scope.curPageM = 1; //текущая страница
    $scope.checkPr = 1; //код причины удаления статьи
    totalPages = 1; //общее кол-во станиц

    var notification; //json уведомления
    var text; //текст уведомления об удалении статьи
    var articleIdForDel; //id статьи для удаления
    var articleTitleForDel; //название статьи для удаления
    var recipient; //получатель уведомления


    $scope.getArticlesModeration = function(pageIndex) {
       if (pageIndex != $scope.curPageM) {
           $scope.curPageM = pageIndex;
       }

       $http.get(contextPath + '/moderation?page=' + pageIndex)
           .then(function successCallback (response) {
               $scope.articlesModeration = response.data.content;
               totalPages = response.data.totalPages;
               $scope.paginationArrayM = $scope.generatePagesIndexesM(1, totalPages);

               if ($scope.paginationArrayM.length == 0){
                   document.getElementById("previewBtn").hidden = "true";
                   document.getElementById("nextBtn").hidden = "true";
               }

           }, function failureCallback (response) {
               alert(response.data.message);
           });
    }

    $scope.preparedForPublication = function(articleId, articleTitle, recipient) {
        notification = {
                        "recipient": recipient,
                        "sender": $localStorage.localUser.username,
                        "text": "Ваша статья <<" + articleTitle + ">> опубликована"
                       };

        $http
            .post(contextPathNotification + "/create", notification)
            .then(
            function (response) {}
        )

        $scope.updateStatus(articleId, 'published');
    }

    $scope.prepareForDelete = function(articleId, articleTitle, username) {
        articleIdForDel = articleId;
        articleTitleForDel = articleTitle;
        recipient = username;

        new bootstrap.Modal(document.getElementById('exampleModal')).show();
    }

    $scope.updateStatus = function(articleId, statusName) {
        $http({
                url: contextPath + '/moderation/' + articleId +  '/updateStatus',
                method: 'PUT',
                params: {
                    status: statusName
                }
        }).then(function(response) {
            $scope.getArticlesModeration($scope.curPageM);
        });
    }

    $scope.setArticle = function(index) {
        $rootScope.article = $scope.articlesModeration[index];
    }

    $scope.generatePagesIndexesM = function (startPage, endPage) {
         let arr = [];
         for (let i = startPage; i < endPage + 1; i++) {
             arr.push(i);
         }
         return arr;
    }


     $scope.isPreviousPageM = function () {
         return ($scope.curPageM == 1) ? false : true;
     }

     $scope.isNextPageM = function () {
         return ($scope.curPageM >= totalPages) ? false : true;
     }

     $scope.checkIndex = function(index){
        $scope.checkPr = index;
     }

    $scope.deleteArticle = function () {
        switch ($scope.checkPr) {
          case 1:
            text = "Модератор отклонил статью <<" + articleTitleForDel + ">> по причине: <<Статья не актуальна>>";
            break;
          case 2:
            text = "Модератор отклонил статью <<" + articleTitleForDel + ">> по причине: <<В статье содержатся нецензурные выражения>>";
            break;
          case 3:
            text = "Модератор отклонил статью <<" + articleTitleForDel + ">> по причине: <<Плагиат>>";
            break;
          case 4:
            text = "Модератор отклонил статью <<" + articleTitleForDel + ">> по причине: <<" + document.getElementById("descriptionArea").value + ">>";
            break;
        }

        notification = {
                        "recipient": recipient,
                        "sender": $localStorage.localUser.username,
                        "text": text
                       };

        $http
            .post(contextPathNotification + "/create", notification)
            .then(
            function (response) {}
        )

        $scope.updateStatus(articleIdForDel, 'hidden');

        setTimeout(() => $("#exampleModal [data-bs-dismiss=modal]").trigger({ type: "click" }), 0);
    }

    $scope.getArticlesModeration($scope.curPageM);
});
