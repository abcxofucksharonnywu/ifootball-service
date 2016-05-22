package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.Game;
import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.repos.GameRepo;
import com.abcxo.ifootball.service.repos.TweetRepo;
import com.abcxo.ifootball.service.repos.UserRepo;
import com.abcxo.ifootball.service.repos.UserTweetRepo;
import com.abcxo.ifootball.service.utils.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

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
    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 6 * 60 * 1000)
    public void runInit() {
        gameRepo.deleteAll();
        runInitInDongqiudi();

    }

    public void runInitInDongqiudi() {
        List<Game> games = runGrepGamesInDongqiudi();
        System.out.println("game runInitInDongqiudi " + games.size());
    }


    public List<Game> runGrepGamesInDongqiudi() {
        List<Game> games = new ArrayList<>();
        try {
            String host = String.format("http://www.dongqiudi.com/match/fetch?tab=null&date=%s&scroll_times=0&tz=-8", Utils.getDate());
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            HttpGet httpGet = new HttpGet(host);
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            //响应状态
            System.out.println("status:" + httpResponse.getStatusLine());
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
                Document root = Jsoup.parse(jsonObject.optString("html"));
                Elements list = root.getElementsByClass("matchItem");
                HttpPost contentPost = new HttpPost("http://www.dongqiudi.com/match/query");
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Element element : list) {
                    nvps.add(new BasicNameValuePair("ids[]", element.attr("rel")));
                }
                contentPost.setEntity(new UrlEncodedFormEntity(nvps));
                HttpResponse contentResponse = closeableHttpClient.execute(contentPost);
                HttpEntity contentEntity = contentResponse.getEntity();
                if (contentResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                    Document document = Utils.getDocument("http://www.tiantian.tv/zuqiuzhibo/");

                    JSONArray contents = new JSONArray(EntityUtils.toString(contentEntity));
                    for (int i = 0; i < contents.length(); i++) {
                        try {
                            JSONObject object = contents.optJSONObject(i);
                            Game game = new Game();
                            game.setTitle(object.optString("competition_name"));
                            game.setName(object.optString("team_A_name"));
                            game.setIcon(object.optString("team_A_logo"));
                            String[] result = object.optString("result").split("-");
                            game.setScore(result.length > 1 ? result[0] : "0");
                            game.setName2(object.optString("team_B_name"));
                            game.setIcon2(object.optString("team_B_logo"));
                            game.setScore2(result.length > 1 ? result[1] : "0");
                            game.setDate(object.optLong("timestamp") * 1000);
                            game.setTime(Utils.getTime(game.getDate()));
                            if (game.getDate() < System.currentTimeMillis()) {
                                game.setStateType(Game.StateType.PREPARE);
                            } else if (game.getDate() >= System.currentTimeMillis() && game.getDate() < System.currentTimeMillis() - 3 * 60 * 60 * 1000) {
                                game.setStateType(Game.StateType.ING);
                            } else {
                                game.setStateType(Game.StateType.END);
                            }
                            JSONArray videos = object.optJSONArray("video");
                            ArrayList<Game.Live> lives = new ArrayList<>();
                            Map<String, Game.Live> liveMap = new HashMap<>();
                            for (int j = 0; j < videos.length(); j++) {
                                JSONObject videoObject = videos.optJSONObject(j);
                                Game.Live live = new Game.Live();
                                live.setTitle(videoObject.optString("title"));
                                live.setUrl(videoObject.optString("url"));
                                lives.add(live);
                                liveMap.put(live.getUrl(), live);
                            }

                            try {
                                Element element = document.select(String.format("ul:matches(%s)", String.format("(.*%s.*%s.*)|(.*%s.*%s.*)", game.getName(), game.getName2(), game.getName2(), game.getName()))).first();
                                if (element != null) {
                                    Element li = element.getElementsByClass("t5").first();
                                    Elements as = li.getElementsByTag("a");
                                    for (Element a : as) {
                                        String title = a.text();
                                        String url = "http://www.tiantian.tv/" + a.attr("href");
                                        if (!liveMap.containsKey(url)) {
                                            Game.Live live = new Game.Live();
                                            live.setTitle(title);
                                            live.setUrl(url);
                                            lives.add(live);
                                            liveMap.put(live.getUrl(), live);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            game.setLives(lives);

                            User user = userRepo.findByName(game.getName());
                            User user2 = userRepo.findByName(game.getName2());
                            game.setUid(user != null ? user.getId() : 0);
                            game.setUid2(user2 != null ? user2.getId() : 0);

                            gameRepo.saveAndFlush(game);
                            games.add(game);


                        } catch (Exception e) {
                            e.printStackTrace();
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
