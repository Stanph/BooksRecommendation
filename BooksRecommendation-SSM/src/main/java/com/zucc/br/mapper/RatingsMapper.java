package com.zucc.br.mapper;

import com.zucc.br.pojo.Ratings;

/**
 * @author haopan
 */
public interface RatingsMapper {
    /**
     * insert rating
     *
     * @param ratings
     * @return
     */
    int insert(Ratings ratings);

    /**
     * @param bookid
     * @return
     */
    double getNewAvgScore(Integer bookid);

    /**
     * @param ratings
     * @return
     */
    int ifExist(Ratings ratings);

    /**
     * @param ratings
     * @return
     */
    int updateScore(Ratings ratings);
}
