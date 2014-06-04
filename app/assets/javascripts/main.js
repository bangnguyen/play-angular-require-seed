(function(requirejs) {
  "use strict";

  // -- DEV RequireJS config --
  requirejs.config({
    // Packages = top-level folders; loads a contained file named "main.js"
    packages: ["common", "home", "user", "dashboard","profile","utils"],
    shim: {
      "jsRoutes" : {
        deps : [],
        // it's not a RequireJS module, so we have to tell it what var is returned
        exports : "jsRoutes"
      },
        "bootstrap-datetimepicker" : {
            deps : ['jquery']
        }
    },
    paths: {
      "jsRoutes" : "/jsroutes"
    }
  });

  requirejs.onError = function(err) {
    console.log(err);
  };

  // Load the app. This is kept minimal so it doesn't need much updating.
  require(["angular", "angular-cookies", "angular-route", "jquery", "bootstrap", "./app","./others"],
    function(angular) {
     //this function is called when angular, angular-cookies, angular-route are all loaded
     //if angular, angular-cookies call define, this functions is not fired until
     // depend
      angular.bootstrap(document, ["app"]);
        $(document).ready(function() {
            $('.js-activated').dropdownHover().dropdown();
        });
    }
  );
})(requirejs);


