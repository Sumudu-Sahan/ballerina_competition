app.controller('loginController', function ($scope, LoginService, $window) {
    $scope.validateLogin = function () {
        var LoginStat = LoginService.checkLogin($scope.UserName, $scope.Password);

        LoginStat.then(function (stat) {
            if (stat.data == "true") {
                $window.location.assign('../Views/landing.html');
            }
            else {
                alert("Invalid login");
            }
        }, function () {        
            alert("Something went wrong");
        });
    }
});