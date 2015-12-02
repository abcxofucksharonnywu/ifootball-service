package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.repos.UserRepo;
import com.abcxo.ifootball.service.utils.Constants;
import com.abcxo.ifootball.service.utils.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shadow on 15/11/30.
 */
@Component
public class UserTask implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    public UserRepo userRepo;


    @Autowired
    public TweetTask tweetTask;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        runInitUsers();
        tweetTask.runInitTweets();
    }

    public void runInitUsers() {
        userRepo.deleteByUserType(User.UserType.TEAM);
        userRepo.deleteByUserType(User.UserType.PUBLIC);
        List<User> users = new ArrayList<>();
        //英超
        users.addAll(runGrepTeam(Constants.GROUP_YINGCHAO, "yingchao"));
        users.add(runInitNews(
                        Constants.NEWS_YINGCHAO,
                        "英格兰足球超级联赛（Premier League），通常简称“英超，是英格兰足球总会属下的职业足球联赛，欧洲五大联赛之一，由20支球队组成。由超级联盟负责具体运作。",
                        "http://b.hiphotos.baidu.com/baike/s%3D220/sign=5f8cb155fffaaf5180e386bdbc5594ed/7e3e6709c93d70cfaa50b37efadcd100baa12b1e.jpg",
                        "http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg")
        );


        //西甲
        users.addAll(runGrepTeam(Constants.GROUP_XIJIA, "xijia"));
        users.add(runInitNews(
                        Constants.NEWS_XIJIA,
                        "西班牙足球甲级联赛（Primera división de Liga，简称 La Liga）在中国则一般简称为“西甲”，是西班牙最高等级的职业足球联赛，也是欧洲及世界最高水平的职业足球联赛之一。",
                        "http://c.hiphotos.baidu.com/baike/w%3D268/sign=9b957073d72a60595210e61c1035342d/d1a20cf431adcbefbc93e2bcacaf2edda2cc9fe1.jpg",
                        "http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg")
        );


        users.addAll(runGrepTeam(Constants.GROUP_DEJIA, "dejia"));
        users.add(runInitNews(
                        Constants.NEWS_DEJIA,
                        "德国足球甲级联赛（德语：Bundesliga）简称德甲，是德国足球最高等级的赛事类别，由德国足球协会于1962年7月28日在多特蒙德确立，自1963-64赛季面世。",
                        "http://b.hiphotos.baidu.com/baike/w%3D268/sign=ca0bd14fd758ccbf1bbcb23c21dabcd4/c9fcc3cec3fdfc03eb909b45d23f8794a5c22666.jpg",
                        "http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg")
        );

        users.addAll(runGrepTeam(Constants.GROUP_YIJIA, "yijia"));
        users.add(runInitNews(
                        Constants.NEWS_YIJIA,
                        "意大利足球甲级联赛（意大利语：Serie A），简称“意甲”，是意大利最高等级的职业足球联赛，由意大利足球协会（Federazione Italiana Giuoco Calcio，FIGC）管理。",
                        "http://pic51.nipic.com/file/20141103/9871522_113912931580_2.png",
                        "http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg")
        );

        users.addAll(runGrepTeam(Constants.GROUP_FAJIA, "fajia"));
        users.add(runInitNews(
                        Constants.NEWS_FAJIA,
                        "法国足球甲级联赛（Championnat de France de football Ligue 1）法语通常简写为 Ligue 1或者是 L1，中文通常简称“法甲”）是法国最高级别的职业足球联赛，是由法国足球协会所组织。",
                        "http://a.hiphotos.baidu.com/baike/w%3D268/sign=7e082adf9652982205333ec5efca7b3b/e850352ac65c10386b68d8dab4119313b07e8997.jpg",
                        "http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg")
        );

        users.addAll(runGrepTeam(Constants.GROUP_ZHONGCHAO, "zhongchao"));
        users.add(runInitNews(
                        Constants.NEWS_ZHONGCHAO,
                        "中国足球协会超级联赛（Chinese Football Association Super League），简称“中超”。由中国足球协会组织，是中国大陆地区最高级别的职业足球联赛（中国港澳台有各自的联赛）。",
                        "http://b.hiphotos.baidu.com/baike/w%3D268/sign=c4f28f0e87cb39dbc1c06050e81709a7/0b55b319ebc4b7455a9f5cf2c9fc1e178b8215ce.jpg",
                        "http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg")
        );

        users.add(runInitNews(
                        Constants.NEWS_OUGUAN,
                        "欧洲冠军联赛，简称欧冠，是欧洲足球协会联盟主办的年度足球比赛，代表欧洲俱乐部足球最高荣誉和水平，被认为是全世界最高素质，最具影响力以及最高水平的俱乐部赛事，亦是世界上奖金最高的足球赛事和体育赛事之一，估计每届赛事约有超过十亿电视观众通过人造卫星观看赛事。",
                        "http://d.hiphotos.baidu.com/baike/w%3D268/sign=0cb200f70855b3199cf985737ba88286/e824b899a9014c08cd776453087b02087af4f4fc.jpg",
                        "http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg")
        );


        users.add(runInitNews(
                        Constants.NEWS_HUABIAN,
                        "搜罗最全面的欧洲五大联赛，欧冠，亚冠，中超有趣好玩的球队球员花边新闻。",
                        "http://bigtu.eastday.com/img/201205/08/75/11869850579652320175.jpg",
                        "http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg")
        );

        //特殊帐号
        users.add(runInitPublic(
                        Constants.PUBLIC_ZHONGDA,
                        "最新最快最准确的足球快讯，欧洲足球新闻一手掌握。",
                        "http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=7e73edad9d22720e7b9beafe4efb2670/79f0f736afc379317dd1cee6eec4b74542a91147.jpg",
                        "http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg")
        );

        System.out.println("user init complete " + users.size());
    }

    private List<User> runGrepTeam(String groupName, String url) {
        List<User> users = new ArrayList<>();
        try {
            String host = "http://www.uhchina.com";
            Document doc = Utils.getDocument(String.format("%s/%sqiudui/", host, url));
            Elements pics = doc.getElementsByClass("picframelist").first().children();
            for (Element element : pics) {
                Element link = element.getElementsByTag("a").first();
                String img = host + link.getElementsByTag("img").first().attr("src");
                String title = link.getElementsByTag("span").first().text();
                User user = new User();
                user.setGroupName(groupName);
                user.setEmail(String.format("%s@ifootball.com", title));
                user.setName(title);
                user.setSign("西甲一个人过21个人的那个门将就是我...");
                user.setAvatar(img);
                user.setCover("http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg");
                user.setGender(User.GenderType.MALE);
                user.setUserType(User.UserType.TEAM);
                userRepo.save(user);
                users.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    private User runInitNews(String name, String sign, String avatar, String cover) {
        User user = new User();
        user.setEmail(String.format("%s@ifootball.com", name));
        user.setName(name);
        user.setSign(sign);
        user.setAvatar(avatar);
        user.setCover(cover);
        user.setGender(User.GenderType.MALE);
        user.setUserType(User.UserType.NEWS);
        user.setGroupName(Constants.GROUP_NEWS);
        userRepo.save(user);
        return user;
    }

    private User runInitPublic(String name, String sign, String avatar, String cover) {
        User user = new User();
        user.setEmail(String.format("%s@ifootball.com", name));
        user.setName(name);
        user.setSign(sign);
        user.setAvatar(avatar);
        user.setCover(cover);
        user.setGender(User.GenderType.MALE);
        user.setUserType(User.UserType.PUBLIC);
        user.setGroupName(Constants.GROUP_PUBLIC);
        userRepo.save(user);
        return user;
    }


}
