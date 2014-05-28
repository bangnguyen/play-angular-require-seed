/**
 * User controllers.
 */
define(["angular"], function(angular) {
    "use strict";

    var ProfileCtrl = function($scope, $location, playRoutes,ngTableParams,$timeout) {



        $scope.create = function(profile) {
           console.log(profile);
        };



        $scope.tableParams = new ngTableParams({
            page: 1,            // show first page
            count: 5           // count per page
        }, {
            total: 0, // length of data
            getData: function($defer, params) {
                 playRoutes.controllers.Application.getProfiles().get().then(function(response) {
                         params.total(36)
                         $defer.resolve(response.data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                 })
            }
        });

        $scope.editId = -1;

        $scope.setEditId =  function(pid) {
            $scope.editId = pid;
            console.log(pid);
        }


    };

    ProfileCtrl.$inject = ["$scope", "$location", "playRoutes","ngTableParams","$timeout"];

    return {
        ProfileCtrl: ProfileCtrl
    };

});
