package com.zucc.br.mapper;

import com.zucc.br.pojo.BxUsers;

/**
 * @author haopan
 */
public interface BxUsersMapper {
    /**
     * insert user location ,age ,account and password
     *
     * @param
     * @return
     */
    int insert(String city,String state,String country, Integer age, String account,String pwd);

    /**
     * change password
     *
     * @param bxUsers
     */
    void updateUserPwd(BxUsers bxUsers);

    /**
     * find user by name
     *
     * @param Account
     * @return
     */
    BxUsers findUserByAccount(String Account);
}
