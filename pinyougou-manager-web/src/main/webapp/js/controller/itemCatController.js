//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        itemCatService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        itemCatService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }
    /**
     * 查询ItemCat
     * @param id
     * 根据id查询ItemCat和 TypeTemplate 的数据信息
     */
    $scope.findOne = function (id) {

            itemCatService.findOne(id).success(
                function (response) {
                    $scope.entity = response;
                    //获取id
                    $scope.entity.id = $scope.entity.tbItemCat.id;
                    //获取分类的名称
                    $scope.entity.name = $scope.entity.tbItemCat.name;
                    //获取parentId
                    $scope.entity.parentId = $scope.entity.tbItemCat.parentId;
                    //获取TypeId
                    $scope.entity.typeId = {
                        id: $scope.entity.tbTypeTemplate.id,
                        text: $scope.entity.tbTypeTemplate.name
                    }
                }
            );

    }


    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            //定义TypeId
            $scope.typeId = $scope.entity.typeId.id;
            //定义实体类型保存的类型
            $scope.entity = {
                id:$scope.entity.id,
                name: $scope.entity.name,
                parentId: $scope.entity.parentId,
                typeId: $scope.typeId
            }
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {
            $scope.entity.parentId = $scope.parentId;//赋予上级 ID
            //定义typeId变量
            $scope.typeId = $scope.entity.typeId.id;
            //定义实体类型保存数据类型
            $scope.entity = {
                name: $scope.entity.name,
                parentId: $scope.entity.parentId,
                typeId: $scope.typeId
            }
            serviceObject = itemCatService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    //$scope.reloadList();//重新加载
                    alert(response.message);
                    $scope.findByParentId($scope.parentId);//重新加载
                    //清空内容
                    $scope.entity = {}
                } else {
                    alert(response.message);
                }
            }
        );
    }



    //定义删除
    $scope.dele=function(){
        //调用angularJS的前端代码
        itemCatService.dele($scope.selectIds).success(function(response){
            if(response.success){
                //$scope.reloadList();//刷新列表
                alert(response.message);
                $scope.findByParentId($scope.parentId);//重新加载
                $scope.selectIds=[];
            }else {
                alert(response.message);
                $scope.findByParentId($scope.parentId);//重新加载
            }
        });
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        itemCatService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }


    //定义变量保存
    $scope.parentId = 0;//上级 ID

    //定义根据id查询返回列表的方法
    $scope.findByParentId = function (parentId) {
        alert(parentId);
        if (parentId!=null || parentId!=undefined) {
                $scope.parentId = parentId;//记住上级 ID
                //调用方法
                itemCatService.findByParentId(parentId).success(function (response) {
                    $scope.list = response;
                });
        }
    }

    //设置默认级别为1
    $scope.grade = 1;//默认为 1 级
    //修改级别
    $scope.setGrade = function (value) {
        $scope.grade = value;
    }

    //读取列表
    $scope.selectList = function (p_entity) {

        if ($scope.grade == 1) {
            //定义两个变量
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        }

        if ($scope.grade == 2) {
            $scope.entity_1 = p_entity;
            $scope.entity_2 = null;
        }

        if ($scope.grade == 3) {
            $scope.entity_2 = p_entity;
        }
        //查询下拉列表
        $scope.findByParentId(p_entity.id);
    }


    //$scope.ItemCatList={data:[{id:1,text:'联想'},{id:2,text:'华为'},{id:3,text:'小米'}]};

    //定义下拉框的列表
    $scope.typeTemplateList = {
        data: []
    }
    //获取分类的下拉列表
    $scope.selectTypeTemplateList = function () {
        //调用服务层的方法
        typeTemplateService.findSpecList().success(function (response) {
            $scope.typeTemplateList = {data: response}
        });
    }


});	
