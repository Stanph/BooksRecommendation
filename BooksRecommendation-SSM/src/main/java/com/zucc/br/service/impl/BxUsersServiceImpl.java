package com.zucc.br.service.impl;

import com.zucc.br.mapper.BxUsersMapper;
import com.zucc.br.pojo.BxUsers;
import com.zucc.br.service.BxUsersService;
import com.zucc.br.util.JwtUtil;
import com.zucc.br.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haopan
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class BxUsersServiceImpl implements BxUsersService {
    @Autowired
    private BxUsersMapper bxUsersMapper;
    @Override
    public Map insert(String city,String state, String country,Integer age, String account, String pwd) {
        Map<String,Object> map= new HashMap<>(1);
        BxUsers bxUsers=bxUsersMapper.findUserByAccount(account);
        if(bxUsers != null){
            //用户已存在
            map.put("code", -1);
        }else{
            int result=bxUsersMapper.insert(city,state,country,age,account,pwd);
            if(result>0){
                map.put("code",0);
            }
        }
        return map;
    }
    @Override
    public Map login(String account, String pwd) throws Exception {
        Map<String,Object> map = new HashMap<>(3);
        BxUsers bxUsers=bxUsersMapper.findUserByAccount(account);
        pwd= Md5Util.getHash(pwd);
        if(bxUsers == null||!pwd.equals(bxUsers.getPwd())){
            //用户不存在或密码错误
            map.put("code", -1);
        }
        else {
            map.put("code", 0);
            String s=JwtUtil.createToken(bxUsers.getUserid(),bxUsers.getAccount());
            map.put("token", s);
            System.out.println("s"+JwtUtil.getAppUID(s));
            map.put("account",bxUsers.getAccount());
        }
        return map;
    }

    @Override
    public Map changePwd(String account, String pwd, String newPwd) {
        BxUsers bxUsers=bxUsersMapper.findUserByAccount(account);
        Map<String,Object> map=new HashMap<>();
        newPwd=Md5Util.getHash(newPwd);
        pwd=Md5Util.getHash(pwd);
        if(newPwd==null||newPwd.length()==0||!pwd.equals(bxUsers.getPwd())){
            map.put("code",-1);
        }
        else{
            bxUsers.setPwd(newPwd);
            bxUsersMapper.updateUserPwd(bxUsers);
            map.put("code",0);
        }
        return map;
    }
}
