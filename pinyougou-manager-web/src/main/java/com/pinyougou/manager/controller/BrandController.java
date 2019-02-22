package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 品牌的控制层
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    //注入数据服务层

    @Reference
    private BrandService brandService;

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

    /**
     * 分页查询数据
     */
    @RequestMapping("/findPage")
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        //调用业务层的方法
        return brandService.findPage(pageNum, pageSize);
    }

    /**
     * 添加品牌的方法
     *
     * @param tbBrand
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand tbBrand) {
        try {
            //添加成功
            brandService.add(tbBrand);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    /**
     * 根据id查询实体(品牌)
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);
    }

    /**
     * 根据id修改品牌
     *
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand tbBrand) {
        try {
            brandService.update(tbBrand);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }


    /**
     * 删除数据(品牌)
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        return brandService.delete(ids);
    }

    /**
     * 模糊查询并且返回首页
     *
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbBrand tbBrand, Integer pageNum, Integer pageSize) {
        return brandService.findPage(tbBrand, pageNum, pageSize);
    }

    /**
     * 展示下拉列表
     *
     * @return
     */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {
        return brandService.selectOptionList();
    }
}
