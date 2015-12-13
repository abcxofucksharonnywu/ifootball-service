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
import org.jsoup.Jsoup;
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
public class TweetTask {
    @Autowired
    public UserRepo userRepo;
    @Autowired
    public TweetRepo tweetRepo;
    @Autowired
    public UserTweetRepo userTweetRepo;


//    //重大新闻
//    @Scheduled(fixedDelay = 40 * 60 * 1000)
//    public void runInitInDongqiudi() {
//        List<Tweet> tweets = runGrepPublicInDongqiudi(Constants.SPECIAL_BREAK, "/?tab=1");
//        System.out.println("tweet runInitInDongqiudi " + tweets.size());
//    }

    //球队新闻及花边新闻
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitInZhiboba() {
        List<Tweet> tweets = runGrepNewsInZhiboba();
        System.out.println("tweet runInitInZhiboba " + tweets.size());
    }


    //切尔西
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitQieerxi() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("切尔西", "/soccer/tag/344.html"));
        tweets.addAll(runGrepInWeibo("切尔西", "/qieerxibbs"));
        System.out.println("tweet runInitQieerxi " + tweets.size());
    }


    //曼联
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitManlian() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("曼联", "/soccer/tag/342.html"));
        tweets.addAll(runGrepInWeibo("曼联", "/manchesterunited"));
        System.out.println("tweet runInitManlian " + tweets.size());
    }

    //曼城
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitMancheng() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("曼城", "/soccer/tag/120.html"));
        tweets.addAll(runGrepInWeibo("曼城", "/u/2759906397?topnav=1&wvr=6&topsug=1"));
        System.out.println("tweet runInitMancheng " + tweets.size());
    }

    //阿森纳
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitAsengna() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("阿森纳", "/soccer/tag/287.html"));
        tweets.addAll(runGrepInWeibo("阿森纳", "/u/2075686772?topnav=1&wvr=6&topsug=1"));
        System.out.println("tweet runInitAsengna " + tweets.size());
    }

    //利物浦
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitLiwupu() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("利物浦", "/soccer/tag/343.html"));
        tweets.addAll(runGrepInWeibo("利物浦", "/u/2218894100?topnav=1&wvr=6&topsug=1"));
        System.out.println("tweet runInitLiwopu " + tweets.size());
    }

    //热刺
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitReci() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("热刺", "/soccer/tag/496.html"));
        tweets.addAll(runGrepInWeibo("热刺", "/u/3065243713?topnav=1&wvr=6&topsug=1"));
        System.out.println("tweet runInitReci " + tweets.size());
    }


    //皇马
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitHuangma() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("皇马", "/soccer/tag/396.html"));
        tweets.addAll(runGrepInWeibo("皇马", "/realmadridcf"));
        System.out.println("tweet runInitHuangma " + tweets.size());
    }

    //巴萨
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitBasa() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("巴萨", "/soccer/tag/380.html"));
        tweets.addAll(runGrepInWeibo("巴萨", "/u/3296530723?topnav=1&wvr=6&topsug=1&is_hot=1"));
        System.out.println("tweet runInitBasa " + tweets.size());
    }

    //马竞技
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitMajingji() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("马竞技", "/soccer/tag/603.html"));
        tweets.addAll(runGrepInWeibo("马竞技", "/atleticodemadrid"));
        System.out.println("tweet runInitMajingji " + tweets.size());
    }


    //AC米兰
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitAC() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("AC米兰", "/soccer/tag/229.html"));
        tweets.addAll(runGrepInWeibo("AC米兰", "/acmilanofficial"));
        System.out.println("tweet runInitAC " + tweets.size());
    }


    //国米
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitGuomi() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("国米", "/soccer/tag/969.html"));
        tweets.addAll(runGrepInWeibo("国米", "/fcinternazionale"));
        System.out.println("tweet runInitGuomi " + tweets.size());
    }


    //尤文
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitYouwen() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("尤文", "/soccer/tag/261.html"));
        tweets.addAll(runGrepInWeibo("尤文", "/u/2796878611?topnav=1&wvr=6&topsug=1&is_hot=1"));
        System.out.println("tweet runInitYouwen " + tweets.size());
    }


    //罗马
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitLuoma() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("罗马", "/soccer/tag/495.html"));
        tweets.addAll(runGrepInWeibo("罗马", "/officialasroma"));
        System.out.println("tweet runInitLuoma " + tweets.size());
    }


    //那不勒
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitNabule() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("那不勒", "/soccer/tag/700.html"));
        tweets.addAll(runGrepInWeibo("那不勒", "/u/2869364232"));
        System.out.println("tweet runInitNabule " + tweets.size());
    }


    //拜仁
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitBairen() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("拜仁", "/soccer/tag/1341.html"));
        tweets.addAll(runGrepInWeibo("拜仁", "/u/2971183691?topnav=1&wvr=6"));
        System.out.println("tweet runInitBairen " + tweets.size());
    }

    //多特
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitDuote() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("多特", "/soccer/tag/487.html"));
        tweets.addAll(runGrepInWeibo("多特", "/BVBorussiaDortmund09"));
        System.out.println("tweet runInitDuote " + tweets.size());
    }

    //沃尔夫
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitWoerfu() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepInWeibo("沃尔夫", "/u/3945922133?topnav=1&wvr=6"));
        System.out.println("tweet runInitWoerfu " + tweets.size());
    }

    //日尔曼
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitRierman() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("日尔曼", "/soccer/tag/465.html"));
        tweets.addAll(runGrepInWeibo("日尔曼", "/ParisSaintGermainFC"));
        System.out.println("tweet runInitRierman " + tweets.size());
    }

    //里昂
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitLiang() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepInWeibo("里昂", "/ParisSaintGermainFC"));
        System.out.println("tweet runInitLiang " + tweets.size());
    }



    //恒大
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitHengda() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("广州恒大淘宝", "/soccer/tag/11654.html"));
        tweets.addAll(runGrepInWeibo("广州恒大淘宝", "/u/1894798092?topnav=1&wvr=6"));
        System.out.println("tweet runInitHengda " + tweets.size());
    }


    //上港
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitShanggang() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("上海上港", "/soccer/tag/12136.html"));
        tweets.addAll(runGrepInWeibo("上海上港", "/u/3593916900?topnav=1&wvr=6"));
        System.out.println("tweet runInitShanggang " + tweets.size());
    }


    //国安
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitGuoan() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("北京国安", "/soccer/tag/11794.html"));
        tweets.addAll(runGrepInWeibo("北京国安", "/u/1912052995?topnav=1&wvr=6"));
        System.out.println("tweet runInitGuoan " + tweets.size());
    }


    //鲁能
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitLuneng() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("山东鲁能", "/soccer/tag/11676.html"));
        tweets.addAll(runGrepInWeibo("山东鲁能", "/u/1911180293?topnav=1&wvr=6"));
        System.out.println("tweet runInitLuneng " + tweets.size());
    }


    //申花
    @Scheduled(fixedDelay = 40 * 60 * 1000)
    public void runInitShenghua() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.addAll(runGrepTeamInHupu("上海绿地申花", "/soccer/tag/11633.html"));
        tweets.addAll(runGrepInWeibo("上海绿地申花", "/u/1650107735?topnav=1&wvr=6&topsug=1"));
        System.out.println("tweet runInitShenghua " + tweets.size());
    }


    //抓取方法
    public List<Tweet> runGrepPublicInDongqiudi(String name, String label) {
        List<Tweet> tweets = new ArrayList<>();
        try {
            User user = userRepo.findByName(name);
            String host = "http://www.dongqiudi.com";
            Document root = Utils.getDocument(String.format("%s%s", host, label));
            Elements list = root.getElementById("news_list").child(0).children();

            int max = 30;
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
                    if (tweetRepo.findByTitle(title) == null) {
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
                        tweet.setTweetType(Tweet.TweetType.SPECIAL);
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
                    if (tweetRepo.findByTitle(title) == null) {
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

            }


        } catch (Exception e) {
            System.out.println("grep team news " + name);
            e.printStackTrace();
        }
        return tweets;
    }


    public List<Tweet> runGrepInWeibo(String name, String url) {
        List<Tweet> tweets = new ArrayList<>();
        try {
            User user = userRepo.findByName(name);
            String host = "http://weibo.com";
            String webUrl = String.format("%s%s", host, url);
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

            WebClient webClient = new WebClient(BrowserVersion.CHROME);
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
            webClient.waitForBackgroundJavaScript(5000);
            Document root = Jsoup.parse(page.asXml());
            Elements list = root.getElementsByAttributeValue("node-type", "feed_list").first().children();

            int max = 30;
            int i = 0;
            for (Element element : list) {
                if (i < max) {
                    Element detail = element.getElementsByClass("WB_detail").first();
                    if (detail != null) {
                        Element content = detail.getElementsByAttributeValue("node-type", "feed_list_content").first();
                        String text = content.text();
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
                                img = img.replace("thumbnail", "bmiddle").replace("square", "bmiddle");
                                imgs.add(img);
                                imageContent.append(String.format(Constants.TWEET_ADD_IMAGE_HTML, img));
                            }
                            tweet.setImages(String.join(";", imgs));

                            tweet.setContent(Utils.content(Constants.TWEET_ADD_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, text).replace(Constants.TWEET_HTML_IMAGES_TAG, imageContent)));

                            tweet.setSource(source);
                            tweet.setDate(Long.valueOf(date));
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
                    }

                    i++;
                } else {
                    break;
                }

            }


        } catch (Exception e) {
            System.out.println("grep webo  " + name);
            e.printStackTrace();
        }

        return tweets;
    }



}
