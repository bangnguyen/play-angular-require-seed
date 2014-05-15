/**
 * Configure routes of profile module.
 */
define(["angular", "./controllers", "common"], function(angular, controllers) {
    var mod = angular.module("profile.routes", ["common"]);
    mod.config(["$routeProvider", function($routeProvider) {
        $routeProvider
            .when("/createProfile", {templateUrl:"/assets/templates/profile/create.html", controller:controllers.ProfileCtrl});
    }]);
    return mod;
});
