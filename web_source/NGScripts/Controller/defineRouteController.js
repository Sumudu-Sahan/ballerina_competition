defineRouteApp.controller("defineRouteController", function($scope, defineRouteService){
    loadRoutes();

    let map;
    let marker;

    let myLatlng = new google.maps.LatLng(6.930841, 79.847104);
    let geocoder = new google.maps.Geocoder();

    $scope.markerList = [];

    var myOptions = {
        zoom: 15,
        center: myLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById("map"), myOptions);

    marker = new google.maps.Marker({
        //draggable: true,
        //position: myLatlng,
        map: map,
        title: "Your location"
    });

    var createMarker = function (mark){
        var marker = new google.maps.Marker({
            map: map,
            position: new google.maps.LatLng(mark.lat, mark.long),
            title: "Point "+mark.location
        });      
                  
    }

    google.maps.event.addListener(map, 'rightclick', function(event) {
        var locName = getAddress(event.latLng);

        $scope.markerList.push({ location : locName, lat : event.latLng.lat(), long : event.latLng.lng() });
        loadLocations();
        $scope.$apply();
        //marker.setPosition(event.latLng);
    });

    function loadLocations(){
        for (i = 0; i < $scope.markerList.length; i++){
            createMarker($scope.markerList[i]);
        }
    };

    function setMapOnAll(map) {
        for (var i = 0; i < $scope.markerList.length; i++) {
          $scope.markerList[i].setMap(map);
        }
    }

    $scope.removeLocation = function(rId){
        setMapOnAll(null);
        $scope.markerList.splice($scope.markerList.indexOf(rId), 1 );
        loadLocations();
        $scope.$apply();
    }

    var getAddress = function(latLng){
        geocoder.geocode({
            'latLng': latLng
        }, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                    return results[0].formatted_address;
                }
            }
        });
    }

    function loadRoutes(){
        var RoteRes = defineRouteService.loadRoutes();
        RoteRes.then(function(resp){
            $scope.RouteNames = resp.data;
        }, function(error){
            alert("Error");
        });
    }

    $scope.isEnabled = false;
    $scope.cmbHide = true;
    $scope.txtHide = false;

    $scope.drpTypeChange = function(){
        if($scope.drpType == 1){
           $scope.cmbHide = true;
           $scope.txtHide = false; 
           $scope.isEnabled = false;
        }
        else{
            $scope.cmbHide = false;
            $scope.txtHide = true;
            $scope.isEnabled = true;            
        }
    }

    $scope.saveRecord = function(){
        
        let routeId = $scope.drpType == 1 ? 0: $scope.drpRoute.route_id;

        var res = defineRouteService.saveRoute($scope.drpType, $scope.RouteName, $scope.Description, $scope.markerList, routeId);
        res.then(function(resp){
            alert(resp.data);
        }, function(error){
            alert("Something went wrong");
        });
    }

    $scope.drpRouteChange = function(route){
        var res = defineRouteService.loadRouteById(route.route_id);
        res.then(function(resp){

            var result = resp.data;
            $scope.markerList = [];
            for (i = 0; i < result.length; i++){
                $scope.markerList.push({ location : result[i].garbage_collecting_point_description, lat : result[i].garbage_collecting_point_lat, long : result[i].garbage_collecting_point_lon });

                if(i != 0){
                    $scope.Description = result[0].garbage_collecting_point_description;
                }
            }

            loadLocations();
            $scope.$apply();

        }, function(error){
            alert(error);
        })
    }

    //google.maps.event.addDomListener(window, "load", initialize());
});
