//品牌的控制层
app.controller("brandController", function ($scope, $controller,brandService) {

    //控制层继承控制层
    $controller('baseController', {$scope: $scope});//继承


    //定义查询的方法
    $scope.findAll = function () {
        //调用服务层的方法
        brandService.findAll().success(function (response) {
            //把后台返回的数据绑定变量ing
            $scope.list = response;
        });
    }

    //分页的方法
    $scope.findPage=function(pageNum,pageSize){
        brandService.findPage(pageNum,pageSize).success(function(response){
              $scope.list=response.rows;//每页显示的数据
            $scope.paginationConf.totalItems=response.total;//总记录数
        });
    }
    //新增品牌
    $scope.save=function(){
        /* var methodName='add';
        //对页面的中id进行判断
        if($scope.entity.id!=null){
            //那么代表就是修改
            methodName='update';
        } */
        var object = null;
        if($scope.entity.id!=null){
            object=brandService.update($scope.entity);
        }else{
            object=brandService.add($scope.entity);
        }
        object.success(function(response){
            //取返回的数据
            if(response.success){
                //成功
                $scope.reloadList();//刷新页面
            }else{
                //失败
                alert(response.message);
            }
        });
    }
    //根据id查询数据
    $scope.findOne=function(id){
        brandService.findOne(id).success(function(response){
            //双向数据绑定显示数据
            $scope.entity=response;
        });
    }
    //定义删除的方法
    $scope.dele=function(){
        //向后台查询数据
        brandService.dele($scope.selectIds).success(function(response){
            if(response.success){
                alert(response.message);
                //删除成功刷新
                //成功
                $scope.reloadList();//刷新页面
                //把集合数据为0
                $scope.selectIds=[];
            }else{
                alert(response.message);
            }
        });
    }
    //初始化
    $scope.searchEntity={}
    //条件查询
    //$http.post('../brand/search.do?pageNum='+pageNum+'&pageSize='+pageSize,$scope.searchEntity)
    $scope.search=function(pageNum,pageSize){
        brandService.search(pageNum,pageSize,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;//显示当前页的数据
                $scope.paginationConf.totalItems=response.total;//总记录数
            });
    }
    //定义一个方法当用户输入之后那么清空输入框的内容
    $scope.clearInput=function(){
        $scope.searchEntity={}
    }
});