assignDriverApp.controller("assignDriverController", function($scope, assignDriverService){
    loadRoutes();
    loadDrivers();
    loadTractors();

    function loadRoutes(){
        var RoteRes = assignDriverService.loadRoutes();
        RoteRes.then(function(resp){
            $scope.RouteNames = resp.data;
        }, function(error){
            alert("Something went wrong");
        });
    }

    function loadDrivers(){
        var DriverResults = assignDriverService.loadDrivers();
        DriverResults.then(function(resp){
            $scope.Drivers = resp.data;
        }, function(){
            alert("Something went wrong");
        });
    }

    function loadTractors (){
        var TractorResults = assignDriverService.loadTractors();
        TractorResults.then(function(resp){
            $scope.Tractors = resp.data;
        }, function(){
            alert("Something went wrong");
        });
    }

    $scope.saveRecord = function(){
        var response  = assignDriverService.saveRecord($scope.drpRoute.route_id, $scope.drpDriver.user_id, $scope.drpTractor.tractor_id);
        response.then(function(res){
            alert(res);
        }, function(){
            alert("something went wrong");
        });
    }

});
