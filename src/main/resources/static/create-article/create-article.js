angular
    .module('HabrApp')
    .controller('createArticleController', function ($scope, $http, $localStorage) {
        const contextPath = 'http://' + window.location.host + '/habr/';


        $scope.createArticle = function (){
            $http.put(contextPath + 'api/v1/articles/create', $scope.articleInf)
                .then(function successCallback (response) {
                    alert('Статья сохранена как черновик');
                }, function failureCallback (response) {
                    alert(response.data.message);
                });
        }

        $scope.publicateArticle = function (){
            $http.put(contextPath + 'api/v1/articles/createAndPublicate', $scope.articleInf)
                .then(function successCallback (response) {
                    alert('Статья отправлена на публикацию');
                }, function failureCallback (response) {
                    alert(response.data.message);
                });
        }
})
