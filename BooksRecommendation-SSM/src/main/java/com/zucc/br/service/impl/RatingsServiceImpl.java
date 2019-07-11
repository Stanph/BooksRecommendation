package com.zucc.br.service.impl;

import com.zucc.br.mapper.BxBooksMapper;
import com.zucc.br.mapper.RatingsMapper;
import com.zucc.br.pojo.BxBooks;
import com.zucc.br.pojo.Ratings;
import com.zucc.br.service.RatingsService;
import com.zucc.br.util.JwtUtil;
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
public class RatingsServiceImpl implements RatingsService {
    @Autowired
    private RatingsMapper ratingsMapper;
    @Autowired
    private BxBooksMapper bxBooksMapper;
    @Override
    public Map insert(Integer userid, Integer bookid, Double score) {
        Ratings r=new Ratings();
        r.setBookid(bookid);
        r.setScore(score);
        r.setUserid(userid);
        System.out.println(userid);
        int exist = ratingsMapper.ifExist(r);
        Map<String,Object> map= new HashMap<>(1);
        int result;
        if(exist==0){
            result= ratingsMapper.insert(r);

        }else{
            result=ratingsMapper.updateScore(r);
        }
        if(result>0){
            double avgScore=ratingsMapper.getNewAvgScore(bookid);
            BxBooks bxBooks= new BxBooks();
            bxBooks.setBookid(bookid);
            bxBooks.setScore(avgScore);
            bxBooksMapper.updateByPrimaryKeySelective(bxBooks);
            map.put("code",0);
        }
        else{
            map.put("code",-1);
        }
        return map;
    }
}
