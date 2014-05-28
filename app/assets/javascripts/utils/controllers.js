/**
 * User controllers.
 */
define(["angular"], function(angular) {
    "use strict";


    var FileController = function($scope, $location, playRoutes) {
        $scope.uploadUrl = playRoutes.controllers.Application.uploadFile().url;


    };

    FileController.$inject = ["$scope", "$location", "playRoutes"];

    return {
        FileController: FileController
    };

});
