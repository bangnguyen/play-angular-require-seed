/**
 * User controllers.
 */
define(["angular","jsRoutes"], function(angular,jsRoutes) {
    "use strict";

    var CourseCtrl = function($scope,$location,$timeout,$http,ngTableParams,alertService) {

        var searchMode = false



        $scope.create = function(course) {
            if($scope.registerCourse.$valid){
                $http({
                    url: jsRoutes.controllers.Courses.create().url,
                    data: course,
                    method: 'post'
                }).success(function(data, status, headers, config) {
                    alertService.alertInfo("A new course has been created successfully ");
                  }).
                    error(function(data, status, headers, config) {
                       alertService.alertDanger(data.message);
                    });
            }  else {
                console.log("not valid")
            }

        };

        $scope.reload = function(){
            $scope.tableParams.reload()

        }

        $scope.deletecourse = function(id) {
            jsRoutes.controllers.Courses.deletecourse(id).ajax({
                    dataType : 'json',
                    type : "delete",
                    success:function(response){
                        $timeout(function(){
                            $scope.reload();
                        },1000)

                      },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.log("error");
                    }
                }
            )
        } ;


        $scope.tableParams = new ngTableParams({
            page: 1,            // show first page
            count: 10           // count per page
        }, {
            total: 0, // length of data
            getData: function ($defer, params) {
                jsRoutes.controllers.Courses.search($scope.keywords,params.page(),params.count()).ajax({
                        dataType: 'json',
                        success: function (response) {
                            params.total(response.total)
                            $defer.resolve(response.data);
                        }
                    }
                )
            }
        });

        $scope.today = function() {
            $scope.dt = new Date();
        };
        $scope.today();

        $scope.clear = function () {
            $scope.dt = null;
        };


        $scope.toggleMin = function() {
            $scope.minDate = $scope.minDate ? null : new Date();
        };
        $scope.toggleMin();

        $scope.open = function($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $scope.opened = true;
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.initDate = new Date('2016-15-20');
        $scope.formats = ['dd-MM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        $scope.format = $scope.formats[0];





    };

    CourseCtrl.$inject = ["$scope", "$location","$timeout","$http","ngTableParams","alertService"];

    return {
        CourseCtrl: CourseCtrl
    };





});



