/**
 * Common functionality.
 */
define(["angular","jquery", "./services/helper", "./services/playRoutes", "./filters", "./directives/example",
    "./bootstrap-hover-dropdown.min"
    ],
    function(angular) {
  "use strict";
        $(function(){
            setTimeout(function(){$('.dropdown-toggle').dropdownHover();}, 500);
        });
  return angular.module("common", ["common.helper", "common.playRoutes", "common.filters",
    "common.directives.example"]);
});




