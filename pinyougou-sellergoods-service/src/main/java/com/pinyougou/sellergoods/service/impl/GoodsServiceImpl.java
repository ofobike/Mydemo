package com.pinyougou.sellergoods.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbBrandMapper brandMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbSellerMapper sellerMapper;

    @Autowired
    private TbGoodsMapper goodsMapper;


    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {

        goods.getGoods().setAuditStatus("0");//状态：未审核
        goodsMapper.insert(goods.getGoods());//插入商品基本信息

        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());//将商品基本表的ID给商品扩展表
        goodsDescMapper.insert(goods.getGoodsDesc());//插入商品扩展表数据
        //调用插入商品的方法
        saveItemList(goods);

    }

    private void setItemValues(TbItem item, Goods goods) {
        //商品分类
        item.setCategoryid(goods.getGoods().getCategory3Id());//三级分类ID
        item.setCreateTime(new Date());//创建日期
        item.setUpdateTime(new Date());//更新日期

        item.setGoodsId(goods.getGoods().getId());//商品ID
        item.setSellerId(goods.getGoods().getSellerId());//商家ID

        //分类名称
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());
        //品牌名称
        TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        item.setBrand(brand.getName());
        //商家名称(店铺名称)
        TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        item.setSeller(seller.getNickName());

        //图片
        List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (imageList.size() > 0) {
            item.setImage((String) imageList.get(0).get("url"));
        }

    }

    /**
     * @param goods
     */
    @Override
    public void update(Goods goods) {
        //如果是修改的商品设置审核的状态为0
        goods.getGoods().setAuditStatus("0");
        //设置商品的上下架的状态为null
        goods.getGoods().setIsMarketable(null);
        //更新Goods基本数据类型
        goodsMapper.updateByPrimaryKey(goods.getGoods());
        //更新GoodsDesc新扩展属性表
        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());
        //先删除原有的SKU数据
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goods.getGoods().getId());
        itemMapper.deleteByExample(example);
        //在放入新的数据
        saveItemList(goods);
    }

    /**
     * 插入商品的SKU
     */
    private void saveItemList(Goods goods){
        if ("1".equals(goods.getGoods().getIsEnableSpec())) {
            for (TbItem item : goods.getItemList()) {
                //构建标题  SPU名称+ 规格选项值
                String title = goods.getGoods().getGoodsName();//SPU名称
                Map<String, Object> map = JSON.parseObject(item.getSpec());
                for (String key : map.keySet()) {
                    title += " " + map.get(key);
                }
                item.setTitle(title);

                setItemValues(item, goods);

                itemMapper.insert(item);
            }
        } else {//没有启用规格

            TbItem item = new TbItem();
            item.setTitle(goods.getGoods().getGoodsName());//标题
            item.setPrice(goods.getGoods().getPrice());//价格
            item.setNum(99999);//库存数量
            item.setStatus("1");//状态
            item.setIsDefault("1");//默认
            item.setSpec("{}");//规格

            setItemValues(item, goods);

            itemMapper.insert(item);
        }

    }
    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Goods findOne(Long id) {
        //返回的组合的实体类
        Goods goods = new Goods();
        //根据参数id查询基本的商品信息
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        //设置组合实体类中的商品基本信息
        goods.setGoods(tbGoods);
        //查询商品描述的信息
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        //设置组合实体类中商品描述数据
        goods.setGoodsDesc(tbGoodsDesc);
        //查询SKU类型
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        //设置规格数据
        goods.setItemList(tbItems);
        return goods;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //根据id查询goods的信息
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            //修改数据库表的数据is_delete 为1
            tbGoods.setIsDelete("1");
            //goodsMapper.deleteByPrimaryKey(id);
            //根据主键更新商品的状态
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeleteIsNull();//非删除状态  制定删除的状态为1不显示出来(已经删除不能再显示)

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                //criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }
            //输出商品的是否上下架的状态

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        //输出查询到的数据
        List<TbGoods> goodsFromDataBase =page.getResult();

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量更新商品的状态
     * @param ids
     * @param status
     */
    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id:ids){
            //根据id查询
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            //设置状态
            tbGoods.setAuditStatus(status);
            //更新
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

    /**
     * 修改商品的状态Market
     * @param ids
     * @param status
     */
    @Override
    public Result updateMarketStatus(Long[] ids, String status) {
        for (Long id:ids){

            //根据id查询商品
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            //修改的商品的上下架的状态先判断商品是否审核
            //获取商品的审核状态
            /**
             * $scope.status = [ 0 '未审核', 1 '已审核', 2 '审核未通过', 3 '关闭'];//商品状态
             */
            String auditStatus = tbGoods.getAuditStatus();
            if ("1".equals(auditStatus)){//如果已经审核
                //此时设置商品的状态
                tbGoods.setIsMarketable(status);
                //更新商品
                goodsMapper.updateByPrimaryKey(tbGoods);
                return new Result(true,"商品上/下架成功");
            }
            //如果没有审核不能上架
            //但是可以下架

        }
        return new Result(false,"商品上/下架失败");

    }

}
