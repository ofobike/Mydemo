package com.pinyougou.sellergoods.controller;
import java.util.List;

import com.pinyougou.pojogroup.Goods;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		//获取登录商家的id
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		//设置商家的id
		goods.getGoods().setSellerId(sellerId);
		try {
			goodsService.add(goods);
			return new Result(true, "增加商品成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加商品失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		//检测是否是商家的id
		//校验是否是当前商家的 id
		Goods goods2 = goodsService.findOne(goods.getGoods().getId());
		//获取当前登录的商家信息
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
         //如果传递过来的商家 ID 并不是当前登录的用户的 ID,则属于非法操作
        if (!goods.getGoods().getSellerId().equals(sellerId) || !goods2.getGoods().getSellerId().equals(sellerId)){
			return new Result(false,"操作非法不允许操作");
		}
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
    /**
	 * 查询+分页 (商家登录的只显示本商家的信息) 条件的筛选
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		//获取商家 ID 只能显示商家的登录之后的商品信息
		String sellerId =SecurityContextHolder.getContext().getAuthentication().getName();
        //添加查询条件
		goods.setSellerId(sellerId);
		//设置查询条件
		return goodsService.findPage(goods, page, rows);		
	}

	/**
	 * 商家自己修改商品的状态(is_marketable)
	 * 字段为 tb_goods 表的 is_marketable 字段。 1
	   表示上架、 0 表示下架
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateMarketStatus")
	public Result updateMarketStatus(Long[] ids,String status){
		try {
			//调用服务层的方法
			goodsService.updateMarketStatus(ids, status);
			return new Result(true,"商品上/下架成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"商品上/下架失败");
		}
	}
}
