angular
    .module('HabrApp')
    .controller(
        'editArticleController',
        function (
            $scope,
            $routeParams,
            $http,
            $localStorage,
            $location
        ) {
            const contextPath = 'http://' + window.location.host + '/habr/';
            var articleId = $routeParams.articleId;


     $scope.getArticle = function (){
            $http.get(contextPath + 'api/v1/articles/view/' + articleId)
                .then(function successCallback (response) {
                    $scope.articleInf = response.data;
                    $scope.getSourceImage($scope.articleInf.imagePath);
                }, function failureCallback (response) {
                    console.log(response);
                    alert(response.data.message);
                });
        }

        $scope.updateArticle = function (){
            $scope.articleInf.authorUsername = $localStorage.localUser.username;
            if($scope.newFile!=null && !$scope.articleInf.imagePath){
                 uploadFile($scope.updateArticle);
                 return;
            }
            $http.put(contextPath + 'api/v1/articles/updatePublicFields', $scope.articleInf)
                .then(function successCallback (response) {
                    alert('Статья сохранена как черновик');
                    $location.path('/profile');
                }, function failureCallback (response) {
                    alert(response.data.message);
                });
        }

        $scope.updateArticleAndPublicate = function (){
            $scope.articleInf.authorUsername = $localStorage.localUser.username;
            if($scope.newFile!=null && !$scope.articleInf.imagePath){
                 uploadFile($scope.updateArticleAndPublicate);
                 return;
            }
            $http.put(contextPath + 'api/v1/articles/updatePublicFieldsAndPublicate', $scope.articleInf)
                .then(function successCallback (response) {
                    alert('Статья отправлена на модерацию');
                    $location.path('/profile');
                }, function failureCallback (response) {
                    alert(response.data.message);
                });
        }


        $scope.getArticle();

        function uploadFile(nameFunction){

             var formData = new FormData();
             formData.append("file", $scope.newFile[0]);
             $http.post(contextPath + "api/v1/image", formData, {headers: { 'Content-Type' : undefined }}
                    ).then(function successCallback (response) {
                        $scope.articleInf.imagePath = response.data.message;
                        nameFunction();
                        delete $scope.newFile;
                    }, function failureCallback (response) {
                        alert(response.message);
                    });
        };

        $scope.getSourceImage = function(imagePath){
                if($scope.articleInf.imagePath!=null){
                    $scope.sourceImage = contextPath.concat('files/',imagePath);
                }
            };
});
angular.module("HabrApp").directive("selectNgFiles", function() {
  return {
    require: "ngModel",
    link: function postLink(scope,elem,attrs,ngModel) {
      elem.on("change", function(e) {
        var files = elem[0].files;
        ngModel.$setViewValue(files);
      })
    }
  }
});