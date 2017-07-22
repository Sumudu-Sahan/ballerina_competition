tractorApp.service("tractorService", function ($http) {
    this.getAllTractors = function(){
        var response = $http({ method: "get", url: "../ServerAPI/getTractors.php", params: { tractor_id: 0 } });
        return response;
    }

    this.getTractorById = function(tractorId){
        var response = $http({ method: "get", url: "../ServerAPI/getTractors.php", params: { tractor_id: tractorId } });
        return response;
    }
    
});