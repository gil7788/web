var app = angular.module('mainUX' , []);


app.controller('TabController' , function(){
  this.primaryTab = 0;
  this.secondaryTab = 0;
  this.finalTab = this.primaryTab + this.secondaryTab;

  this.setPrimaryTab = function(finalValue){
    this.primaryTab = finalValue;
    this.finalTab = this.primaryTab + this.secondaryTab;
  };

  this.setSecondaryTab = function(finalValue){
    this.secondaryTab = finalValue;
    this.finalTab = this.primaryTab + this.secondaryTab;
  };

  this.isSet = function(v){
    for(i=0;i<v.length;i++){
      if (this.finalTab == v[i]){
        return true;
      }
    }
      return false;
    };
});
