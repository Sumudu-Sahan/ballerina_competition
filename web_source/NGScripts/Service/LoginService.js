app.service("LoginService", function ($http) {
    this.checkLogin = function (Uname, password) {
        var response = $http({
            method: "post",
            url: "../ServerAPI/login.php",
            data: {
                userName: Uname,
                Password: password
            }
        });
        return response;
    }
});