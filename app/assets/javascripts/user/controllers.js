/**
 * User controllers.
 */
define(["angular"], function(angular) {
  "use strict";

  var LoginCtrl = function($scope, $location,$cookieStore, userService) {
    $scope.credentials = {};

    $scope.login = function(credentials) {
      userService.loginUser(credentials).then(function(user) {
        $cookieStore.put('username',user.username);
        //console.log("user "+user.username)
        $location.path("/dashboard");
      });
    };
  };



  LoginCtrl.$inject = ["$scope", "$location", "$cookieStore","userService"];

  return {
    LoginCtrl: LoginCtrl
  };

});
