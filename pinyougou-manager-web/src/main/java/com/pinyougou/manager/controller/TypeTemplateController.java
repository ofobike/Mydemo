package com.pinyougou.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.sellergoods.service.TypeTemplateService;

import entity.PageResult;
import entity.Result;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbTypeTemplate> findAll() {
        return typeTemplateService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return typeTemplateService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbTypeTemplate typeTemplate) {
        try {
            typeTemplateService.add(typeTemplate);
            return new Result(true, "增加模板成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加模板失败");
        }
    }

    /**
     * 修改
     *
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbTypeTemplate typeTemplate) {
        try {
            typeTemplateService.update(typeTemplate);
            return new Result(true, "修改模板成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改模板失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbTypeTemplate findOne(Long id) {
        return typeTemplateService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        return typeTemplateService.delete(ids);
    }

    /**
     * 查询+分页
     *
     * @param typeTemplate
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbTypeTemplate typeTemplate, int page, int rows) {
        return typeTemplateService.findPage(typeTemplate, page, rows);
    }

    /**
     * 展示品牌的下拉数据
     * @param id
     * @return
     */
    @RequestMapping("/findSpecList")
    public List<Map> findSpecList(Long id){
        return typeTemplateService.findSpecList(id);
    }
}
