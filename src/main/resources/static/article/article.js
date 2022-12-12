angular
    .module('HabrApp')
    .controller(
        'articleController',
        function (
            $rootScope,
            $scope,
            $routeParams,
            $http,
            $location,
            $localStorage,
            $anchorScroll
        ) {
            const rootPath = 'http://' + window.location.host + '/habr/';
            const articlesPath = 'api/v1/articles';
            const likesPath = 'api/v1/likes';
            const commentsPath = 'api/v1/comments';
            const defaultArticle =
                {
                    "id": -1,
                    "title": "Все статьи",
                    "text": ""
                }
            ;
            var path;
            var like;
            var articleId;

            $scope.getArticle = function () {
                if (articleId == -1) {
                    $scope.article = defaultArticle;
                    return;
                }

                path = rootPath + articlesPath + "/view" + "/" + articleId;
                $http
                    .get(
                        path
                    )
                    .then(
                        function (response) {
                            $scope.article = response.data;
                            $scope.getListComments(articleId);
                            $scope.getSourceImage($scope.article.imagePath);
                        }
                    )
                ;
            }

            if($routeParams.commentId){
                var commentId = $routeParams.commentId;
                $http.get(rootPath.concat(commentsPath,'/getArticleId/',commentId)).then(function (response) {
                       articleId = response.data;
                       $scope.getArticle();
                   });
            } else {
                articleId = $routeParams.articleId;
                $scope.getArticle();
            }

            $scope.addLike = function () {
                path = rootPath + likesPath;
                like =
                    {
                        "username": $localStorage.localUser.username,
                        "articleId": $scope.article.id
                    };

                $http
                    .post(path + "/add", like)
                    .then(
                        function (response) {
                            location.reload();
                        }
                    )
                ;
            }

            $scope.getListComments = function(articleId) {
               $http.get(rootPath + commentsPath + '/' + articleId)
                   .then(function (response) {
                       $scope.comments = response.data;
                   });
            }

            $scope.addComment = function (articleId, commentId, text) {
                let newComment = {};
                newComment.text = text;
                newComment.username = $localStorage.localUser.username;
                newComment.articleId = articleId;

                if(commentId != null){
                    newComment.parentCommentId = commentId;
                }

                $http.post(rootPath + commentsPath + '/add', newComment)
                .then(function successCallback (response) {
                   $scope.getListComments(articleId);
                   delete $scope.viewAnswerPanel;
               });
            }

            $scope.viewAnswer = function(id){
                if($localStorage.localUser){
                    $scope.viewAnswerPanel = id;
                } else {
                    alert("Необходимо авторизоваться");
                }

            }

            $scope.getSourceImage = function(imagePath){
                if($scope.article.imagePath!=null){
                    $scope.article.imagePath = rootPath.concat('files/',imagePath);
                }
            }

            $scope.gotoComment = function() {
                 if(commentId){
                    $location.hash('comment' + commentId);
                    $anchorScroll();
                 }
            }

            $scope.banComment = function(cId){
                $http.post(rootPath.concat(commentsPath,'/moderation/ban/',cId)).then(function (response) {
                    alert(response.data.message);
                    $scope.getListComments(articleId);
                });
            }
        }
    )
;
