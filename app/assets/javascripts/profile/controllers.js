/**
 * User controllers.
 */
define(["angular","jsRoutes"], function(angular,jsRoutes) {
    "use strict";

    var ProfileCtrl = function($scope, $location,$timeout,ngTableParams) {

        var searchMode = false





        $scope.create = function(profile) {
           console.log(profile);
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
                jsRoutes.controllers.Profiles.search($scope.keywords,params.page(),params.count()).ajax({
                        dataType: 'json',
                        success: function (response) {
                            params.total(response.total)
                            $defer.resolve(response.data);
                        }
                    }
                )
            }
        });





    };

    ProfileCtrl.$inject = ["$scope", "$location","$timeout","ngTableParams"];

    return {
        ProfileCtrl: ProfileCtrl
    };

});
