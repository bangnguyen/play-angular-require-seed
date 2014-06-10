/**
 * User controllers.
 */
define(["angular", "jsRoutes"], function (angular, jsRoutes) {
    "use strict";

    var CourseCtrl = function ($scope, $location, $timeout, $http, ngTableParams, alertService, $cookieStore) {


        var searchMode = false


        $scope.teachers = []


        jsRoutes.controllers.Search.getAllTeacher().ajax({
                dataType: 'json',
                success: function (response) {
                    $timeout(function () {
                        //$scope.teachers =  response
                        // $scope.teachers = response
                        $scope.teachers = response.data
                        console.log(response)
                        console.log($scope.teachers)
                    })

                }
            }
        )

        $scope.levels = [
            "Beginner", "Elem", "PostElem", "PreInter", "PostInter", "Advance"
        ]


        if ($cookieStore.get("course") != undefined) {
            $scope.item = $cookieStore.get("course")
            $scope.item.start = new Date($scope.item.start)
            $scope.item.finish = new Date($scope.item.finish)
            // $scope.item = JSON.parse($cookieStore.get("course"))
        } else {
            $scope.item = {
                code: "",
                title: "",
                level: $scope.levels[0],
                teacher1: "",
                start: new Date(),
                finish: new Date(),
                days_hours: "",
                price: 0,
                discount: 0.0,
                comment: ""
            }
        }


        $scope.$watch("item", function () {
            $cookieStore.put("course", $scope.item)
            console.log($cookieStore.get("course"))
        }, true)


        $scope.create = function () {
            if ($scope.register.$valid) {
                $http({
                    url: jsRoutes.controllers.Courses.create().url,
                    data: $scope.item,
                    method: 'post'
                }).success(function (data, status, headers, config) {
                    alertService.alertInfo("A new course has been created successfully ");
                }).
                    error(function (data, status, headers, config) {
                        alertService.alertDanger(data.message);
                    });
            } else {
                console.log("not valid")
            }

        };

        $scope.reload = function () {
            $scope.tableParams.reload()

        }

        $scope.deletecourse = function (id) {
            jsRoutes.controllers.Courses.deletecourse(id).ajax({
                    dataType: 'json',
                    type: "delete",
                    success: function (response) {
                        $timeout(function () {
                            $scope.reload();
                        }, 1000)

                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log("error");
                    }
                }
            )
        };


        $scope.tableParams = new ngTableParams({
            page: 1,            // show first page
            count: 10           // count per page
        }, {
            total: 0, // length of data
            getData: function ($defer, params) {
                jsRoutes.controllers.Courses.search($scope.keywords, params.page(), params.count()).ajax({
                        dataType: 'json',
                        success: function (response) {
                            params.total(response.total)
                            $defer.resolve(response.data);
                        }
                    }
                )
            }
        });


        $scope.toggleMin = function () {
            $scope.minDate = $scope.minDate ? null : new Date();
        };
        $scope.toggleMin();

        $scope.open = function ($event, which) {
            $event.preventDefault();
            $event.stopPropagation();
            switch (which) {
                case 0 :
                    $scope.start = true;
                    break;
                case 1 :
                    $scope.finish = true;
                    break;
            }
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.formats = ['dd-MM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        $scope.format = 'dd-MM-yyyy';


    };

    CourseCtrl.$inject = ["$scope", "$location", "$timeout", "$http", "ngTableParams", "alertService", "$cookieStore"];

    return {
        CourseCtrl: CourseCtrl
    };


});



