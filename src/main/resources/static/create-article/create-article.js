angular
    .module('HabrApp')
    .controller('createArticleController', function ($scope, $http, $localStorage) {
        const contextPath = 'http://' + window.location.host + '/habr/';


        $scope.createArticle = function (){
            $scope.articleInf.authorUsername = $localStorage.localUser.username;
            if($scope.newFile && !$scope.articleInf.imagePath){
                 uploadFile($scope.createArticle);
                 return;
            }
            $http.put(contextPath + 'api/v1/articles/create', $scope.articleInf)
                .then(function successCallback (response) {
                    alert('Статья сохранена как черновик');
                                        console.log($scope.articleInf);

                }, function failureCallback (response) {
                    alert(response.data.message);
                    console.log(response.data);
                });
        }

        $scope.publicateArticle = function (){
            $scope.articleInf.authorUsername = $localStorage.localUser.username;
            if($scope.newFile && !$scope.articleInf.imagePath){
                 uploadFile($scope.publicateArticle);
                 return;
            }
            $http.put(contextPath + 'api/v1/articles/createAndPublicate', $scope.articleInf)
                .then(function successCallback (response) {
                    alert('Статья отправлена на публикацию');
                }, function failureCallback (response) {
                    alert(response.data.message);
                });
        }

     function uploadFile(nameFunction){

         var formData = new FormData();
         formData.append("file", $scope.newFile[0]);
         $http.post(contextPath + "api/v1/image", formData, {headers: { 'Content-Type' : undefined }}
                ).then(function successCallback (response) {
                    $scope.articleInf.imagePath = response.data.message;
                    nameFunction();
                }, function failureCallback (response) {
                    alert(response.message);
                });
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
