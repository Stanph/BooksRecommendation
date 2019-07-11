package com.zucc.br.service.impl;

import com.zucc.br.mapper.BxBooksMapper;
import com.zucc.br.pojo.BxBooks;
import com.zucc.br.service.BxBooksService;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haopan
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class BxBooksServiceImpl implements BxBooksService {
    @Autowired
    private BxBooksMapper bxBooksMapper;
    @Override
    public Map insert() {
        return null;
    }
    @Override
    public Map booksList(int offSet, int num, int type) {
        Map map=new HashMap();
        List<BxBooks> books;
        books = bxBooksMapper.findAllBooks(offSet,num);
        map.put("books",books);
        return map;
    }

    @Override
    public Map popularBooksList() {
        Map map=new HashMap();
        List<BxBooks> books;
        books = bxBooksMapper.findPopularBooks();
        map.put("books",books);
        return map;
    }

    @Override
    public Map findBookByID(List<RecommendedItem> list) {
        Map map=new HashMap();
        List<BxBooks> result=new ArrayList<BxBooks>();
        for (RecommendedItem ritem : list) {
            BxBooks bxBooks = bxBooksMapper.selectByPrimaryKey((int)ritem.getItemID());
            result.add(bxBooks);
        }
        map.put("books",result);
        return map;
    }

    @Override
    public Map findBookByID1(Integer bookid) {
        Map map=new HashMap();
        BxBooks bxBooks=bxBooksMapper.selectByPrimaryKey(bookid);
        map.put("books",bxBooks);
        return map;
    }

    @Override
    public Map listPasBooks(int userid) {
        Map map=new HashMap();
        List<BxBooks> books;
        books = bxBooksMapper.findPasBooks(userid);
        map.put("books",books);
        return map;
    }
}
