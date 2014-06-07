/**
 * Configure routes of course module.
 */
define(["angular", "./controllers", "common"], function(angular, controllers) {
    var mod = angular.module("course.routes", ["common"]);
    mod.config(["$routeProvider", function($routeProvider) {
        $routeProvider
            .when("/createCourse", {templateUrl:"/assets/templates/course/create.html", controller:controllers.CourseCtrl})
            .when("/courses", {templateUrl:"/assets/templates/course/list.html", controller:controllers.CourseCtrl});
    }]);
    return mod;
});
