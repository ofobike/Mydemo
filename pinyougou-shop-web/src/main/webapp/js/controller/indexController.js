app.controller("indexController",function($scope,loginService){
     //控制器定义方法
    $scope.showLoginName=function () {
        //调用服务层的方法
        loginService.showLoginName().success(function(response){
            if ("anonymousUser"==response.loginName){
                //跳转到首页
                location.href="../shoplogin.html"
            }
            //定义一个变量接受后台返回的数据
            $scope.loginName=response.loginName;
        });
    }
});