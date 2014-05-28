/**
 * Configure routes of profile module.
 */
define(["angular", "./controllers", "common"], function(angular, controllers) {
    var mod = angular.module("utils.routes", ["common"]);
    mod.config(["$routeProvider", function($routeProvider) {
        $routeProvider
            .when("/uploadFile", {templateUrl:"/assets/templates/utils/uploadFile.html", controller:controllers.FileController});
    }]);
    return mod;
});
