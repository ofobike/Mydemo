package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;
import entity.Result;

import java.util.List;
import java.util.Map;

/**
 * 品牌的接口
 */
public interface BrandService {
    /**
     * 返回所有的品牌
     * @return
     */
    List<TbBrand> findAll();

    /**
     * 返回分页的数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult findPage(int pageNum,int pageSize);

    /**
     * 增加品牌
     * @param tbBrand
     */
    void add(TbBrand tbBrand);

    /**
     * 根据id获取品牌
     * @param id
     * @return
     */
    TbBrand findOne(Long id);

    /**
     * 修改品牌
     * @param tbBrand
     */
    void update(TbBrand tbBrand);

    /**
     * 根据id
     * @param ids 删除数据
     */
    Result delete(Long[] ids);

    /**
     * 模糊查询并且分页
     * @param tbBrand
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult findPage(TbBrand tbBrand,int pageNum,int pageSize);

    /**
     * 下拉列表展示数据
     */
    List<Map> selectOptionList();
}
