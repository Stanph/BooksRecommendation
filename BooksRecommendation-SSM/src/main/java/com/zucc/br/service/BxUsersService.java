package com.zucc.br.service;

import java.util.Map;

/**
 * @author haopan
 */
public interface BxUsersService {
    /**
     * insert user location ,age ,account and password
     * @param city
     * @param state
     * @param country
     * @param age
     * @param account
     * @param pwd
     * @return
     */
    Map insert(String city,String state, String country, Integer age, String account,String pwd);

    /**
     * user log in
     * @param account
     * @param pwd
     * @return
     * @throws Exception
     */
    Map login(String account, String pwd) throws Exception;

    /**
     * user change password
     * @param account
     * @param pwd
     * @param newPwd
     * @return
     */
    Map changePwd(String account, String pwd, String newPwd);
}
