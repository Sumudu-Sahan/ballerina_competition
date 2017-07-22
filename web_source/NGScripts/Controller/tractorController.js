tractorApp.controller('tractorController', function ($scope, tractorService, ngDialog) {
    loadTractors();

    function loadTractors(){
        var tractorList= tractorService.getAllTractors();
        tractorList.then(function(resp){
            $scope.tractors = resp.data;
        }, function(error){
            alert("Error");
        });
    }

    $scope.sort = function (keyname) {
        $scope.sortKey = keyname;   //set the sortKey to the param passed
        $scope.reverse = !$scope.reverse; //if true make it false and vice versa
    }

    $scope.viewTractorDetails = function(tractorId){
        $scope.Dialog = ngDialog.open({
            template: 'tractorForm',
            width: '800px',
            scope: $scope
        });
        
        var response = tractorService.getTractorById(tractorId);
        response.then(function(res){
            $scope.tractor_number = res.data[0].tractor_number;
            $scope.vehicle_image = res.data[0].tractor_image;
        }, function(){
            alert("Something went wrong");
        });
    }

    $scope.closeDialog = function () {
        $scope.Dialog.close()
    }
});