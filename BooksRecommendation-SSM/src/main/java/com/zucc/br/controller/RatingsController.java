package com.zucc.br.controller;

import com.auth0.jwt.interfaces.Claim;
import com.zucc.br.service.RatingsService;
import com.zucc.br.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author haopan
 */
@Controller
@RequestMapping(value = "/ratings")
public class RatingsController {
    @Autowired
    private RatingsService ratingsService;
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public Map insert(@RequestBody(required = true) Map map){
        String token = (String) map.get("token");
        Integer bookid = (Integer) map.get("bookid");
        Integer score = (Integer) map.get("score");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        Integer userID = JwtUtil.getAppUID(token);
        Map result=ratingsService.insert(userID,bookid,score.doubleValue());
        return result;
    }
}
