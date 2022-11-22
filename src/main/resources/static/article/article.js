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
            const rootPath = 'http://localhost:8189/habr/';
            const likesPath = 'api/v1/likes';
            var path;
            var like;

            $scope.addLike = function () {
                path = rootPath + likesPath;
                like =
                    {
                        "username": $localStorage.localUser.username,
                        "articleId": $rootScope.article.id
                    };

                $http
                    .post(path + "/add", like)
                    .then(
                        function (response) {}
                    )
                ;
            }
        }
    )
;
