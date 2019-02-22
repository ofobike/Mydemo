package com.pinyougou.manager.controller;

import java.util.List;

import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.pojogroup.ItemCatAndTypeTemplate;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;

import entity.PageResult;
import entity.Result;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    @Reference
    private TypeTemplateService typeTemplateService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbItemCat> findAll() {
        return itemCatService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return itemCatService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbItemCat itemCat) {
        try {
            itemCatService.add(itemCat);
            return new Result(true, "增加分类成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加分类失败");
        }
    }

    /**
     * 修改
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbItemCat itemCat) {
        try {
            itemCatService.update(itemCat);
            return new Result(true, "修改分类成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改分类失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public ItemCatAndTypeTemplate findOne(Long id) {
        ItemCatAndTypeTemplate itemCatAndTypeTemplate = new ItemCatAndTypeTemplate();
        TbItemCat itemCat = itemCatService.findOne(id);
        //获取typeTemplate的typeTemplateId
        Long typeId = itemCat.getTypeId();
        //调用TypeTemplateService查询模板的名称
        TbTypeTemplate tbTypeTemplate = typeTemplateService.findOne(typeId);
        //设置分类的类型
        itemCatAndTypeTemplate.setTbItemCat(itemCat);
        //设置类型模板
        itemCatAndTypeTemplate.setTbTypeTemplate(tbTypeTemplate);
        return itemCatAndTypeTemplate;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        //用户删除分类的下面的数据
        return  itemCatService.delete(ids);
    }

    /**
     * 查询+分页
     *
     * @param itemCat
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbItemCat itemCat, int page, int rows) {
        return itemCatService.findPage(itemCat, page, rows);
    }

    /**
     * 根据id查询返回列表
     *
     * @return
     */
    @RequestMapping("/findByParentId")
    public List<TbItemCat> findByParentId(Long parentId) {
        return itemCatService.findByParentId(parentId);
    }
}
