/**
 * User controllers.
 */
define(["angular", "jsRoutes"], function (angular, jsRoutes) {
    "use strict";

    var ProfileCtrl = function ($scope, $route, $location, $timeout, $http, ngTableParams, alertService) {
        $scope.location = $location
        $scope.item = {}
        $scope.cat = 'Cat Minky';

        var searchMode = false
        var itemDelete = undefined
        var currentItemId = $route.current.params.id
        $scope.positions = [
            "Student", "Teacher", "Management", "Other"
        ]

        $scope.courses = []

        $scope.allcourses = []


        jsRoutes.controllers.Search.searchAllOpenCourses().ajax({
                dataType: 'json',
                success: function (response) {
                    $scope.allcourses = response.data
                }
            }
        )


        if (currentItemId != undefined) {
            jsRoutes.controllers.Profiles.get(currentItemId).ajax({
                    dataType: 'json',
                    success: function (response) {
                        $timeout(function () {
                            $scope.item = response;
                            $scope.item.birthday = new Date(response.birthday);
                            $scope.item.courses = []
                        })
                    }
                }
            )
        }

        $scope.mode = function () {
            var mode = ""
            if ($location.path().indexOf('edit') != -1)
                mode = "edit"
            else if ($location.path().indexOf('view') != -1)
                mode = "view"
            else mode = "create"
            return mode
        }

        function beforeSend(data) {
            var newObject = JSON.parse(JSON.stringify(data));
            newObject.birthday = Date.parse(data.birthday)
            var coursesId = []
            for (var i = 0; i < newObject.courses.length; i++)
                coursesId.push(newObject.courses[i].id)
            newObject.phone = data.phone.toString()
            newObject.coursesId = coursesId
            return newObject
        }

        $scope.addCourse = function (item) {
            if ($scope.item.courses.indexOf(item) == -1)
                $scope.item.courses.push(item)

        }

        $scope.editCourse = function (course) {
            $scope.showEditCourse = true;
        }


        $scope.edit = function (item) {
            $location.path("/profiles/" + item.id + "/edit")
        }

        $scope.create = function (item) {
            if ($scope.registerProfile.$valid) {
                $http({
                    url: jsRoutes.controllers.Profiles.create().url,
                    data: beforeSend(item),
                    method: 'post'
                }).success(function (data, status, headers, config) {
                    alertService.alertInfo("A new profile has been created successfully ");
                }).
                    error(function (data, status, headers, config) {
                        $scope.errors = data.errors
                        alertService.alertDanger(data.errors.all);
                    });
            } else {
                console.log("not valid")
            }

        };


        $scope.update = function (item) {
            if ($scope.registerProfile.$valid) {
                $http({
                    url: jsRoutes.controllers.Profiles.update(item.id).url,
                    data: beforeSend(item),
                    method: "put"
                }).success(function (data, status, headers, config) {
                    alertService.alertInfo("Profile has been updated successfully ");
                }).
                    error(function (data, status, headers, config) {
                        $scope.errors = data.errors
                        alertService.alertDanger(data.errors.all);

                    });
            } else {
                console.log("not valid")
            }

            if($scope.registerCourse.$valid){
                $http({
                    url: jsRoutes.controllers.CourseStudents.create(item.id).url,
                    data: beforeSend(item),
                    method: "put"
                }).success(function (data, status, headers, config) {
                    alertService.alertInfo("Profile has been updated successfully ");
                }).
                    error(function (data, status, headers, config) {
                        $scope.errors = data.errors
                        alertService.alertDanger(data.errors.all);

                    });
                console.log("regiter Course Valid")
            }
            else {
                console.log("regiter Course not valid")
            }





        };



        $scope.reload = function () {
            $scope.tableParams.reload()

        }

        $scope.deleteObject = function (item) {
            jsRoutes.controllers.Profiles.deleteObject(item.id).ajax({
                    dataType: 'json',
                    type: "deleteItem",
                    beforeSend: function () {
                        itemDelete = item;
                    },
                    success: function (response) {
                        $scope.reload();
                        itemDelete = undefined;
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log("error");
                        itemDelete = undefined;
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
                if (itemDelete != undefined) {
                    var indexItem = $scope.tableParams.data.indexOf(itemDelete)
                    $scope.tableParams.data.splice(indexItem, 1)
                    params.total($scope.tableParams.data.length)
                    $defer.resolve($scope.tableParams.data)
                }
                else {
                    jsRoutes.controllers.Search.searchProfilesByKeyword($scope.keywords, params.page(), params.count()).ajax({
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

        $scope.today = function () {
            $scope.dt = new Date();
        };
        $scope.today();

        $scope.clear = function () {
            $scope.dt = null;
        };


        $scope.open = function ($event) {
            if ($scope.mode() != 'view') {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.opened = true;
            }
        };

        $scope.formats = ['dd-MM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        $scope.format = $scope.formats[0];
        $scope.dateOptions = {
            format: $scope.formats[0]
        };


    };

    ProfileCtrl.$inject = ["$scope", "$route", "$location", "$timeout", "$http", "ngTableParams", "alertService"];

    return {
        ProfileCtrl: ProfileCtrl
    };


});



