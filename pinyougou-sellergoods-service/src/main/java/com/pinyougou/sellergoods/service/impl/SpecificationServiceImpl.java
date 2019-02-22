package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojogroup.Specification;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private TbSpecificationMapper specificationMapper;

    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbSpecification> findAll() {
        return specificationMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Specification specification) {
        specificationMapper.insert(specification.getSpecification());//插入规格
        //循环插入规格选项
        for (TbSpecificationOption specificationOption : specification.getSpecificationOptionList()) {
            specificationOption.setSpecId(specification.getSpecification().getId());//设置规格 ID
            specificationOptionMapper.insert(specificationOption);
        }
    }


    /**
     * 修改
     */
    @Override
    public void update(Specification specification) {
        //保存修改的规格
        //获取规格
        TbSpecification tbSpecification = specification.getSpecification();
        specificationMapper.updateByPrimaryKey(tbSpecification);
        //删除原有的规格的选项
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        //设置删除的条件
        criteria.andSpecIdEqualTo(tbSpecification.getId());
        specificationOptionMapper.deleteByExample(example);
        //再次循环放入数据
        //获取规格的选项
        List<TbSpecificationOption> tbSpecificationOptions = specification.getSpecificationOptionList();
        for (TbSpecificationOption option : tbSpecificationOptions) {
            //设置id
            option.setSpecId(tbSpecification.getId());
            //循环插入数据
            specificationOptionMapper.insert(option);
        }
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Specification findOne(Long id) {
        //创建对象
        Specification specification = new Specification();
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        //设置规格
        specification.setSpecification(tbSpecification);

        //查询规格选项
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        //创建查询的条件
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        //设置查询的条件 根据id查询数据
        criteria.andSpecIdEqualTo(id);
        List<TbSpecificationOption> tbSpecificationOptions = specificationOptionMapper.selectByExample(example);
        //设置规格的选项
        specification.setSpecificationOptionList(tbSpecificationOptions);
        return specification;
    }

    /**
     * 批量删除
     */
    @Override
    public Result delete(Long[] ids) {
        //如果用户没有选择那么提示用户
        if (ids.length > 0 && ids != null) {
            try {
                for (Long id : ids) {
                    //删除规格
                    specificationMapper.deleteByPrimaryKey(id);
                    //删除规格选项
                    TbSpecificationOptionExample example = new TbSpecificationOptionExample();
                    TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
                    criteria.andSpecIdEqualTo(id);
                    //把规格的选项也同时删除
                    specificationOptionMapper.deleteByExample(example);
                }
                return new Result(true, "删除规格成功");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, "删除规格失败");
            }
        }
        return new Result(false, "用户没有选择删除的数据");
    }


    @Override
    public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbSpecificationExample example = new TbSpecificationExample();
        Criteria criteria = example.createCriteria();

        if (specification != null) {
            if (specification.getSpecName() != null && specification.getSpecName().length() > 0) {
                criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
            }

        }

        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }
    /**
     * 展示规格选项的下拉列表
     * @return
     */
    @Override
    public List<Map> selectSpecificationList() {
        return specificationMapper.selectSpecificationList();
    }
}
