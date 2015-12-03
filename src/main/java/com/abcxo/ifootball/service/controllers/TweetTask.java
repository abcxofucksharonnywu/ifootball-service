package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.Tweet;
import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.models.UserTweet;
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

import java.util.*;

/**
 * Created by shadow on 15/11/30.
 */
@Component
public class TweetTask {
    @Autowired
    public UserRepo userRepo;
    @Autowired
    public TweetRepo tweetRepo;
    @Autowired
    public UserTweetRepo userTweetRepo;

    public Map<String, String> hupuUrls = new HashMap<>();

    public TweetTask() {
        hupuUrls.put("切尔西", "/soccer/tag/344.html");
//        hupuUrls.put("曼联", "/soccer/tag/342.html");
//        hupuUrls.put("曼城", "/soccer/tag/120.html");
//        hupuUrls.put("阿森纳", "/soccer/tag/287.html");
//        hupuUrls.put("利物浦", "/soccer/tag/343.html");
//        hupuUrls.put("热刺", "/soccer/tag/496.html");
//
//        hupuUrls.put("皇马", "/soccer/tag/396.html");
//        hupuUrls.put("巴萨", "/soccer/tag/380.html");
//        hupuUrls.put("马竞技", "/soccer/tag/603.html");
//
//        hupuUrls.put("AC米兰", "/soccer/tag/229.html");
//        hupuUrls.put("国米", "/soccer/tag/969.html");
//        hupuUrls.put("尤文", "/soccer/tag/261.html");
//        hupuUrls.put("罗马", "/soccer/tag/495.html");
//        hupuUrls.put("那不勒", "/soccer/tag/700.html");
//
//        hupuUrls.put("拜仁", "/soccer/tag/1341.html");
//        hupuUrls.put("多特", "/soccer/tag/487.html");
//
//        hupuUrls.put("日尔曼", "/soccer/tag/465.html");
//
////        hupuUrls.put("埃因霍温", "/soccer/tag/16623.html");
////        hupuUrls.put("本菲卡", "/soccer/tag/6502.html");
////        hupuUrls.put("波尔图", "/soccer/tag/1388.html");
////        hupuUrls.put("阿贾克斯", "/soccer/tag/1121.html");
//
//        hupuUrls.put("广州恒大淘宝", "/china/tag/11654.html");
//        hupuUrls.put("上海上港", "/china/tag/12136.html");
//        hupuUrls.put("北京国安", "/china/tag/11794.html");
//        hupuUrls.put("山东鲁能", "/china/tag/11676.html");
//        hupuUrls.put("上海绿地申花", "/china/tag/11633.html");

    }

    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitTweets() {
        List<Tweet> tweets = new ArrayList<>();
//        tweets.addAll(runGrepNewsInZhiboba());
//        tweets.addAll(runGrepPublicInDongqiudi(Constants.PUBLIC_ZHONGDA, "/?tab=1"));

        //球队虎扑
        for (Map.Entry<String, String> entry : hupuUrls.entrySet()) {
            tweets.addAll(runGrepTeamInHupu(entry.getKey(), entry.getValue()));
        }
        System.out.println("tweet init complete");
    }


    public List<Tweet> runGrepPublicInDongqiudi(String name, String label) {
        List<Tweet> tweets = new ArrayList<>();
        try {
            User user = userRepo.findByName(name);
            String host = "http://www.dongqiudi.com";
            Document root = Utils.getDocument(String.format("%s%s", host, label));
            Elements list = root.getElementById("news_list").child(0).children();

            int max = 30;
            int summaryMax = 70;
            int i = 0;
            for (Element child : list) {
                if (i < max) {
                    String link = host + child.getElementsByTag("a").first().attr("href");
                    Document article = Utils.getDocument(link);
                    Element detailEl = article.getElementsByClass("detail").first();
                    Element contentEl = detailEl.getElementsByTag("div").get(1);
                    String title = detailEl.getElementsByTag("h1").first().text();
                    String time = detailEl.getElementsByClass("time").first().text();
                    String source = detailEl.getElementsByClass("name").first().text();
                    String text = contentEl.text();
                    String summary = text.substring(0, text.length() > summaryMax ? summaryMax : text.length() - 1);

                    if (tweetRepo.findByTitle(title) == null) {
                        Tweet tweet = new Tweet();
                        tweet.setUid(user.getId());
                        tweet.setIcon(user.getAvatar());
                        tweet.setName(user.getName());

                        tweet.setTitle(title);
                        tweet.setSummary(summary);
                        Elements imgEls = contentEl.getElementsByTag("img");
                        List<String> imgs = new ArrayList<>();
                        for (Element imgEl : imgEls) {
                            String img = imgEl.attr("src");
                            imgs.add(img);
                        }
                        tweet.setImages(String.join(";", imgs));
                        tweet.setContent(Utils.content(Constants.TWEET_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, contentEl.toString())));

                        tweet.setSource(source);
                        tweet.setTime(time);
                        tweet.setDate(new Date().getTime());
                        tweet.setTweetType(Tweet.TweetType.PUBLIC);
                        tweet = tweetRepo.saveAndFlush(tweet);

                        UserTweet userTweet = new UserTweet();
                        userTweet.setUid(user.getId());
                        userTweet.setTid(tweet.getId());
                        userTweet.setUserTweetType(UserTweet.UserTweetType.ADD);
                        userTweetRepo.saveAndFlush(userTweet);
                        tweets.add(tweet);
                    }
                    i++;
                } else {
                    break;
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return tweets;
    }


    public List<Tweet> runGrepNewsInZhiboba() {
        List<Tweet> tweets = new ArrayList<>();
        try {

            String host = "http://news.zhibo8.cc/zuqiu/more.htm?label=";
            Document root = Utils.getDocument(host);
            Elements list = root.getElementsByClass("articleList");

            int max = 200;
            int summaryMax = 70;
            int i = 0;
            for (Element element : list) {
                for (Element child : element.children()) {
                    if (i < max) {
                        String tag = child.attr("data-label");
                        Element titleEl = child.getElementsByClass("articleTitle").first();
                        String title = titleEl.getElementsByTag("a").first().text();
                        String link = titleEl.getElementsByTag("a").first().attr("href");
                        String source = child.getElementsByClass("source").first().text();
                        String time = child.getElementsByClass("postTime").first().text();
                        Document article = Utils.getDocument(link);
                        Element contentEl = article.getElementsByClass("content").first();
                        String text = contentEl.text();
                        String summary = text.substring(0, text.length() > summaryMax ? summaryMax : text.length() - 1);
                        if (tweetRepo.findByTitle(title) == null) {
                            String name;
                            if (tag.contains(Constants.GROUP_YINGCHAO)) {
                                name = Constants.NEWS_YINGCHAO;
                            } else if (tag.contains(Constants.GROUP_XIJIA)) {
                                name = Constants.NEWS_XIJIA;
                            } else if (tag.contains(Constants.GROUP_DEJIA)) {
                                name = Constants.NEWS_DEJIA;
                            } else if (tag.contains(Constants.GROUP_YIJIA)) {
                                name = Constants.NEWS_YIJIA;
                            } else if (tag.contains(Constants.GROUP_FAJIA)) {
                                name = Constants.NEWS_FAJIA;
                            } else if (tag.contains(Constants.GROUP_ZHONGCHAO)) {
                                name = Constants.NEWS_ZHONGCHAO;
                            } else if (tag.contains("花边")) {
                                name = Constants.NEWS_HUABIAN;
                            } else if (tag.contains("欧冠")) {
                                name = Constants.NEWS_OUGUAN;
                            } else {
                                continue;
                            }
                            User user = userRepo.findByName(name);


                            Tweet tweet = new Tweet();
                            tweet.setUid(user.getId());
                            tweet.setIcon(user.getAvatar());
                            tweet.setName(user.getName());

                            tweet.setTitle(title);
                            tweet.setSummary(summary);
                            Elements imgEls = contentEl.getElementsByTag("img");
                            List<String> imgs = new ArrayList<>();
                            for (Element imgEl : imgEls) {
                                String img = imgEl.attr("src");
                                imgs.add(img);
                            }
                            tweet.setImages(String.join(";", imgs));
                            tweet.setContent(Utils.content(Constants.TWEET_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, contentEl.toString())));

                            tweet.setSource(source);
                            tweet.setTime(time);
                            tweet.setDate(new Date().getTime());
                            tweet.setTweetType(Tweet.TweetType.NEWS);
                            tweet = tweetRepo.saveAndFlush(tweet);

                            UserTweet userTweet = new UserTweet();
                            userTweet.setUid(user.getId());
                            userTweet.setTid(tweet.getId());
                            userTweet.setUserTweetType(UserTweet.UserTweetType.ADD);
                            userTweetRepo.saveAndFlush(userTweet);
                            tweets.add(tweet);
                        }
                        i++;
                    } else {
                        break;
                    }


                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return tweets;
    }


    public List<Tweet> runGrepTeamInHupu(String name, String url) {
        List<Tweet> tweets = new ArrayList<>();
        try {
            User user = userRepo.findByName(name);
            String host = "http://voice.hupu.com";
            Document root = Utils.getDocument(String.format("%s%s", host, url));
            Elements list = root.getElementsByClass("list");

            int max = 30;
            int summaryMax = 70;
            int i = 0;
            for (Element element : list) {
                if (i < max) {
                    Element titleEl = element.getElementsByClass("txt").first().getElementsByClass("video").first();
                    String link = titleEl.getElementsByTag("a").first().attr("href");
                    Document article = Utils.getDocument(link);
                    String title = article.getElementsByClass("headline").first().text();
                    String source = article.getElementsByClass("comeFrom").first().getElementsByTag("a").first().text();
                    String time = article.getElementById("pubtime_baidu").text();
                    Element contentEl = article.getElementsByClass("artical-content").first();
                    String text = contentEl.text();
                    String summary = text.substring(0, text.length() > summaryMax ? summaryMax : text.length() - 1);
                    if (tweetRepo.findByTitle(title) == null) {
                        Tweet tweet = new Tweet();
                        tweet.setUid(user.getId());
                        tweet.setIcon(user.getAvatar());
                        tweet.setName(user.getName());

                        tweet.setTitle(title);
                        tweet.setSummary(summary);
                        Elements imgEls = contentEl.getElementsByTag("img");
                        List<String> imgs = new ArrayList<>();
                        for (Element imgEl : imgEls) {
                            String img = imgEl.attr("src");
                            imgs.add(img);
                        }
                        tweet.setImages(String.join(";", imgs));
                        tweet.setContent(Utils.content(Constants.TWEET_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, contentEl.toString())));

                        tweet.setSource(source);
                        tweet.setTime(time);
                        tweet.setDate(new Date().getTime());
                        tweet.setTweetType(Tweet.TweetType.TEAM);
                        tweet = tweetRepo.saveAndFlush(tweet);

                        UserTweet userTweet = new UserTweet();
                        userTweet.setUid(user.getId());
                        userTweet.setTid(tweet.getId());
                        userTweet.setUserTweetType(UserTweet.UserTweetType.ADD);
                        userTweetRepo.saveAndFlush(userTweet);
                        tweets.add(tweet);
                    }
                    i++;
                } else {
                    break;
                }

            }


        } catch (Exception e) {
            System.out.println("grep team news " + name);
            e.printStackTrace();
        }
        return tweets;
    }


}
