garbagePostApp.service("garbagePostService", function ($http) {
    this.loadGarbagePost = function(){
        var response = $http({ method: "get", url: "http://192.168.8.103:9090/api/getAllGarbagePoints/all",
});
        return response;
    }

    this.loadGarbagePostById = function(garbageId){
        var response = $http({ method: "get", url: "../ServerAPI/garbagePost.php", params: { post_id: garbageId } });
        return response;
    }

    this.saveRoute = function(routeName, lat, Lng){
        var response = $http({ method: "post", url: "../ServerAPI/saveGarbagePost.php", 
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}, 
        data: { routeName: routeName, lat:lat, long: Lng } });

        return response;
    }
});