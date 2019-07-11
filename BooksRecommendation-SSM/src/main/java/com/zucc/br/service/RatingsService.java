package com.zucc.br.service;

import java.util.Map;

/**
 * @author haopan
 */
public interface RatingsService {
    /**
     * insert rating
     *
     * @param userid
     * @param bookid
     * @param score
     * @return
     */
    Map insert(Integer userid, Integer bookid, Double score);

}
