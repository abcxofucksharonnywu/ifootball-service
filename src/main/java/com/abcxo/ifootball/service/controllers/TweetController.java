package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.*;
import com.abcxo.ifootball.service.repos.*;
import com.abcxo.ifootball.service.utils.Constants;
import com.abcxo.ifootball.service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shadow on 15/11/15.
 */
@RestController
public class TweetController {
    @Autowired
    private TweetRepo tweetRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TweetTweetRepo tweetTweetRepo;
    @Autowired
    private UserTweetRepo userTweetRepo;
    @Autowired
    private UserUserRepo userUserRepo;
    @Autowired
    private MessageRepo messageRepo;

    //新建Tweet
    @RequestMapping(value = "/tweet", method = RequestMethod.POST)
    public Tweet add(@RequestParam("prompt") String prompt,
                     @RequestParam("originTid") long originTid,
                     @RequestBody Tweet tweet) {

        String content = tweet.getContent();
        tweet.setSummary(content);
        tweet.setContent(Utils.content(String.format(Constants.TWEET_ADD_HTML, content, "")));
        tweet.setTime(Utils.getTime());
        tweet.setDate(new Date().getTime());
        tweet = tweetRepo.saveAndFlush(tweet);

        //保持userTweet
        long uid = tweet.getUid();
        User user = userRepo.findOne(uid);
        UserTweet userTweet = new UserTweet();
        userTweet.setUid(uid);
        userTweet.setTid(tweet.getId());
        userTweet.setUserTweetType(UserTweet.UserTweetType.ADD);
        userTweetRepo.saveAndFlush(userTweet);

        //保存@ 并且发送message
        if (!StringUtils.isEmpty(prompt)) {
            String[] uids = prompt.split(";");
            for (String id : uids) {
                //保存@
                UserTweet ut = new UserTweet();
                userTweet.setUid(Long.valueOf(id));
                userTweet.setTid(tweet.getId());
                userTweet.setUserTweetType(UserTweet.UserTweetType.PROMPT);
                userTweetRepo.saveAndFlush(ut);

                //保存@ message
                Message message = new Message();
                message.setUid(uid);
                message.setUid2(Long.valueOf(id));
                message.setTid(tweet.getId());
                message.setMessageType(Message.MessageType.PROMPT);
                message.setTitle(user.getName());
                message.setIcon(user.getAvatar());
                message.setTime(Utils.getTime());
                messageRepo.saveAndFlush(message);
                Utils.message(message);
            }
        }

        //查找转发的tweet
        if (originTid > 0) {
            Tweet originTweet = tweetRepo.findOne(originTid);
            tweet.setOriginTweet(originTweet);
            TweetTweet tweetTweet = new TweetTweet();
            tweetTweet.setTid(tweet.getId());
            tweetTweet.setTid2(originTid);
            tweetTweet.setTweetTweetType(TweetTweet.TweetTweetType.REPEAT);
            tweetTweetRepo.saveAndFlush(tweetTweet);

            originTweet.setRepeatCount(originTweet.getRepeatCount() + 1);
            tweetRepo.saveAndFlush(originTweet);
        }

        return tweet;
    }


    @RequestMapping(value = "/tweet/photo", method = RequestMethod.POST)
    public Tweet photo(@RequestParam("tid") long tid,
                       @RequestParam("image") MultipartFile[] images) {
        Tweet tweet = tweetRepo.findOne(tid);
        List<String> imageUrls = new ArrayList<>();
        StringBuffer imageContent = new StringBuffer();
        for (MultipartFile image : images) {
            String imageUrl = Utils.image(image);
            imageUrls.add(imageUrl);
            imageContent.append(String.format(Constants.TWEET_ADD_IMAGE_HTML, imageUrl));
        }
        String content = tweet.getSummary();
        tweet.setContent(Utils.content(String.format(Constants.TWEET_ADD_HTML, content, imageContent)));
        tweet.setImages(String.join(";", imageUrls));
        tweet = tweetRepo.saveAndFlush(tweet);
        return tweet;
    }


    @RequestMapping(value = "/tweet", method = RequestMethod.GET)
    public Tweet get(@RequestParam("uid") long uid, @RequestParam("tid") long tid) {
        Tweet tweet = tweetRepo.findOne(uid);
        tweet.setStar(userTweetRepo.findOneByUidAndTidAndUserTweetType(uid, tweet.getId(), UserTweet.UserTweetType.STAR) != null);
        Long tid2 = tweetTweetRepo.findTid2ByTidAndTweetTweetType(tweet.getId(), TweetTweet.TweetTweetType.REPEAT);
        if (tid2 != null) {
            tweet.setOriginTweet(tweetRepo.findOne(tid2));
        }
        return tweet;
    }

    public enum GetsType {

        HOME(0),
        TEAM(1),
        NEWS(2),
        USER(3);
        private int index;

        GetsType(int index) {
            this.index = index;
        }

        public static int size() {
            return GetsType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }


    @RequestMapping(value = "/tweet/list", method = RequestMethod.GET)
    public List<Tweet> gets(@RequestParam("uid") long uid,
                            @RequestParam("getsType") GetsType getsType,
                            @RequestParam("pageIndex") int pageIndex,
                            @RequestParam("pageSize") int pageSize) {
        List<Long> uids = new ArrayList<>();
        if (uid > 0 && (getsType == GetsType.HOME || getsType == GetsType.USER)) {
            uids.add(uid);
        }
        if (getsType != GetsType.USER) {
            List<User> users = new ArrayList<>();
            if (uid > 0) {
                users.addAll(userRepo.findAll(userUserRepo.findUid2sByUidAndUserUserType(uid, UserUser.UserUserType.FOCUS)));
            } else {
                users.add(userRepo.findByName(Constants.NEWS_YINGCHAO));
                users.add(userRepo.findByName(Constants.NEWS_XIJIA));
                users.add(userRepo.findByName(Constants.NEWS_DEJIA));
                users.add(userRepo.findByName(Constants.NEWS_YIJIA));
                users.add(userRepo.findByName(Constants.NEWS_FAJIA));
                users.add(userRepo.findByName(Constants.NEWS_ZHONGCHAO));
                users.add(userRepo.findByName(Constants.NEWS_HUABIAN));
            }
            for (User user : users) {
                if (getsType == GetsType.HOME && (user.getUserType() == User.UserType.NORMAL ||
                        user.getUserType() == User.UserType.PUBLIC)) {
                    uids.add(user.getId());
                } else if (getsType == GetsType.TEAM && user.getUserType() == User.UserType.TEAM) {
                    uids.add(user.getId());
                } else if (getsType == GetsType.NEWS && user.getUserType() == User.UserType.NEWS) {
                    uids.add(user.getId());
                }
            }

        }
        List<Long> tids = userTweetRepo.findTidsByUidsAndUserTweetType(uids, UserTweet.UserTweetType.ADD);
        List<Tweet> tweets = tweetRepo.findAllByOrderByDateAsc(tids);
        for (Tweet tweet : tweets) {
            tweet.setStar(userTweetRepo.findOneByUidAndTidAndUserTweetType(uid, tweet.getId(), UserTweet.UserTweetType.STAR) != null);
            Long tid2 = tweetTweetRepo.findTid2ByTidAndTweetTweetType(tweet.getId(), TweetTweet.TweetTweetType.REPEAT);
            if (tid2 != null) {
                tweet.setOriginTweet(tweetRepo.findOne(tid2));
            }
        }

        return tweets;

    }


    @RequestMapping(value = "/tweet/star", method = RequestMethod.POST)
    public void star(@RequestParam("uid") long uid,
                     @RequestParam("tid") long tid,
                     @RequestParam("star") boolean star) {
        userTweetRepo.deleteByUidAndTidAndUserTweetType(uid, tid, UserTweet.UserTweetType.STAR);
        Tweet tweet = tweetRepo.findOne(tid);
        tweet.setStarCount(star ? tweet.getStarCount() + 1 : tweet.getStarCount() - 1);
        tweetRepo.saveAndFlush(tweet);
        if (star) {
            UserTweet userTweet = new UserTweet();
            userTweet.setUid(uid);
            userTweet.setTid(tid);
            userTweet.setUserTweetType(UserTweet.UserTweetType.STAR);
            userTweetRepo.saveAndFlush(userTweet);
        }

    }

    @RequestMapping(value = "/tweet/comment", method = RequestMethod.POST)
    public void comment(@RequestBody Message message) {

        Tweet tweet = tweetRepo.findOne(message.getTid());
        tweet.setCommentCount(tweet.getCommentCount() + 1);
        tweetRepo.saveAndFlush(tweet);

        //保存评论
        UserTweet userTweet = new UserTweet();
        userTweet.setUid(message.getUid());
        userTweet.setTid(message.getTid());
        userTweet.setUserTweetType(UserTweet.UserTweetType.COMMENT);
        userTweetRepo.saveAndFlush(userTweet);

        message.setTime(Utils.getTime());
        Utils.message(messageRepo.saveAndFlush(message));
    }


}
