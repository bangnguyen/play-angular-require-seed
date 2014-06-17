/**
 * User controllers.
 */
define(["angular", "jsRoutes"], function (angular, jsRoutes) {
    "use strict";

    var CourseCtrl = function ($scope,$route, $location, $timeout, $http, ngTableParams, alertService, $cookieStore) {

        console.log("routes params " + $route.current.params)

        var courses = []
        var itemDelete = undefined
        var currentItemId = $route.current.params.id


        $scope.teachers = []

        if(currentItemId !=undefined ){
            jsRoutes.controllers.Courses.get(currentItemId).ajax({
                    dataType: 'json',
                    success: function (response) {
                        $timeout(function(){
                            $scope.item = response ;
                            $scope.item.start = new Date(response.start);
                            $scope.item.finish = new Date(response.finish);
                        })
                    }
                }
            )
        }




        jsRoutes.controllers.Search.getAllTeachers().ajax({
                dataType: 'json',
                success: function (response) {
                    $timeout(function () {
                        $scope.teachers = response.data
                    })

                }
            }
        )

        $scope.levels = [
            "Beginner", "Elem", "PostElem", "PreInter", "PostInter", "Advance"
        ]

        function beforeSend(data) {
            var newObject = JSON.parse(JSON.stringify(data));
            newObject.start = Date.parse(data.start)
            newObject.finish = Date.parse(data.finish)
            return newObject
        }

        //$cookieStore.remove("course")
       /* if ($cookieStore.get("course") != undefined) {
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
                comment: "",
                isOpen : true
            }
        }*/


       /* $scope.$watch("item", function () {
            $cookieStore.put("course", $scope.item)
        }, true)*/


        $scope.create = function (item) {
            if ($scope.register.$valid) {
                $http({
                    url: jsRoutes.controllers.Courses.create().url,
                    data: beforeSend(item),
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

        $scope.update = function (item) {
            if ($scope.register.$valid) {
                $http({
                    url: jsRoutes.controllers.Courses.update(item.id).url,
                    data: beforeSend(item),
                    method: 'put'
                }).success(function (data, status, headers, config) {
                    alertService.alertInfo("Profile has been updated successfully ");
                }).
                    error(function (data, status, headers, config) {
                        $scope.errors= data.errors
                        alertService.alertDanger(data.errors.all);
                    });
            } else {
                console.log("not valid")
            }

        };

        $scope.save = function () {
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

        $scope.deleteObject = function (item) {

            jsRoutes.controllers.Courses.deleteObject(item.id).ajax({
                    dataType: 'json',
                    type: "delete",
                    beforeSend : function(){
                        itemDelete = item ;
                    } ,
                    success: function (response) {
                        $scope.reload();
                        itemDelete = undefined ;
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        itemDelete = undefined ;
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
                if(itemDelete != undefined ) {
                    var indexItem = $scope.tableParams.data.indexOf(itemDelete)
                    $scope.tableParams.data.splice(indexItem,1)
                    params.total($scope.tableParams.data.length)
                    $defer.resolve($scope.tableParams.data)
                }
                else {
                    jsRoutes.controllers.Search.getAllCourses().ajax({
                            dataType: 'json',
                            success: function (response) {
                                params.total(response.total)
                                $defer.resolve(response.data);
                            }
                        }
                    )
                }
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

    CourseCtrl.$inject = ["$scope", "$route", "$location", "$timeout", "$http", "ngTableParams", "alertService", "$cookieStore"];

    return {
        CourseCtrl: CourseCtrl
    };


});



