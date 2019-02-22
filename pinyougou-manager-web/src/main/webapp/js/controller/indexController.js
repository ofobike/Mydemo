app.controller("indexController",function($scope,loginService){
    //定义方法
    $scope.showLoginName=function(){
        //调用服务层的方法
        loginService.loginName().success(function(response){
            if ("anonymousUser"==response.loginName){
                 //跳转到首页
                location.href="../login.html"
            }
             //定义变量接受返回的参数
             $scope.loginName=response.loginName;
        });
    }
});