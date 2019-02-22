package com.pinyougou.sellergoods.controller;
import java.util.List;
import java.util.ServiceLoader;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference
	private SellerService sellerService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeller> findAll(){			
		return sellerService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return sellerService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param seller
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbSeller seller){
		//设置密码加密
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String newPassword = encoder.encode(seller.getPassword());
		//设置加密密码
		seller.setPassword(newPassword);
		try {
			sellerService.add(seller);
			return new Result(true, "增加商家成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加商家失败");
		}
	}
	
	/**
	 * 修改
	 * @param seller
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbSeller seller){
		try {
			sellerService.update(seller);
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
	public TbSeller findOne(String id){

		return sellerService.findOne(id);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(String [] ids){
		try {
			sellerService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param seller
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbSeller seller, int page, int rows  ){
		return sellerService.findPage(seller, page, rows);		
	}

	/**
	 * 根据商家的id修改的商家的密码
	 * @return
	 */
	@RequestMapping("/updateSellerPassword")
	public Result updateSellerPassword(String oldPassword,String newPassword){
         //获取商家的信息Id
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		//根据商家的id查询商家的信息
		TbSeller tbSeller = sellerService.findOne(sellerId);
		//根据获取商家的密码(加密之后)
		String password = tbSeller.getPassword();
		//对比原始密码
		//获取加密
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		//对比原密码和页面接受的密码
		boolean matches = bCryptPasswordEncoder.matches(oldPassword, password);
		//如果匹配成功
		if (matches){
			//对传入的新密码进行加密
			String confirmPassword = bCryptPasswordEncoder.encode(newPassword);
			//设置新密码
			tbSeller.setPassword(confirmPassword);
			//修改商家的信息
			//调用服务层的方法
			sellerService.update(tbSeller);
			return new Result(true,"修改密码成功!!");
		}else{
			return new Result(false,"密码不匹配!请确认原密码");
		}

	}
	
}
