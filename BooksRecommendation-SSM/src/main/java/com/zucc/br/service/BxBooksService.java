package com.zucc.br.service;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.util.List;
import java.util.Map;

/**
 * @author haopan
 */
public interface BxBooksService {
    Map insert();
    Map booksList(int offSet, int num, int type);
    Map popularBooksList();
    Map findBookByID(List<RecommendedItem> list);
    Map findBookByID1(Integer bookid);
    Map listPasBooks(int userid);
}
