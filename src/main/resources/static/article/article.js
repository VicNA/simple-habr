angular
    .module('HabrApp')
    .controller(
        'articleController',
        function (
            $rootScope,
            $scope,
            $http,
            $location,
            $localStorage,
            $sessionStorage
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

            $scope.getArticle = function () {
                $rootScope.articleId = $sessionStorage.articleId;

                if ($rootScope.articleId == -1) {
                    $scope.article = defaultArticle;
                    return;
                }

                path = rootPath + articlesPath + "/view" + "/" + $rootScope.articleId;
                $http
                    .get(
                        path
                    )
                    .then(
                        function (response) {
                            $scope.article = response.data;
                            $scope.getListComments($rootScope.articleId);
                        }
                    )
                ;
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
                        function (response) {}
                    )
                ;
            }

            $scope.getArticle($rootScope.articleId);

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
        }
    )
;
