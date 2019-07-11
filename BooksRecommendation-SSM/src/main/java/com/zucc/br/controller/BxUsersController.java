package com.zucc.br.controller;

import com.auth0.jwt.interfaces.Claim;
import com.zucc.br.service.BxUsersService;
import com.zucc.br.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haopan
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/users")
public class BxUsersController {
    @Autowired
    private BxUsersService bxUsersService;
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Map insert(@RequestBody(required = true) Map map){
        String account = (String) map.get("account");
        String pwd = (String) map.get("newPwd");
        String city=(String) map.get("city");
        String state=(String) map.get("state");
        String country=(String) map.get("country");
        Integer age=(Integer) map.get("age");
        Map result=new HashMap(1);
        result=bxUsersService.insert(city,state,country,age,account,pwd);
        return result;
    }
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Map login(@RequestBody(required=true) Map<String,Object> map) throws Exception {
        String account=(String) map.get("account");
        String pwd=(String) map.get("pwd");
        Map result=bxUsersService.login(account,pwd);
        return result;
    }

    @RequestMapping(value = "/changePwd",method = RequestMethod.POST)
    @ResponseBody
    public Map changePwd(@RequestBody(required = true) Map<String,Object> map){
        String token = (String) map.get("token");
        String pwd = (String)map.get("pwd");
        String newPwd = (String)map.get("newPwd");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        String name = JwtUtil.getAppName(token);
        Map result=bxUsersService.changePwd(name,pwd,newPwd);
        return result;
    }
}
