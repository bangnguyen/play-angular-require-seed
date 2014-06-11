/** Common helpers */
define(["angular"], function(angular) {
  "use strict";

  var mod = angular.module("common.helper", []);
  mod.service("helper", function() {
    return {
      sayHi: function() {
        return "hi";
      }
    };
  });

    mod.service("alertService", function($timeout) {

        function timeOutAlert(){

            $timeout(function(){
                $("#alert-container").hide();
            },3000)

        }
        this.alertDanger = function(message){
            if(!$("#alert-container").is(':visible')){
                $("#alert-container").html("<div class='alert alert-danger info-bar container'>"+
                    message+"</div>");
                $("#alert-container").show()
                timeOutAlert()
            }
        };

        this.alertInfo = function(message){
            if(!$("#alert-container").is(':visible')){
                $("#alert-container").html("<div class='alert alert-info info-bar container'>"+
                    message+"</div>");
                $("#alert-container").show()
                timeOutAlert()
            }
        };

        this.alertWarning = function(message){
            if(!$("#alert-container").is(':visible')){
                $("#alert-container").html("<div class='alert alert-warning info-bar container'>"+
                    message+"</div>");
                $("#alert-container").show()
                timeOutAlert()
            }
        };


    });
  return mod;
});
