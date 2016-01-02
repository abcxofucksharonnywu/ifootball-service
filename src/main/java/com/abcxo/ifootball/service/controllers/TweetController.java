package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.*;
import com.abcxo.ifootball.service.repos.*;
import com.abcxo.ifootball.service.utils.Constants;
import com.abcxo.ifootball.service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        //保持userTweet
        long uid = tweet.getUid();
        User user = userRepo.findOne(uid);

        if (user.getUserType() == User.UserType.SPECIAL && originTid > 0) {//超级用户
            Tweet t = tweetRepo.findOne(originTid);
            tweet = new Tweet();
            tweet.setUid(uid);
            tweet.setName(user.getName());
            tweet.setIcon(user.getAvatar());
            tweet.setTweetType(Tweet.TweetType.SPECIAL);
            tweet.setTitle(t.getTitle());
            tweet.setSummary(t.getSummary());
            tweet.setImages(t.getImages());
            tweet.setContent(t.getContent());
            tweet.setLat(t.getLat());
            tweet.setLon(t.getLon());
            tweet.setLocation(t.location);
            tweet.setTime(Utils.getTime());
            tweet.setDate(System.currentTimeMillis());
            originTid = 0;
            prompt = null;

        } else {
            String content = tweet.getContent();
            tweet.setSummary(content);
            Pattern pattern = Pattern.compile("@[^\\s]*");
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                String p = content.substring(start, end);
                content = content.replace(p, Constants.TWEET_ADD_PROMPT_HTML.replace(Constants.TWEET_HTML_PROMPT_TAG, p));
            }

            tweet.setContent(Utils.content(Constants.TWEET_ADD_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, content).replace(Constants.TWEET_HTML_IMAGES_TAG, "")));
            tweet.setTime(Utils.getTime());
            tweet.setDate(System.currentTimeMillis());
        }

        tweet = tweetRepo.saveAndFlush(tweet);

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
                ut.setUid(Long.valueOf(id));
                ut.setTid(tweet.getId());
                ut.setUserTweetType(UserTweet.UserTweetType.PROMPT);
                userTweetRepo.saveAndFlush(ut);

                //保存@ message
                Message message = new Message();
                message.setUid(uid);
                message.setUid2(Long.valueOf(id));
                message.setTid(tweet.getId());
                message.setMessageType(Message.MessageType.PROMPT);
                message.setTitle(user.getName());
                message.setContent(user.getName());
                message.setIcon(user.getAvatar());
                message.setTime(Utils.getTime());
                message.setDate(System.currentTimeMillis());
                Utils.message(messageRepo.saveAndFlush(message));
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
            imageContent.append(Constants.TWEET_ADD_IMAGE_HTML.replace(Constants.TWEET_HTML_IMAGE_TAG, imageUrl));
        }
        String content = tweet.getSummary();
        tweet.setContent(Utils.content(Constants.TWEET_ADD_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, content).replace(Constants.TWEET_HTML_IMAGES_TAG, imageContent)));
        tweet.setImages(String.join(";", imageUrls));
        tweet = tweetRepo.saveAndFlush(tweet);
        return tweet;
    }


    @RequestMapping(value = "/tweet", method = RequestMethod.GET)
    public Tweet get(@RequestParam("uid") long uid, @RequestParam("tid") long tid) {
        Tweet tweet = tweetRepo.findOne(tid);
        tweet.setStar(userTweetRepo.findByUidAndTidAndUserTweetType(uid, tweet.getId(), UserTweet.UserTweetType.STAR) != null);
        TweetTweet tweetTweet = tweetTweetRepo.findByTidAndTweetTweetType(tweet.getId(), TweetTweet.TweetTweetType.REPEAT);
        if (tweetTweet != null) {
            Tweet originTweet = tweetRepo.findOne(tweetTweet.getTid2());
            originTweet.setStar(userTweetRepo.findByUidAndTidAndUserTweetType(uid, originTweet.getId(), UserTweet.UserTweetType.STAR) != null);
            tweet.setOriginTweet(originTweet);
        }
        return tweet;
    }

    public enum GetsType {

        HOME(0),
        TEAM(1),
        NEWS(2),
        USER(3),
        SEARCH(4);
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
    public List<Tweet> gets(@RequestParam("getsType") GetsType getsType,
                            @RequestParam("uid") long uid,
                            @RequestParam("keyword") String keyword,
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
                users.add(userRepo.findByName(Constants.SPECIAL_BREAK));
                users.add(userRepo.findByName(Constants.NEWS_YINGCHAO));
                users.add(userRepo.findByName(Constants.NEWS_XIJIA));
                users.add(userRepo.findByName(Constants.NEWS_DEJIA));
                users.add(userRepo.findByName(Constants.NEWS_YIJIA));
                users.add(userRepo.findByName(Constants.NEWS_FAJIA));
                users.add(userRepo.findByName(Constants.NEWS_ZHONGCHAO));
                users.add(userRepo.findByName(Constants.NEWS_OUGUAN));
                users.add(userRepo.findByName(Constants.NEWS_HUABIAN));

            }
            for (User user : users) {
                if (getsType == GetsType.HOME && (user.getUserType() == User.UserType.NORMAL ||
                        user.getUserType() == User.UserType.SPECIAL)) {
                    uids.add(user.getId());
                } else if (getsType == GetsType.TEAM && user.getUserType() == User.UserType.TEAM) {
                    uids.add(user.getId());
                } else if (getsType == GetsType.NEWS && user.getUserType() == User.UserType.NEWS) {
                    uids.add(user.getId());
                }
            }

        }

        PageRequest pageRequest = new PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "date");
        Page<Tweet> tweets = null;

        if (getsType != GetsType.SEARCH) {
            if (uids.size() > 0) {
                List<Long> tids = userTweetRepo.findTidsByUidsAndUserTweetType(uids, UserTweet.UserTweetType.ADD);
                tweets = tweetRepo.findByIdIn(tids, pageRequest);
            }
        } else if (!StringUtils.isEmpty(keyword)) {
            keyword = "%" + keyword + "%";
            tweets = tweetRepo.findByNameLikeIgnoreCaseOrTitleLikeIgnoreCaseOrSourceLikeIgnoreCaseOrSummaryLikeIgnoreCase(keyword, keyword, keyword, keyword, pageRequest);

        }

        if (tweets != null) {
            for (Tweet tweet : tweets) {
                tweet.setStar(userTweetRepo.findByUidAndTidAndUserTweetType(uid, tweet.getId(), UserTweet.UserTweetType.STAR) != null);
                TweetTweet tweetTweet = tweetTweetRepo.findByTidAndTweetTweetType(tweet.getId(), TweetTweet.TweetTweetType.REPEAT);
                if (tweetTweet != null) {
                    Tweet originTweet = tweetRepo.findOne(tweetTweet.getTid2());
                    originTweet.setStar(userTweetRepo.findByUidAndTidAndUserTweetType(uid, originTweet.getId(), UserTweet.UserTweetType.STAR) != null);
                    tweet.setOriginTweet(originTweet);
                }
            }
            return tweets.getContent();
        } else {

            return new ArrayList<>();
        }

    }


    @RequestMapping(value = "/tweet", method = RequestMethod.DELETE)
    public void delete(@RequestParam("tid") long tid) {
        messageRepo.deleteByTid(tid);
        tweetTweetRepo.deleteByTid(tid);
        userTweetRepo.deleteByTid(tid);
        tweetRepo.delete(tid);
    }

    @RequestMapping(value = "/tweet/star", method = RequestMethod.POST)
    public void star(@RequestParam("uid") long uid,
                     @RequestParam("tid") long tid,
                     @RequestParam("star") boolean star) {
        Tweet tweet = tweetRepo.findOne(tid);
        if (star) {
            if (userTweetRepo.findByUidAndTidAndUserTweetType(uid, tid, UserTweet.UserTweetType.STAR) == null) {
                UserTweet userTweet = new UserTweet();
                userTweet.setUid(uid);
                userTweet.setTid(tid);
                userTweet.setUserTweetType(UserTweet.UserTweetType.STAR);
                userTweetRepo.saveAndFlush(userTweet);

                User user = userRepo.findOne(uid);
                Message message = new Message();
                message.setUid(uid);
                message.setUid2(tweet.getUid());
                message.setTid(tid);
                message.setMessageType(Message.MessageType.STAR);
                message.setTitle(user.getName());
                message.setContent(user.getName());
                message.setIcon(user.getAvatar());
                message.setTime(Utils.getTime());
                message.setDate(System.currentTimeMillis());
                Utils.message(messageRepo.saveAndFlush(message));
            } else {
                return;
            }
        } else {
            userTweetRepo.deleteByUidAndTidAndUserTweetType(uid, tid, UserTweet.UserTweetType.STAR);
        }
        tweet.setStarCount(star ? tweet.getStarCount() + 1 : tweet.getStarCount() - 1);
        tweetRepo.saveAndFlush(tweet);

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


        message.setMessageType(Message.MessageType.COMMENT);
        message.setTime(Utils.getTime());
        message.setDate(System.currentTimeMillis());
        Utils.message(messageRepo.saveAndFlush(message));
    }


}
