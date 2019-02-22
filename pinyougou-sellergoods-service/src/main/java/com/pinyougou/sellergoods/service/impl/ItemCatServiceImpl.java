package com.pinyougou.sellergoods.service.impl;

import java.util.*;

import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemCatExample;
import com.pinyougou.pojo.TbItemCatExample.Criteria;
import com.pinyougou.sellergoods.service.ItemCatService;

import entity.PageResult;
import sun.nio.cs.FastCharsetProvider;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbItemCat> findAll() {
        return itemCatMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbItemCat itemCat) {
        itemCatMapper.insert(itemCat);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbItemCat itemCat) {
        itemCatMapper.updateByPrimaryKey(itemCat);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbItemCat findOne(Long id) {
        return itemCatMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public Result delete(Long[] ids) {
        TbItemCatExample example = null;
        Criteria criteria = null;
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                //当在这个id下面能够查询到list数据的时候不能删除设置条件
                example = new TbItemCatExample();
                criteria = example.createCriteria();
                //设置查询的条件
                criteria.andParentIdEqualTo(id);
                List<TbItemCat> itemCats = itemCatMapper.selectByExample(example);
                //当list集合中有数据不能删除
                if (itemCats.size() > 0 && itemCats!=null) {
                    //这时候不能删除
                    return new Result(false,"该"+id+"下级有数据不能删除!!");
                } else {
                    //当集合中没有数据时候才能删除
                    itemCatMapper.deleteByPrimaryKey(id);
                }
            }
            return new Result(true,"删除成功!");

        }
        return new Result(false, "用户没有选择数据!!!");

    }


    @Override
    public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();

        if (itemCat != null) {
            if (itemCat.getName() != null && itemCat.getName().length() > 0) {
                criteria.andNameLike("%" + itemCat.getName() + "%");
            }

        }

        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据Id查询返回列表
     *
     * @param parentId
     * @return
     */
    @Override
    public List<TbItemCat> findByParentId(Long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();
        //设置查询的条件
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(example);
        return tbItemCats;
    }
}
