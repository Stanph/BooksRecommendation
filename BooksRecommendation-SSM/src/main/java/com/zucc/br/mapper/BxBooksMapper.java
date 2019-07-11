package com.zucc.br.mapper;

import com.zucc.br.pojo.BxBooks;

import java.util.List;

/**
 * @author haopan
 */
public interface BxBooksMapper {
    /**
     * insert book
     *
     * @param bxBooks
     * @return
     */
    int insert(BxBooks bxBooks);

    /**
     * @param offSet
     * @param pageSize
     * @return
     */
    List<BxBooks> findAllBooks(int offSet, int pageSize);

    /**
     * @return
     */
    List<BxBooks> findPopularBooks();

    /**
     * @param userid
     * @return
     */
    List<BxBooks> findPasBooks(int userid);
    /**
     * @param bookid
     * @return
     */
    BxBooks selectByPrimaryKey(int bookid);

    /**
     * @param bxBooks
     * @return
     */
    int updateByPrimaryKeySelective(BxBooks bxBooks);
}
