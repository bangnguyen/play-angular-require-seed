/**
 * The app module, as both AngularJS as well as RequireJS module.
 * Splitting an app in several Angular modules serves no real purpose in Angular 1.0/1.1.
 * (Hopefully this will change in the near future.)
 * Splitting it into several RequireJS modules allows async loading. We cannot take full advantage
 * of RequireJS and lazy-load stuff because the angular modules have their own dependency system.
 * Ideally the scripts you load will be modules that are defined by calling define()
 */
define(["angular","utils","home", "user", "dashboard","profile","./common/moment.min","./common/ng-table"], function(angular) {
  "use strict";

  // We must already declare most dependencies here (except for common), or the submodules' routes
  // will not be resolved
  return angular.module("app", ["utils","home", "user", "dashboard","profile","ngTable","ui.bootstrap","ui.bootstrap.tpls"]);
});

