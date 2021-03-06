/**
 * course package module.
 * Manages all sub-modules so other RequireJS modules only have to import the package.
 */
define(["angular", "./routes"], function(angular) {
    "use strict";
    return angular.module("course", ["ngCookies", "ngRoute", "course.routes"]);
});


