/**
 * User service, exposes user model to the rest of the app.
 */
define(["angular", "common"], function(angular) {
  "use strict";

  var mod = angular.module("user.services", ["common"]);
  mod.factory("userService", ["$http", "$q", "playRoutes","$cookieStore",function($http, $q, playRoutes,$cookieStore) {
    var user, token;
    return {
     /* loginUser : function(credentials) {
        return playRoutes.controllers.Application.login().post(credentials).then(function(response) {
          // return promise so we can chain easily
          token = response.data.token;
          // in a real app we could use the token to fetch the user data
          return playRoutes.controllers.Users.user(3).get();
        }).then(function(response) {
          user = response.data; // Extract user data from user() request
          user.email = credentials.email;
          return user;
        });
      }*/
    loginUser : function(credentials) {
        return playRoutes.controllers.Application.login().post(credentials).then(function(response) {
            // return promise so we can chain easily
            // in a real app we could use the token to fetch the user data
            user = response.data
            return user
        })
    }
        ,
      logout : function() {
      // Logout on server in a real app
      $cookieStore.remove('username');
      user = undefined;
      },
      getUser : function() {
        //console.log("getUser "+ $cookieStore.get('username'))
        var username = $cookieStore.get('username')
        if(username!=undefined)
            user = {username : username}
        else
           user = undefined
        return user;
      }
    };
  }]);
  /**
   * Add this object to a route definition to only allow resolving the route if the user is
   * logged in. This also adds the contents of the objects as a dependency of the controller.
   */
  mod.constant("userResolve", {
    user: ["$q", "userService", function($q, userService) {
      var deferred = $q.defer();
      var user = userService.getUser();
      if (user) {
        deferred.resolve(user);
      } else {
        deferred.reject();
      }
      return deferred.promise;
    }]
  });
  /**
   * If the current route does not resolve, go back to the start page.
   */
  var handleRouteError = function($rootScope, $location) {
    $rootScope.$on("$routeChangeError", function(e, next, current) {
      $location.path("/");
    });
  };
  handleRouteError.$inject = ["$rootScope", "$location"];
  mod.run(handleRouteError);
  return mod;
});
