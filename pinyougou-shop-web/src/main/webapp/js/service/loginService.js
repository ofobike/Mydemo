app.service("loginService",function($http){
    //定义方法
    this.showLoginName=function(){
        //调用后台的方法
        return $http.get('../login/name.do');
    }
});