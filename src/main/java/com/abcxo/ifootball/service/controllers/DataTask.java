package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.Data;
import com.abcxo.ifootball.service.repos.DataRepo;
import com.abcxo.ifootball.service.repos.TweetRepo;
import com.abcxo.ifootball.service.repos.UserRepo;
import com.abcxo.ifootball.service.repos.UserTweetRepo;
import com.abcxo.ifootball.service.utils.Constants;
import com.abcxo.ifootball.service.utils.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shadow on 15/11/30.
 */
@Component
public class DataTask {
//    @Autowired
//    public UserRepo userRepo;
//    @Autowired
//    public TweetRepo tweetRepo;
//    @Autowired
//    public UserTweetRepo userTweetRepo;
//
//    @Autowired
//    public DataRepo dataRepo;
//
//
//    //直播
//    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000, initialDelay = 2 * 60 * 1000)
//    public void runInit() {
//        dataRepo.deleteAll();
//        runInitInDongqiudi();
//
//    }
//
//    public void runInitInDongqiudi() {
//        List<Data> datas = runGrepDatasInDongqiudi();
//        System.out.println("data runInitInDongqiudi " + datas.size());
//    }
//
//
//    public List<Data> runGrepDatasInDongqiudi() {
//        List<Data> datas = new ArrayList<>();
//        try {
//            Map<String, String> leagues = new HashMap<>();
//            leagues.put("英超", "8");
//            leagues.put("西甲", "7");
//            leagues.put("欧冠", "10");
//            leagues.put("德甲", "9");
//            leagues.put("意甲", "13");
//            leagues.put("法甲", "16");
//            leagues.put("中超", "51");
//            leagues.put("亚冠", "251");
//
//            Map<String, String> categories = new HashMap<>();
//            categories.put("积分榜", "team_rank");
//            categories.put("射手榜", "goal_rank");
//            categories.put("助攻榜", "assist_rank");
//            categories.put("赛程表", "schedule");
//            for (Map.Entry<String, String> entry : leagues.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//                for (Map.Entry<String, String> entry1 : categories.entrySet()) {
//                    try {
//                        String key1 = entry1.getKey();
//                        String value1 = entry1.getValue();
//                        String url = String.format("http://www.dongqiudi.com/data?competition=%s&type=%s", value, value1);
//                        Document document = Utils.getDocument(url);
//                        Elements elements = document.select("table[class=list_1]");
//                        if (elements == null || elements.size() == 0) {
//                            elements = document.select("table[class=list_2]");
//                        }
//                        Element element = elements.first();
//                        element.select("th[class=top_2]").remove();
//                        if ("赛程表".equals(key1)){
//                            element.select("th[class=prev]").remove();
//                            element.select("th[class=next]").remove();
//                            element.select("th[id=schedule_title]").attr("colspan", "10");
//                        }
//
//                        Data data = new Data();
//                        data.setName(key);
//                        data.setCategory(key1);
//                        data.setUrl(Utils.content(Constants.TWEET_HTML_DATA.replace(Constants.TWEET_HTML_CONTENT_TAG, element.toString())));
//                        data.setDate(System.currentTimeMillis());
//                        data.setTime(Utils.getTime(data.getDate()));
//                        dataRepo.saveAndFlush(data);
//                        datas.add(data);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return datas;
//    }
}
