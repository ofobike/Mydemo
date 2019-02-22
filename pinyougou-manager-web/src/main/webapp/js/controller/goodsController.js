//控制层
app.controller('goodsController', function ($scope, $controller, $location, uploadService, goodsService, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //定义点击清空输入框的方法
    $scope.cleanInputName = function () {
        $scope.searchEntity.goodsName = "";
    }

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function () {
        //获取页面传递过来的参数
        var id = $location.search()['id'];//获取参数值
        if (id == undefined || id == null) {
            return;
        }
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;

                //向富文本编辑器添加商品介绍
                editor.html($scope.entity.goodsDesc.introduction);
                //显示图片列表
                $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
                //显示扩展属性
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                //规格
                $scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems);
                //SKU 列表规格列转换
                for( var i=0;i<$scope.entity.itemList.length;i++ ){
                    $scope.entity.itemList[i].spec =
                        JSON.parse( $scope.entity.itemList[i].spec);
                }
            }
        );
    }

    /**
     * //根据规格名称和选项名称返回是否被勾选
     * @param specName
     * @param optionName
     * @returns {boolean}
     */
    $scope.checkAttributeValue=function(specName,optionName){
        var items= $scope.entity.goodsDesc.specificationItems;
        var object= $scope.searchObjectByKey(items,'attributeName',specName);
        if(object==null){
            return false;
        }else{
            if(object.attributeValue.indexOf(optionName)>=0){
                return true;
            }else{
                return false;
            }
        }
    }
    //保存
    $scope.save=function(){
        //提取文本编辑器的值
        $scope.entity.goodsDesc.introduction=editor.html();
        var serviceObject;//服务层对象
        if($scope.entity.goods.id!=null){//如果有 ID
            serviceObject=goodsService.update( $scope.entity ); //修改
        }else{
            serviceObject=goodsService.add( $scope.entity );//增加
        }
        serviceObject.success(
            function(response){
                if(response.success){
                    alert('保存成功');
                    // $scope.entity={};
                    // editor.html("");
                    location.href="goods.html";//跳转到商品列表页 不需要上面的操作了
                }else{
                    alert(response.message);
                }
            }
        );
    }
    /*var serviceObject;//服务层对象
    if($scope.entity.id!=null){//如果有ID
        serviceObject=goodsService.update( $scope.entity ); //修改
    }else{
        serviceObject=goodsService.add( $scope.entity  );//增加
    }
    serviceObject.success(
        function(response){
            if(response.success){
                //重新查询
                $scope.reloadList();//重新加载
            }else{
                alert(response.message);
            }
        }
    );	*/



    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    $scope.status = ['未审核', '已审核', '审核未通过', '关闭'];//商品状态
    /**
     * 查询商品的信息
     * @param page 页码
     * @param rows 每页显示的数量
     */
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                //后台查询的记录绑定的到变量$scope.list 上
                $scope.list = response.rows;
                //更新总记录条数(数据库查询到的数据)
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }
    /**
     * 上传图片
     */
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (response) {
            if (response.success) {//如果上传成功，取出 url
                $scope.image_entity.url = response.message;//设置文件地址
            } else {
                alert(response.message);
            }
        }).error(function () {
            alert("传图片错误!!!");
        });
    }

    $scope.entity = {goods: {}, goodsDesc: {itemImages: []}};//定义页面实体结构
    //添加图片列表
    $scope.add_image_entity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }
    //列表中移除图片
    $scope.remove_image_entity = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);

    }
    //展示一级下拉列表
    $scope.selectItemCat1List = function () {
        //调用业务列表的方法
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCat1List = response;
        });
    }
    //读取二级分类
    $scope.$watch('entity.goods.category1Id', function (newValue, oldValue) {
        //根据选择的值，查询二级分类
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat2List = response;
            }
        );
    });
    //读取三级分类
    $scope.$watch('entity.goods.category2Id', function (newValue, oldValue) {
        //根据选择的值，查询二级分类
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat3List = response;
            }
        );
    });
    //三级分类选择后 读取模板 ID
    $scope.$watch('entity.goods.category3Id', function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(
            function (response) {
                //TODO 这里出现BUG 因为自己定义的接受模板出现错误 导致response.typeId=null
                //alert(response.tbItemCat.typeId);
                $scope.entity.goods.typeTemplateId = response.tbItemCat.typeId; //更新模板 ID
            }
        );
    });
    //模板 ID 选择后 更新品牌列表
    $scope.$watch('entity.goods.typeTemplateId', function (newValue, oldValue) {
        //TODO 因为上面的为null 导致newValue成为undefind
        typeTemplateService.findOne(newValue).success(
            function (response) {
                $scope.typeTemplate = response;//获取类型模板
                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);//品牌列表
                //如果没有 ID，则加载模板中的扩展数据
                if ($location.search()['id'] == null) {
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);//扩展属性
                }
            }
        );
        //调用方法
        typeTemplateService.findSpecList(newValue).success(function (response) {
            //定义一个变量接受数据
            $scope.specList = response;
            //输出页面
            //console.log($scope.specList);
        });
    });


    $scope.entity = {goodsDesc: {itemImages: [], specificationItems: []}};
    $scope.updateSpecAttribute = function ($event, name, value) {
        var object = $scope.searchObjectByKey(
            $scope.entity.goodsDesc.specificationItems, 'attributeName', name);
        if (object != null) {
            if ($event.target.checked) {
                object.attributeValue.push(value);
            } else {//取消勾选
                object.attributeValue.splice(object.attributeValue.indexOf(value), 1);//移除选项
                //如果选项都取消了，将此条记录移除
                if (object.attributeValue.length == 0) {
                    $scope.entity.goodsDesc.specificationItems.splice(
                        $scope.entity.goodsDesc.specificationItems.indexOf(object), 1);
                }
            }
        } else {
            $scope.entity.goodsDesc.specificationItems.push(
                {"attributeName": name, "attributeValue": [value]});
        }
    }
    //创建 SKU 列表
    $scope.createItemList = function () {
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}]
        ;//初始
        var items = $scope.entity.goodsDesc.specificationItems;
        for (var i = 0; i < items.length; i++) {
            $scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
        }
    }
    //添加列值
    addColumn = function (list, columnName, conlumnValues) {
        var newList = [];//新的集合
        for (var i = 0; i < list.length; i++) {
            var oldRow = list[i];
            for (var j = 0; j < conlumnValues.length; j++) {
                var newRow = JSON.parse(JSON.stringify(oldRow));//深克隆
                newRow.spec[columnName] = conlumnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }
    //定义变量保存数据
    $scope.itemCatList = []
    /**
     * 展示页面的分类数据
     */
    $scope.findItemList = function () {
        //调用查询所有的方法
        itemCatService.findAll().success(function (response) {
            for (var i = 0; i < response.length; i++) {
                // console.log(response[i].id)
                //console.log(response[i].name)
                $scope.itemCatList[response[i].id] = response[i].name;
                //console.log($scope.itemList)
            }
        });
    }
    //更改状态
    $scope.updateStatus=function(status){
        goodsService.updateStatus($scope.selectIds,status).success(
            function(response){
                if(response.success){//成功
                    $scope.reloadList();//刷新列表
                    $scope.selectIds=[];//清空 ID 集合
                }else{
                    alert(response.message);
                }
            }
        );
    }
});	
