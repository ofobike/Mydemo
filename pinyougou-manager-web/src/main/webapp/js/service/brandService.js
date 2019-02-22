app.service("brandService",function($http){
    //定义方法绑定数据 查询所有
    this.findAll=function(){
        //调用后台的数据
       return  $http.get('../brand/findAll.do');
    }
    //抽取分页
    this.findPage=function(pageNum,pageSize){
        return $http.get('../brand/findPage.do?pageNum='+pageNum+'&pageSize='+pageSize);
    }
    //根据id查询
    this.findOne=function(id){
        return $http.get('../brand/findOne.do?id='+id);
    }
    //抽取add
    this.add=function(entity){
        return $http.post('../brand/add.do',entity);
    }
    //抽取更新的update
    this.update=function(entity){
        return $http.post('../brand/update.do',entity);
    }
    //抽取删除的方法
    this.dele=function(ids){
        return $http.get('../brand/delete.do?ids='+ids);
    }
    //抽取模糊查询并且分页
    this.search=function(pageNum,pageSize,entity){
        return $http.post('../brand/search.do?pageNum=' + pageNum
            + '&pageSize=' + pageSize, entity)
    }
    //展示下拉列表
    this.selectOptionList=function(){
        //向后端获取数据
        return $http.get('../brand/selectOptionList.do');
    }
});