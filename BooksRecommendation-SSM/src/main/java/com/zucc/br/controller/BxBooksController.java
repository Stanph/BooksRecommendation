package com.zucc.br.controller;

import com.auth0.jwt.interfaces.Claim;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.zucc.br.service.BxBooksService;
import com.zucc.br.util.JwtUtil;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author haopan
 */
@Controller
@RequestMapping(value = "/books")
public class BxBooksController {
    final static int NEIGHBORHOOD_NUM = 10;
    final static int RECOMMENDER_NUM = 20;
    @Autowired
    private BxBooksService bxBooksService;
    @RequestMapping(value = "/id",method = RequestMethod.POST)
    @ResponseBody
    public Map findBookByID(@RequestBody(required=true) Map<String,Object> map){
        Integer bookid=(Integer) map.get("bookid");
        Map result=bxBooksService.findBookByID1(bookid);
        return result;
    }
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public Map booksList(@RequestBody(required=true) Map<String,Object> map){
        int p= (int) map.get("p");
        int num= (int) map.get("num");
        int type=(int) map.get("type");
        int offSet=(p-1)*num;
        Map result=bxBooksService.booksList(offSet,num,type);
        return result;
    }
    @RequestMapping(value = "/popularList",method = RequestMethod.POST)
    @ResponseBody
    public Map popularBooksList(@RequestBody(required=true) Map<String,Object> map){
        Map result=bxBooksService.popularBooksList();
        return result;
    }
    @RequestMapping(value = "/rating",method = RequestMethod.POST)
    @ResponseBody
    public Map listPasBooks(@RequestBody(required=true) Map<String,Object> map){
        String token = (String) map.get("token");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        Integer userid = JwtUtil.getAppUID(token);
        Map result=bxBooksService.listPasBooks(userid);
        return result;
    }
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public Map insert(@RequestBody(required = true) Map map){

        return null;
    }
    @RequestMapping(value = "/brByUser",method = RequestMethod.POST)
    @ResponseBody
    public Map brByUser(@RequestBody(required=true) Map<String,Object> map) throws TasteException {
        String token = (String) map.get("token");
        Map<String, Claim> a = JwtUtil.verifyToken(token);
        Integer userid = JwtUtil.getAppUID(token);
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://127.0.0.1:3306/books?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        dataSource.setUser("root");
        dataSource.setPassword("docker_qa");
        //  JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource, "ratings", "userid", "bookid", "score", null);
        JDBCDataModel jdbcDataModel = new MySQLJDBCDataModel(dataSource,"ratings", "userid", "bookid", "score", null);
        ReloadFromJDBCDataModel model = new ReloadFromJDBCDataModel(jdbcDataModel);
        UserSimilarity user = new EuclideanDistanceSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, user, model);
        Recommender r = new GenericUserBasedRecommender(model, neighborhood, user);
        LongPrimitiveIterator iter = model.getUserIDs();
        List<RecommendedItem> list = r.recommend(userid, RECOMMENDER_NUM);
        for (RecommendedItem ritem : list) {
            System.out.printf("(%s,%f)", ritem.getItemID(), ritem.getValue());
        }
        Map result=bxBooksService.findBookByID(list);
        return result;
    }
    @RequestMapping(value = "/brByItem",method = RequestMethod.POST)
    @ResponseBody
    public Map brByItem(@RequestBody(required=true) Map<String,Object> map) throws TasteException {
        Integer bookid=(Integer) map.get("bookid");
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(
                "jdbc:mysql://127.0.0.1:3306/books?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        dataSource.setUser("root");
        dataSource.setPassword("docker_qa");
        JDBCDataModel jdbcDataModel = new MySQLJDBCDataModel(dataSource, "ratings", "userid", "bookid", "score", null);
        ReloadFromJDBCDataModel model = new ReloadFromJDBCDataModel(jdbcDataModel);
        ItemSimilarity similarity = new TanimotoCoefficientSimilarity(model);
        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);
        // Recommender r = new GenericUserBasedRecommender(model, neighborhood,
        // user);
        LongPrimitiveIterator iter = model.getUserIDs();
//        List<RecommendedItem> list = recommender.recommend(5506, RECOMMENDER_NUM);
        List<RecommendedItem> list = recommender.mostSimilarItems(bookid, RECOMMENDER_NUM);
        for (RecommendedItem ritem : list) {
            System.out.printf("(%s,%f)", ritem.getItemID(), ritem.getValue());
        }
        Map result=bxBooksService.findBookByID(list);
        return result;
    }
}
