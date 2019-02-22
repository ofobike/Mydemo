 //控制层 
app.controller('sellerController' ,function($scope,$controller ,sellerService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		sellerService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	};
	
	//分页
	$scope.findPage=function(page,rows){			
		sellerService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};
	
	//查询实体 
	$scope.findOne=function(id){				
		sellerService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	};
	
	//保存 
	$scope.save=function(){
        sellerService.add($scope.entity ).success(
            function(response){
                if(response.success){
                	alert(response.message);
                    location.href='shoplogin.html';
                }else{
                    alert(response.message);
                }
            }
		);				
	};
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		sellerService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	};
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		sellerService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数

			}			
		);
	};

	//修改密码
	$scope.changePassword=function(){
		sellerService.changePassword($scope.entity.oldPassword,$scope.entity.newPassword).success(function(response){
              //修改的成功的显示
			if (response.success){
                $scope.entity.oldPassword="";
                $scope.entity.newPassword="";
                $scope.confirmPassword="";
				alert(response.message);
                window.location.reload();
                location.href='../shoplogin.html';

			}else{
				//没有修改成功
				alert(response.message);
			}
		});
	};


	//判断用户的新旧密码不能相同
	$scope.oldAndNewPassword=function () {
        //获取用户的输入的oldPassword
        $scope.entity.oldPassword=$scope.entity.oldPassword;
        $scope.entity.newPassword=$scope.entity.newPassword;
        //定义两个字符串变量接受判断的数据
        //如果新密码和old密码相等
        if($scope.entity.oldPassword==$scope.entity.newPassword){
            $scope.result = '新旧密码不能相同';
            alert($scope.result);
        }
    };
    //判断用户的新旧密码不能相同
    $scope.newAndConfirmPassword=function () {
        //获取用户的输入的oldPassword
        $scope.entity.newPassword=$scope.entity.newPassword;
        $scope.confirmPassword=$scope.confirmPassword;
        //定义两个字符串变量接受判断的数据
        if($scope.entity.newPassword!=$scope.confirmPassword && $scope.confirmPassword!=null){
            $scope.result = '两次密码不匹配';
            alert($scope.result);
        }
    }

});	
