package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.Tweet;
import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.models.UserTweet;
import com.abcxo.ifootball.service.repos.TweetRepo;
import com.abcxo.ifootball.service.repos.UserRepo;
import com.abcxo.ifootball.service.repos.UserTweetRepo;
import com.abcxo.ifootball.service.utils.Constants;
import com.abcxo.ifootball.service.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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


    public void runInitTweets() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepNewsInZhiboba());
        tweets.addAll(runGrepPublicInDongqiudi(Constants.PUBLIC_IMPORTANT, "/?tab=1"));
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
                    Element contentEl = detailEl.getElementsByTag("div").first();
                    String title = contentEl.getElementsByTag("h1").first().text();
                    String time = contentEl.getElementsByClass("time").first().text();
                    String source = contentEl.getElementsByClass("name").first().text();
                    String summary = contentEl.text().substring(0, summaryMax);

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
                        tweet.setContent(Utils.content(String.format(Constants.TWEET_HTML, contentEl.toString())));

                        tweet.setSource(source);
                        tweet.setTime(time);
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
                        String summary = contentEl.text().substring(0, summaryMax);
                        if (tweetRepo.findByTitle(title) == null) {
                            String name ;
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
                            tweet.setContent(Utils.content(String.format(Constants.TWEET_HTML, contentEl.toString())));

                            tweet.setSource(source);
                            tweet.setTime(time);
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


}
