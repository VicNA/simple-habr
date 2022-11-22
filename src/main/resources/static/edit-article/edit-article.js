angular
    .module('HabrApp')
    .controller('editArticleController', function ($scope, $routeParams, $http, $localStorage) {
        var articleId = $routeParams.articleId;
        const contextPath = 'http://' + window.location.host + '/habr/';


     $scope.getArticle = function (){
            $http.get(contextPath + 'api/v1/articles/view/' + articleId)
                .then(function successCallback (response) {
                    $scope.articleInf = response.data;
                }, function failureCallback (response) {
                    console.log(response);
                    alert(response.data.message);
                });
        }

        $scope.updateArticle = function (){
            $http.put(contextPath + 'api/v1/articles/updatePublicFields', $scope.articleInf)
                .then(function successCallback (response) {
                    alert('Статья сохранена как черновик');
                }, function failureCallback (response) {
                    alert(response.data.message);
                });
        }

        $scope.updateArticleAndPublicate = function (){
            $http.put(contextPath + 'api/v1/articles/updatePublicFieldsAndPublicate', $scope.articleInf)
                .then(function successCallback (response) {
                    alert('Статья отправлена на модерацию');
                }, function failureCallback (response) {
                    alert(response.data.message);
                });
        }


        $scope.getArticle();
})