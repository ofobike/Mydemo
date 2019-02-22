app.service("loginService",function($http){
    //读取后台登录人的姓名
    this.loginName = function(){
        return $http.get('../login/name.do');
    }
});