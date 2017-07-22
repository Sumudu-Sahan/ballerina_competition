defineRouteApp.service("defineRouteService", function ($http) {
    this.saveRoute = function(type, routeName, desc, routes, routeId){
        var response = $http({ method: "post", url: "../ServerAPI/saveRoute.php", 
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}, 
        data: { type: type, routeName: routeName, desc: desc, routes:routes, routeId: routeId } });

        return response;
    }

    this.loadRoutes = function(){
        var response = $http({ method: "get", url: "../ServerAPI/getRoutes.php"});
        return response;
    }

    this.loadRouteById = function(rId){
        var response = $http({ method: "get", url: "../ServerAPI/getRoutes.php",
        params:{route_id: rId}});
        return response;
    }
});