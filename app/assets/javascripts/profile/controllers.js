/**
 * User controllers.
 */
define(["angular"], function(angular) {
    "use strict";

    var ProfileCtrl = function($scope, $location) {
        $scope.profile = {};

        $scope.create = function(profile) {
           console.log(profile);
        };
    };

    ProfileCtrl.$inject = ["$scope", "$location"];

    return {
        ProfileCtrl: ProfileCtrl
    };

});
