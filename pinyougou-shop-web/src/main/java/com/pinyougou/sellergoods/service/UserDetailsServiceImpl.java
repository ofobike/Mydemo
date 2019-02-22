package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSeller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户登录的验证
 */

public class UserDetailsServiceImpl implements UserDetailsService {

    private SellerService sellerService;
    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("............经过UserDetailServiceImpl...........");
        //创建保存角色的集合
        List<SimpleGrantedAuthority> authorities = new ArrayList();
        authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        //根据用户名查询商家的信息
        TbSeller tbSeller = sellerService.findOne(username);
        //对于商家的信息判断
        if (tbSeller!=null){//必须查询到值
               //对于商家的状态判断
               if ("1".equals(tbSeller.getStatus())){
                   return new User(username,tbSeller.getPassword(),authorities);
               }else {
                   //用户没有审核
                   return null;
               }
        }else {
            //没有查询到用户
            return  null;
        }


    }
}
