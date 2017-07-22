assignDriverApp.service("assignDriverService", function ($http) {
    this.loadRoutes = function(){
        var response = $http({ method: "get", url: "../ServerAPI/getRoutes.php"});
        return response;
    }

    this.loadDrivers = function(){
        var response = $http({ method: "get", url: "../ServerAPI/getDriver.php"});
        return response;
    }

    this.loadTractors = function(){
        var response = $http({ method: "get", url: "../ServerAPI/getTractors.php"});
        return response;
    }

    this.saveRecord = function(routeId, driverId, tractorId){
        var response = $http({ method: "post", url: "../ServerAPI/saveDriverAssign.php", 
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}, 
        data: { route_id: routeId, driver_id: driverId, tractor_id: tractorId } });

        return response;
    }

});