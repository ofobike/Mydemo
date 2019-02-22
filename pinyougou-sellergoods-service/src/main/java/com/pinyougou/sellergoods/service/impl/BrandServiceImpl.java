package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 接口的实现类
 */
@Service
public class BrandServiceImpl implements BrandService {

    //注入接口的实现类
    @Autowired
    private TbBrandMapper tbBrandMapper;

    /**
     * 查询的所有的品牌
     *
     * @return
     */
    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectByExample(null);
    }

    /**
     * 返回分页的数据集合
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        //调用分页的插件
        PageHelper.startPage(pageNum, pageSize);
        //调用业务层的方法
        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
        //返回数据查询的数据结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加品牌
     *
     * @param tbBrand
     */
    @Override
    public void add(TbBrand tbBrand) {
        tbBrandMapper.insert(tbBrand);
    }

    /**
     * 根据id获取品牌
     *
     * @param id
     * @return
     */
    @Override
    public TbBrand findOne(Long id) {
        //根据主键查询
        return tbBrandMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改品牌
     *
     * @param tbBrand
     */
    @Override
    public void update(TbBrand tbBrand) {
        //根据主键修改品牌
        tbBrandMapper.updateByPrimaryKey(tbBrand);
    }

    /**
     * 根据Id删除数据
     *
     * @param ids 删除数据
     */
    @Override
    public Result delete(Long[] ids) {
        //如果用户没有选择
        if (ids.length != 0 && ids != null) {
            try {
                //遍历数组
                for (Long id : ids) {
                    //调用数据的删除方法
                    tbBrandMapper.deleteByPrimaryKey(id);
                }
                return new Result(true, "删除成功");
            } catch (Exception e) {
                return new Result(false, "删除失败");
            }
        }
        return new Result(false, "没有选择删除的数据");
    }

    /**
     * 模糊查询并且分页
     *
     * @param tbBrand
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(TbBrand tbBrand, int pageNum, int pageSize) {
        //设置分页数据
        PageHelper.startPage(pageNum, pageSize);
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        //设置条件
        //不等于空的时候进行模糊查询(用户在输入框输入数据)
        if (tbBrand != null) {
            //进行品牌的名字的模糊查询
            String tbBrandName = tbBrand.getName();
            if (tbBrandName != null && tbBrandName.length() > 0){
                //进行品牌的名的查询
                criteria.andNameLike("%"+tbBrandName+"%");
            }
            //进行品牌首字母的模糊查询
            String tbBrandFirstChar = tbBrand.getFirstChar();
            if (tbBrandFirstChar!=null && tbBrandFirstChar.length()>0){
                //品牌的首字母查询
                criteria.andFirstCharLike("%"+tbBrandFirstChar+"%");
            }
        }
        //设置查询的条件
        Page<TbBrand>  page = (Page<TbBrand>) tbBrandMapper.selectByExample(example);
        //返回分页的查询数据
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 展示下拉列表
     * @return
     */
    @Override
    public List<Map> selectOptionList() {
        return tbBrandMapper.selectOptionList();
    }
}
