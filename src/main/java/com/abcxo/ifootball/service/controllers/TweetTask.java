package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.Tweet;
import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.models.UserTweet;
import com.abcxo.ifootball.service.repos.TweetRepo;
import com.abcxo.ifootball.service.repos.UserRepo;
import com.abcxo.ifootball.service.repos.UserTweetRepo;
import com.abcxo.ifootball.service.utils.Constants;
import com.abcxo.ifootball.service.utils.Utils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    //视频新闻
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 8 * 60 * 1000)
    public void runInitInVideo() {
        System.out.println("tweet runInitInVideo " + " begin");
        runGrepPublicInDongqiudi();
        System.out.println("tweet runInitInVideo ");

    }


    //花边新闻
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 10 * 60 * 1000)
    public void runInitInHuabian() {
        System.out.println("tweet runInitInHuabian " + " begin");
        runGrepHuabianInTencent();
        System.out.println("tweet runInitInHuabian ");


    }


    //球队新闻及花边新闻
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 12 * 60 * 1000)
    public void runInitInZhiboba() {
        System.out.println("tweet runInitInZhiboba " + " begin");
        runGrepNewsInZhiboba();
        System.out.println("tweet runInitInZhiboba ");

    }


    @Scheduled(fixedDelay = 20 * 60 * 1000, initialDelay = 10 * 60 * 1000)
    public void runInitWeiBo1() {
        runGrepInWeibo("切尔西", "/chelseafc?is_all=1");
        runGrepInWeibo("曼联", "/manchesterunited?profile_ftype=1&is_all=1&noscale_head=1");
        runGrepInWeibo("曼城", "/mcfcofficial?profile_ftype=1&is_all=1");
        runGrepInWeibo("阿森纳", "/OfficialArsenal?profile_ftype=1&is_all=1");
        runGrepInWeibo("皇家马德里", "/realmadridcf?profile_ftype=1&amp;is_all=1");
        runGrepInWeibo("巴塞罗那", "/u/3296530723?topnav=1&wvr=6&topsug=1&is_hot=1");
        runGrepInWeibo("马德里竞技", "/atleticodemadrid?profile_ftype=1&is_all=1");
        runGrepInWeibo("尤文图斯", "/u/2796878611?topnav=1&wvr=6&topsug=1&is_hot=1");
        runGrepInWeibo("拜仁慕尼黑", "/fcbayern?profile_ftype=1&is_all=1");
        runGrepInWeibo("巴黎圣日耳曼", "/ParisSaintGermainFC?profile_ftype=1&is_all=1");
        runGrepInWeibo("广州恒大淘宝", "/gzevergrandefc?is_all=1");
        runGrepInWeibo("上海上港", "/u/3593916900?profile_ftype=1&is_all=1");
        System.out.println("tweet runInitWeiBo1 ");
    }

    @Scheduled(fixedDelay = 30 * 60 * 1000, initialDelay = 20 * 60 * 1000)
    public void runInitWeiBo2() {
        runGrepInWeibo("利物浦", "/liverpoolfc?profile_ftype=1&is_all=1");
        runGrepInWeibo("托特纳姆热刺", "/tottenhamhotspur?profile_ftype=1&is_all=1");
        runGrepInWeibo("AC米兰", "/acmilanofficial?profile_ftype=1&is_all=1");
        runGrepInWeibo("国际米兰", "/fcinternazionale?profile_ftype=1&is_all=1");
        runGrepInWeibo("那不勒斯", "/u/2869364232");
        runGrepInWeibo("多特蒙德", "/BVBorussiaDortmund09?profile_ftype=1&is_all=1");
        runGrepInWeibo("巴黎圣日耳曼", "/ParisSaintGermainFC?profile_ftype=1&is_all=1");
        runGrepInWeibo("北京国安", "/guoanfootballclub?profile_ftype=1&is_all=1");
        runGrepInWeibo("山东鲁能", "/lntsfc?profile_ftype=1&is_all=1\n");
    }

    @Scheduled(fixedDelay = 40 * 60 * 1000, initialDelay = 30 * 60 * 1000)
    public void runInitWeiBo3() {
        runGrepInWeibo("罗马", "/officialasroma?profile_ftype=1&amp;is_all=1");
        runGrepInWeibo("那不勒斯", "/u/2869364232");
        runGrepInWeibo("沃尔夫斯堡", "/vflwolfsburg?profile_ftype=1&amp;is_all=1");
        runGrepInWeibo("里昂", "/oltitan24?profile_ftype=1&is_all=1");
        runGrepInWeibo("上海绿地申花", "/shenhuafc?profile_ftype=1&is_all=1");
    }


    //自定义
    @Scheduled(fixedDelay = 5 * 60 * 60 * 1000, initialDelay = 40 * 60 * 1000)
    public void runInitCustom() {
        System.out.println("tweet runInitCustom " + " begin");
        try {
            JSONArray json = new JSONArray(Utils.getGrepString());
            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    JSONObject object = json.optJSONObject(i);
                    String user = object.optString("user");
                    String url = object.optString("url");
                    if (!StringUtils.isEmpty(url)) {
                        runGrepInWeibo(user, url);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("tweet runInitCustom ");

    }


    //    //切尔西
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 14 * 60 * 1000)
    public void runInitQieerxi() {
        System.out.println("tweet runInitQieerxi " + " begin");
        runGrepTeamInHupu("切尔西", "/soccer/tag/344.html");
        System.out.println("tweet runInitQieerxi ");


    }


    //曼联
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 16 * 60 * 1000)
    public void runInitManlian() {
        System.out.println("tweet runInitManlian " + " begin");
        runGrepTeamInHupu("曼联", "/soccer/tag/342.html");
        System.out.println("tweet runInitManlian ");


    }

    //曼城
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 18 * 60 * 1000)
    public void runInitMancheng() {
        System.out.println("tweet runInitMancheng " + " begin");
        runGrepTeamInHupu("曼城", "/soccer/tag/120.html");
        System.out.println("tweet runInitMancheng ");


    }

    //阿森纳
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 20 * 60 * 1000)
    public void runInitAsengna() {
        System.out.println("tweet runInitAsengna " + " begin");
        runGrepTeamInHupu("阿森纳", "/soccer/tag/287.html");
        System.out.println("tweet runInitAsengna ");


    }

    //利物浦
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 22 * 60 * 1000)
    public void runInitLiwupu() {
        System.out.println("tweet runInitLiwupu " + " begin");
        runGrepTeamInHupu("利物浦", "/soccer/tag/343.html");

        System.out.println("tweet runInitLiwopu ");


    }

    //热刺
    @Scheduled(fixedDelay = 1 * 60 * 60 * 1000, initialDelay = 24 * 60 * 1000)
    public void runInitReci() {
        System.out.println("tweet runInitReci " + " begin");
        runGrepTeamInHupu("托特纳姆热刺", "/soccer/tag/496.html");
        System.out.println("tweet runInitReci ");


    }


    //皇马
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 26 * 60 * 1000)
    public void runInitHuangma() {
        System.out.println("tweet runInitHuangma " + " begin");
        runGrepTeamInHupu("皇家马德里", "/soccer/tag/396.html");
        System.out.println("tweet runInitHuangma ");


    }

    //巴萨
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 28 * 60 * 1000)
    public void runInitBasa() {
        System.out.println("tweet runInitBasa " + " begin");
        runGrepTeamInHupu("巴塞罗那", "/soccer/tag/380.html");
        System.out.println("tweet runInitBasa ");


    }

    //马竞技
    @Scheduled(fixedDelay = 1 * 60 * 60 * 1000, initialDelay = 30 * 60 * 1000)
    public void runInitMajingji() {
        System.out.println("tweet runInitMajingji " + " begin");
        runGrepTeamInHupu("马德里竞技", "/soccer/tag/603.html");
        System.out.println("tweet runInitMajingji ");


    }


    //AC米兰
    @Scheduled(fixedDelay = 1 * 60 * 60 * 1000, initialDelay = 32 * 60 * 1000)
    public void runInitAC() {
        System.out.println("tweet runInitAC " + " begin");
        runGrepTeamInHupu("AC米兰", "/soccer/tag/229.html");
        System.out.println("tweet runInitAC ");


    }


    //国米
    @Scheduled(fixedDelay = 1 * 60 * 60 * 1000, initialDelay = 34 * 60 * 1000)
    public void runInitGuomi() {
        System.out.println("tweet runInitGuomi " + " begin");
        runGrepTeamInHupu("国际米兰", "/soccer/tag/969.html");
        System.out.println("tweet runInitGuomi ");


    }


    //尤文
    @Scheduled(fixedDelay = 1 * 60 * 60 * 1000, initialDelay = 36 * 60 * 1000)
    public void runInitYouwen() {
        System.out.println("tweet runInitYouwen " + " begin");
        runGrepTeamInHupu("尤文图斯", "/soccer/tag/261.html");
        System.out.println("tweet runInitYouwen ");


    }


    //罗马
    @Scheduled(fixedDelay = 2 * 60 * 60 * 1000, initialDelay = 38 * 60 * 1000)
    public void runInitLuoma() {
        System.out.println("tweet runInitLuoma " + " begin");
        runGrepTeamInHupu("罗马", "/soccer/tag/495.html");
        System.out.println("tweet runInitLuoma ");


    }


    //那不勒
    @Scheduled(fixedDelay = 2 * 60 * 60 * 1000, initialDelay = 40 * 60 * 1000)
    public void runInitNabule() {
        System.out.println("tweet runInitNabule " + " begin");
        runGrepTeamInHupu("那不勒斯", "/soccer/tag/700.html");
        System.out.println("tweet runInitNabule ");


    }


    //拜仁
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 42 * 60 * 1000)
    public void runInitBairen() {
        System.out.println("tweet runInitBairen " + " begin");
        runGrepTeamInHupu("拜仁慕尼黑", "/soccer/tag/1341.html");
        System.out.println("tweet runInitBairen ");


    }

    //多特
    @Scheduled(fixedDelay = 1 * 60 * 60 * 1000, initialDelay = 44 * 60 * 1000)
    public void runInitDuote() {
        System.out.println("tweet runInitDuote " + " begin");
        runGrepTeamInHupu("多特蒙德", "/soccer/tag/487.html");
        System.out.println("tweet runInitDuote ");


    }

    //沃尔夫
    @Scheduled(fixedDelay = 2 * 60 * 60 * 1000, initialDelay = 46 * 60 * 1000)
    public void runInitWoerfu() {
        System.out.println("tweet runInitWoerfu " + " begin");
        System.out.println("tweet runInitWoerfu ");


    }

    //日尔曼
    @Scheduled(fixedDelay = 1 * 60 * 60 * 1000, initialDelay = 48 * 60 * 1000)
    public void runInitRierman() {
        System.out.println("tweet runInitRierman " + " begin");
        runGrepTeamInHupu("巴黎圣日耳曼", "/soccer/tag/465.html");
        System.out.println("tweet runInitRierman ");


    }

    //里昂
    @Scheduled(fixedDelay = 2 * 60 * 60 * 1000, initialDelay = 50 * 60 * 1000)
    public void runInitLiang() {
        System.out.println("tweet runInitLiang " + " begin");

        System.out.println("tweet runInitLiang ");


    }


    //恒大
    @Scheduled(fixedDelay = 12 * 60 * 60 * 1000, initialDelay = 52 * 60 * 1000)
    public void runInitHengda() {
        System.out.println("tweet runInitHengda " + " begin");

        runGrepTeamInHupu("广州恒大淘宝", "/soccer/tag/11654.html");
        System.out.println("tweet runInitHengda ");


    }


    //上港
    @Scheduled(fixedDelay = 12 * 60 * 60 * 1000, initialDelay = 54 * 60 * 1000)
    public void runInitShanggang() {
        System.out.println("tweet runInitShanggang " + " begin");
        runGrepTeamInHupu("上海上港", "/soccer/tag/12136.html");
        System.out.println("tweet runInitShanggang ");


    }


    //国安
    @Scheduled(fixedDelay = 12 * 60 * 60 * 1000, initialDelay = 56 * 60 * 1000)
    public void runInitGuoan() {
        System.out.println("tweet runInitGuoan " + " begin");

        runGrepTeamInHupu("北京国安", "/soccer/tag/11794.html");
        System.out.println("tweet runInitGuoan ");


    }


    //鲁能
    @Scheduled(fixedDelay = 12 * 60 * 60 * 1000, initialDelay = 58 * 60 * 1000)
    public void runInitLuneng() {
        System.out.println("tweet runInitLuneng " + " begin");
        runGrepTeamInHupu("山东鲁能", "/soccer/tag/11676.html");
        System.out.println("tweet runInitLuneng ");


    }


    //申花
    @Scheduled(fixedDelay = 12 * 60 * 60 * 1000, initialDelay = 60 * 60 * 1000)
    public void runInitShenghua() {
        System.out.println("tweet runInitShenghua " + " begin");
        runGrepTeamInHupu("上海绿地申花", "/soccer/tag/11633.html");
        System.out.println("tweet runInitShenghua ");


    }


    //抓取方法
    public List<Tweet> runGrepPublicInDongqiudi() {
        List<Tweet> tweets = new ArrayList<>();
        try {
            User user = userRepo.findByName(Constants.NEWS_VIDEO);
            String host = "http://www.dongqiudi.com/video";
            Document root = Utils.getDocument(host);
            Elements list = root.getElementById("news_list").getElementsByTag("ol").first().children();

            int max = 30;
            int i = 0;
            for (Element element : list) {
                try {
                    if (i < max) {

                        Element imgEL = element.getElementsByTag("img").first();
                        String image = imgEL.attr("src");
                        String title = imgEL.attr("alt");
                        String url = "http://www.dongqiudi.com" + imgEL.parent().attr("href");
                        Document content = Utils.getDocument(url);
                        Element contentEL = content.getElementsByClass("con").get(0);
                        String time = content.getElementsByClass("time").first().text();
                        String text = contentEL.text().trim();
                        if (tweetRepo.findByTitle(title).size() == 0) {
                            Tweet tweet = new Tweet();
                            tweet.setUid(user.getId());
                            tweet.setIcon(user.getAvatar());
                            tweet.setName(user.getName());
                            tweet.setTitle(title);
                            tweet.setSummary(text);
                            tweet.setContent(Utils.content(Constants.TWEET_HTML_VIDEO.replace(Constants.TWEET_HTML_CONTENT_TAG, contentEL.toString())));
                            tweet.setDate(Utils.getDate(time));
                            tweet.setTime(Utils.getTime(tweet.getDate()));
                            tweet.setImages(image);
                            tweet.setTweetType(Tweet.TweetType.NEWS);
                            tweet.setTweetContentType(Tweet.TweetContentType.VIDEO);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        } catch (Exception e) {
            System.out.println("grep dongqiudi news ");
            e.printStackTrace();
        }
        return tweets;
    }


    public List<Tweet> runGrepHuabianInTencent() {
        List<Tweet> tweets = new ArrayList<>();
        try {
            User user = userRepo.findByName(Constants.NEWS_HUABIAN);
            String url = "http://sports.qq.com/l/isocce/interpics/list.htm";
            Document root = Utils.getDocument(url);
            Elements list = root.getElementById("piclist").children();
            int max = 30;
            int i = 0;
            for (Element element : list) {
                try {
                    if (i < max) {

                        String link = "http://xw.qq.com/sports/" + element.getElementsByTag("a").first().attr("href").replace("http://sports.qq.com/a/", "").replace("/", "").replace("htm", "");
                        Document article = Utils.getDocument(link);
                        String title = article.getElementsByClass("title").first().text().trim();
                        String source = "腾讯体育";
                        String time = article.getElementsByClass("time").text();
                        Elements imgEls = article.getElementsByClass("image");
                        Element contentEl = article.getElementsByClass("content").first();
                        String text = contentEl.text().trim();
                        if (tweetRepo.findByTitle(title).size() == 0) {
                            Tweet tweet = new Tweet();
                            tweet.setUid(user.getId());
                            tweet.setIcon(user.getAvatar());
                            tweet.setName(user.getName());
                            tweet.setTitle(title);

                            List<String> imgs = new ArrayList<>();
                            List<String> titles = new ArrayList<>();
                            for (Element imgEl : imgEls) {
                                String img = imgEl.getElementsByTag("img").first().attr("src");
                                Element p = imgEl.nextElementSibling();
                                String pText = null;
                                if (p != null && !"img".equals(p.tag().getName())) {
                                    pText = p.text();
                                }
                                imgs.add(img);
                                titles.add(!StringUtils.isEmpty(pText) ? pText.trim() : "_");
                            }
                            tweet.setImages(String.join(";", imgs));
                            tweet.setImageTitles(String.join(";", titles));


                            tweet.setSummary(text);
                            tweet.setContent(Utils.content(Constants.TWEET_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, contentEl.toString())));
                            tweet.setSource(source);
                            tweet.setDate(Utils.getDate(time));
                            tweet.setTime(Utils.getTime(tweet.getDate()));
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
                } catch (Exception e) {
                    e.printStackTrace();
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
            int i = 0;
            for (Element element : list) {
                for (Element child : element.children()) {
                    try {
                        if (i < max) {

                            String tag = child.attr("data-label");
                            Element titleEl = child.getElementsByClass("articleTitle").first();
                            String title = titleEl.getElementsByTag("a").first().text();
                            String link = titleEl.getElementsByTag("a").first().attr("href");
                            String source = child.getElementsByClass("source").first().text();
                            String time = child.getElementsByClass("postTime").first().text();
                            Document article = Utils.getDocument(link);
                            Element contentEl = article.getElementsByClass("content").first();
                            String text = contentEl.text().trim();
                            if (tweetRepo.findByTitle(title).size() == 0) {
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
                                tweet.setSummary(text);
                                Elements imgEls = contentEl.getElementsByTag("img");
                                List<String> imgs = new ArrayList<>();
                                for (Element imgEl : imgEls) {
                                    String img = imgEl.attr("src");
                                    imgs.add(img);
                                }
                                tweet.setImages(String.join(";", imgs));
                                tweet.setContent(Utils.content(Constants.TWEET_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, contentEl.toString())));

                                tweet.setSource(source);
                                tweet.setDate(Utils.getDate(time));
                                tweet.setTime(Utils.getTime(tweet.getDate()));
                                tweet.setTweetType(Tweet.TweetType.NEWS);
                                tweet.setTweetContentType(imgs.size() >= 3 ? Tweet.TweetContentType.IMAGES : Tweet.TweetContentType.NORMAL);
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

                    } catch (Exception e) {
                        e.printStackTrace();
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
            int i = 0;
            for (Element element : list) {
                try {
                    if (i < max) {

                        Element titleEl = element.getElementsByClass("txt").first().getElementsByClass("video").first();
                        String link = titleEl.getElementsByTag("a").first().attr("href");
                        Document article = Utils.getDocument(link);
                        String title = article.getElementsByClass("headline").first().text();
                        String source = article.getElementsByClass("comeFrom").first().getElementsByTag("a").first().text();
                        String time = article.getElementById("pubtime_baidu").text();
                        Element contentEl = article.getElementsByClass("artical-content").first();
                        String text = contentEl.text().trim();
                        if (tweetRepo.findByTitle(title).size() == 0) {
                            Tweet tweet = new Tweet();
                            tweet.setUid(user.getId());
                            tweet.setIcon(user.getAvatar());
                            tweet.setName(user.getName());

                            tweet.setTitle(title);
                            tweet.setSummary(text);
                            Elements imgEls = contentEl.getElementsByTag("img");
                            List<String> imgs = new ArrayList<>();
                            for (Element imgEl : imgEls) {
                                String img = imgEl.attr("src");
                                imgs.add(img);
                            }
                            tweet.setImages(String.join(";", imgs));
                            tweet.setContent(Utils.content(Constants.TWEET_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, contentEl.toString())));

                            tweet.setSource(source);
                            tweet.setDate(Utils.getDate(time));
                            tweet.setTime(Utils.getTime(tweet.getDate()));
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
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        } catch (Exception e) {
            System.out.println("grep team news " + name);
            e.printStackTrace();
        }
        return tweets;
    }


    public List<Tweet> runGrepInWeibo(String name, String url) {
        List<Tweet> tweets = new ArrayList<>();
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        try {
            User user = userRepo.findByName(name);
            String host = "http://weibo.com";
            String webUrl = url.startsWith("http") ? url : String.format("%s%s", host, url);
            Map<String, String> map = new HashMap<>();
            //map.put请根据自己的微博cookie得到
            map.put("Apache", "8536937909666.449.1449887302431");
            map.put("SINAGLOBAL", "8536937909666.449.1449887302431");
            map.put("SUB", "_2AkMhNwlxf8NjqwJRmPodzWPkbIl0zQ_EiebDAHzsJxJjHiBG7G3ceUM3HhgndSXTFUTPmJXUFRpT");
            map.put("SUBP", "0033WrSXqPxfM72-Ws9jqgMF55z29P9D9WWDimj.Wx-aUoqIoc.x40eV");
            map.put("ULV", "1449887302437:1:1:1:8536937909666.449.1449887302431:");
            map.put("YF-Ugrow-G0", "5b31332af1361e117ff29bb32e4d8439");
            map.put("_s_tentry", "passport.weibo.com");
            map.put("login_sid_t", "1e9f2f8194194ee5b0daf420316de0cb");


            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getCookieManager().setCookiesEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                webClient.getCookieManager().addCookie(new Cookie(".weibo.com", entry.getKey(), entry.getValue()));
            }
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            HtmlPage page = webClient.getPage(webUrl);
            webClient.waitForBackgroundJavaScript(20000);
            Document root = Jsoup.parse(page.asXml());
            Elements list = root.getElementsByAttributeValue("node-type", "feed_list").first().children();

            int max = 30;
            int i = 0;
            for (Element element : list) {
                try {
                    if (i < max) {

                        Element detail = element.getElementsByClass("WB_detail").first();
                        if (detail != null) {
                            Element content = detail.getElementsByAttributeValue("node-type", "feed_list_content").first();
                            String text = content.text().trim();
                            String source = "新浪微博";
                            String date = detail.getElementsByAttributeValue("node-type", "feed_list_item_date").first().attr("date");
                            if (tweetRepo.findByDate(Long.valueOf(date)) == null) {
                                Tweet tweet = new Tweet();
                                tweet.setUid(user.getId());
                                tweet.setIcon(user.getAvatar());
                                tweet.setName(user.getName());
                                tweet.setSummary(text);

                                Elements imgEls = detail.getElementsByTag("img");
                                List<String> imgs = new ArrayList<>();
                                StringBuffer imageContent = new StringBuffer();
                                for (Element imgEl : imgEls) {
                                    String img = imgEl.attr("src");
                                    if (!"face".equals(imgEl.attr("type"))) {
                                        img = img.replace("thumbnail", "bmiddle").replace("square", "bmiddle");
                                        imgs.add(img);
                                        imageContent.append(Constants.TWEET_ADD_IMAGE_HTML.replace(Constants.TWEET_HTML_IMAGE_TAG, img));

                                    }

                                }
                                tweet.setImages(String.join(";", imgs));


                                tweet.setContent(Utils.content(Constants.TWEET_ADD_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, content.toString()).replace(Constants.TWEET_HTML_IMAGES_TAG, imageContent)));

                                tweet.setSource(source);
                                tweet.setDate(Long.valueOf(date));
                                tweet.setTime(Utils.getTime(tweet.getDate()));
                                tweet.setTweetType(Tweet.TweetType.valueOf(user.getUserType().name()));

                                tweet = tweetRepo.saveAndFlush(tweet);

                                UserTweet userTweet = new UserTweet();
                                userTweet.setUid(user.getId());
                                userTweet.setTid(tweet.getId());
                                userTweet.setUserTweetType(UserTweet.UserTweetType.ADD);
                                userTweetRepo.saveAndFlush(userTweet);
                                tweets.add(tweet);
                            }
                        }

                        i++;
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        } catch (Exception e) {
            System.out.println("grep webo  " + name);
            e.printStackTrace();
        } finally {
            webClient.close();
        }

        return tweets;
    }


}
