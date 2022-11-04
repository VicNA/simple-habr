var testApp =
    angular
        .module("HabrApp", []);

testApp
    .controller(
        "HabrController",
        function (
            $scope,
            $http
        ) {
            $scope.category = {name: "Все категории"};
            $scope.getAllCategories = function () {
                $http
                    .get('http://localhost:8189/habr/api/v1/categories')
                    .then(
                        function (response) {
                            $scope.categories = response.data;
                        }
                    )
                ;
            }
            $scope.getAllArticles = function () {
                $http
                    .get('http://localhost:8189/habr/api/v1/articles')
                    .then(
                        function (response) {
                            $scope.articles = response.data;
                        }
                    )
                ;
            }
            $scope.setCategory = function (index) {
                $scope.category = $scope.categories[index];
            }
            $scope.setArticle = function (index) {
                $scope.article = $scope.articles[index];
            }

            $scope.getAllCategories();
            $scope.getAllArticles();
        }
    )
;
