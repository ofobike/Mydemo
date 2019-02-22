package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功之后显示登录的人姓名
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    /**
     * 获取用户的登录名字
     * @return
     */
    @RequestMapping("/name")
    public Map name(){
        //获取用户的登录名字
        String loginName=SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = new HashMap();
        map.put("loginName",loginName);
        return map;
    }
}
