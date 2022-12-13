angular.module('HabrApp').controller('searchController', function($rootScope, $scope, $http, $localStorage) {
    const rootPath = 'http://' + window.location.host + '/habr/api/v1';
    const contextPath = rootPath + '/articles';
    const constantPath = rootPath + '/constants';

    $scope.filters = [];
    $scope.currentPage = 1;
    totalPages = 1;

    $scope.getFilterDtPublished = function () {
        if ($scope.filters.length == 0) {
            $http.get(constantPath + '/publishedDate').then(function(response) {
                $scope.filters = response.data
            });
        }
    }

    $scope.loadArticles = function (pageIndex = 1) {
        if (pageIndex != $scope.currentPage) {
            $scope.currentPage = pageIndex;
        }

        var title = document.getElementById('inputTitleSearch').value;

        if (title.length > 0) {
            var sort = [
                document.getElementById('selectSearchSort').value,
                document.querySelector('input[name="radioSort"]:checked').value
            ].join(',');

            $http({
                url: contextPath + '/findByFilter',
                method: 'GET',
                params: {
                    page: pageIndex,
                    title: title,
                    sort: sort
                }
            }).then(function(response) {
                $scope.articles = response.data.content;
                totalPages = response.data.totalPages;
                $scope.paginationArray = Array.from({length: totalPages}, (x, i) => i + 1);
            });
        } else {
            $scope.articles = [];
        }
    }

    $scope.isPreviousPage = () =>  $scope.currentPage == 1 ? false : true;

    $scope.isNextPage = () => ($scope.currentPage == totalPages) ? false : true;

    $scope.getFilterDtPublished();
    $scope.loadArticles($scope.currentPage);
});
