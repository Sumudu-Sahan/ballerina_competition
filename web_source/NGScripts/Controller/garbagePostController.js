garbagePostApp.controller("garbagePostController", function($scope, garbagePostService, ngDialog){
    loadGarbagePost();

    function loadGarbagePost(){
        var garbagePostRes = garbagePostService.loadGarbagePost();
        garbagePostRes.then(function(resp){
            $scope.GarbagePostList = resp.data;
        }, function(error){
            alert("Error");
        });
    }

    let lettitude;
    let longitude;

    $scope.viewPostRequest = function(postId){
        $scope.Dialog = ngDialog.open({
            template: 'requestForm',
            width: '900px',
            scope: $scope
        });
        
        var response = garbagePostService.loadGarbagePostById(postId);
        response.then(function(res){
            var formData = res.data[0];
            $scope.post_title = formData.garbage_post_title;
            $scope.post_description = formData.garbage_post_description;
            $scope.post_image = formData.garbage_post_image;
            lettitude = formData.garbage_post_lat;
            longitude = formData.garbage_post_lon;

            if(lettitude != null && longitude != null){
                loadMap(formData.garbage_post_lat, formData.garbage_post_lon);
            }

        }, function(){
            alert("Something went wrong");
        });
    }

    $scope.closeDialog = function () {
        $scope.Dialog.close()
    }

    function loadMap(lat, lng){
        let map;
        let marker;

        let myLatlng = new google.maps.LatLng(lat, lng);

        $scope.markerList = [];

        var myOptions = {
            zoom: 15,
            center: myLatlng,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };

        map = new google.maps.Map(document.getElementById("map"), myOptions);

        marker = new google.maps.Marker({
            position: myLatlng,
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

        $scope.isHide = true;

        $scope.statusChange = function(id){
            if(id == 1){
                $scope.isHide = false;
            }
            else{
                $scope.isHide = true;
            }
        }

        $scope.addToRoute = function(){
            var res= garbagePostService.saveRoute($scope.RouteName, lettitude, longitude);
            res.then(function(resp){
                alert(resp.data);
            }, function(){
                alert("Something went wrong");
            });
        }

    }
});
