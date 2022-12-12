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
            const categoriesPath = 'api/v1/categories';
            $scope.selection=[];

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

        $scope.getArticleCategories = function(){
            $http.get(contextPath + 'api/v1/categories/article/' + articleId)
                .then(function successCallback (response) {
                    $scope.articleCategories = response.data;
                }, function failureCallback (response) {
                    console.log(response);
                    alert(response.data.message);
                });
        }

        $scope.getCategories = function () {
            path = contextPath + categoriesPath;
            $http
                .get(path)
                .then(function (response) {
                        $scope.categories = response.data;

                        for (var i in $scope.articleCategories) {
                            $scope.selection.push($scope.articleCategories[i].name);
                        }

                        $scope.toggleSelection = function toggleSelection(categoryName) {
                            var idx = $scope.selection.indexOf(categoryName);
                            if (idx > -1) {
                                // is currently selected
                                $scope.selection.splice(idx, 1);
                            }
                            else {
                                // is newly selected
                                $scope.selection.push(categoryName);
                            }
                        };
                    }, function failureCallback (response) {
                    alert(response.data.message);
                });
        }

        $scope.updateArticle = function (){
            $scope.articleInf.authorUsername = $localStorage.localUser.username;
            if($scope.newFile!=null && !$scope.articleInf.imagePath){
                 uploadFile($scope.updateArticle);
                 return;
            }

            var request ='api/v1/articles/'+$scope.articleInf.id+'/updateCategories?categories='+ $scope.selection.join(',');
            $http.put( contextPath + request)
                .then(function successCallback (response) {
                }, function failureCallback (response) {
                    alert(response.data.message);
                });

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
            var request ='api/v1/articles/'+$scope.articleInf.id+'/updateCategories?categories='+ $scope.selection.join(',');
            $http.put( contextPath + request)
                .then(function successCallback (response) {
                }, function failureCallback (response) {
                    alert(response.data.message);
                });

            $http.put(contextPath + 'api/v1/articles/updatePublicFieldsAndPublicate', $scope.articleInf)
                .then(function successCallback (response) {
                    alert('Статья отправлена на модерацию');
                    $location.path('/profile');
                }, function failureCallback (response) {
                    alert(response.data.message);
                });
        }


        $scope.getArticle();
        $scope.getArticleCategories();
        $scope.getCategories();

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