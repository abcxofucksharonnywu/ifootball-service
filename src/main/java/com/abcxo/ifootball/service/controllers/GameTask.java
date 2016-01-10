package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.Game;
import com.abcxo.ifootball.service.repos.GameRepo;
import com.abcxo.ifootball.service.repos.TweetRepo;
import com.abcxo.ifootball.service.repos.UserRepo;
import com.abcxo.ifootball.service.repos.UserTweetRepo;
import com.abcxo.ifootball.service.utils.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shadow on 15/11/30.
 */
@Component
public class GameTask {
    @Autowired
    public UserRepo userRepo;
    @Autowired
    public TweetRepo tweetRepo;
    @Autowired
    public UserTweetRepo userTweetRepo;

    @Autowired
    public GameRepo gameRepo;


    public static List<String> TEAMS = Arrays.asList("巴塞罗那", "曼城", "阿森纳", "曼联", "皇家马德里", "罗马", "AC米兰", "巴列卡诺", "国际米兰", "那不勒斯", "切尔西", "热刺", "马德里竞技", "尤文图斯", "本菲卡", "利物浦", "拜仁慕尼黑", "多特蒙德", "巴黎圣日耳曼", "北京国安", "广州恒大", "上海上港", "山东鲁能");

    //直播
    @Scheduled(fixedDelay = 4 * 60 * 60 * 1000)
    public void runInit() {
        gameRepo.deleteAll();
        runInitInZhiboba();

    }

    public void runInitInZhiboba() {
        List<Game> games = runGrepGamesInZhiboba();
        System.out.println("game runInitInZhiboba " + games.size());
    }


    public List<Game> runGrepGamesInZhiboba() {
        List<Game> games = new ArrayList<>();
        try {
            String host = String.format("http://m.zhibo8.cc/json/zhibo/saishi.json?aabbccddeeff=%d", System.currentTimeMillis());
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            HttpGet httpGet = new HttpGet(host);
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            //响应状态
            System.out.println("status:" + httpResponse.getStatusLine());
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                JSONArray array = new JSONArray(EntityUtils.toString(entity));
                for (int o = 0; o < array.length(); o++) {
                    JSONObject json = array.optJSONObject(o);
                    JSONArray list = json.getJSONArray("list");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject object = list.optJSONObject(i);
                        Game game = new Game();
                        String date = object.optString("start");
                        String name = object.optString("home_team");
                        String name2 = object.optString("visit_team");
                        if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(name2) && (TEAMS.contains(name) || TEAMS.contains(name2))) {
                            HttpGet contentGet = new HttpGet(String.format("http://m.zhibo8.cc/m/android/json/%s?aabbccddeeff=%d", object.optString("url").replace(".htm", ".json"), System.currentTimeMillis()));
                            HttpResponse contentResponse = closeableHttpClient.execute(contentGet);
                            HttpEntity contentEntity = contentResponse.getEntity();
                            if (contentResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                                JSONObject contentJson = new JSONObject(EntityUtils.toString(contentEntity));
                                String title = contentJson.optString("match_type");
                                String content = contentJson.optString("content");
                                JSONArray channelArray = contentJson.getJSONArray("channel");
                                ArrayList<Game.Live> lives = new ArrayList<>();
                                for (int j = 0; j < channelArray.length(); j++) {
                                    JSONObject channel = channelArray.optJSONObject(j);
                                    Game.Live live = new Game.Live();
                                    live.setTitle(channel.optString("name"));
                                    live.setUrl(channel.optString("url"));
                                    lives.add(live);
                                }

                                game.setTitle(title);
                                game.setContent(content);
                                game.setName(name);
                                game.setName2(name2);
                                game.setLives(lives);
//                    String icon = content.getElementsByClass("t1_logo").first().attr("src");
//                    String score = content.getElementsByClass("host_score").first().text();
//                    String name2 = content.getElementsByClass("team_guest").first().text();
//                    String icon2 = content.getElementsByAttributeValue("alt", name).first().attr("src");
//                    String score2 = content.getElementsByClass("visit_score").first().text();
                                game.setDate(Utils.getDate(date));
                                game.setTime(Utils.getTime(game.getDate()));
                                gameRepo.saveAndFlush(game);
                                games.add(game);
                            }
                        }


                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return games;
    }


}
