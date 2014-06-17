/**
 * Configure routes of profile module.
 */
define(["angular", "./controllers", "common"], function(angular, controllers) {
    var mod = angular.module("profile.routes", ["common"]);
    mod.config(["$routeProvider", function($routeProvider) {
        $routeProvider
            .when("/createProfile", {templateUrl:"/assets/templates/profile/create.html", controller:controllers.ProfileCtrl})
            .when("/profiles", {templateUrl:"/assets/templates/profile/list.html", controller:controllers.ProfileCtrl})
            .when("/profiles/:id/edit", {templateUrl:"/assets/templates/profile/create.html", controller:controllers.ProfileCtrl})
            .when("/profiles/:id/view", {templateUrl:"/assets/templates/profile/create.html", controller:controllers.ProfileCtrl})
    }]);
    return mod;
});
