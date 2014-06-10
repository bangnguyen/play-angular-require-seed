/**
 * User controllers.
 */
define(["angular","jsRoutes"], function(angular,jsRoutes) {
    "use strict";

    var ProfileCtrl = function($scope,$location,$timeout,$http,ngTableParams,alertService) {

        var searchMode = false



        $scope.create = function(profile) {
            if($scope.registerProfile.$valid){
                $http({
                    url: jsRoutes.controllers.Profiles.create().url,
                    data: profile,
                    method: 'post'
                }).success(function(data, status, headers, config) {
                    alertService.alertInfo("A new profile has been created successfully ");
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

        $scope.deleteProfile = function(id) {
            jsRoutes.controllers.Profiles.deleteProfile(id).ajax({
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
                jsRoutes.controllers.Search.searchProfilesByKeyword($scope.keywords,params.page(),params.count()).ajax({
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

    ProfileCtrl.$inject = ["$scope", "$location","$timeout","$http","ngTableParams","alertService"];

    return {
        ProfileCtrl: ProfileCtrl
    };





});



