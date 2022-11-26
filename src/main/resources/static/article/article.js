angular
    .module('HabrApp')
    .controller(
        'articleController',
        function (
            $rootScope,
            $scope,
            $http,
            $location,
            $localStorage
        ) {
            const rootPath = 'http://' + window.location.host + '/habr/';
            const articlesPath = 'api/v1/articles';
            const likesPath = 'api/v1/likes';
            const defaultArticle =
                {
                    "id": -1,
                    "title": "Все статьи",
                    "text": ""
                }
            ;
            var path;
            var like;

            $scope.getArticle = function (articleId) {
                if (articleId == -1) {
                    $scope.article = defaultArticle;
                }

                path = rootPath + articlesPath + "/view" + "/" +articleId;
                $http
                    .get(
                        path
                    )
                    .then(
                        function (response) {
                            $scope.article = response.data;
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
        }
    )
;
