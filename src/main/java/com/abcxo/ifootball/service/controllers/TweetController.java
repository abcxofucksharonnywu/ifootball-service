package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.*;
import com.abcxo.ifootball.service.repos.*;
import com.abcxo.ifootball.service.utils.Constants;
import com.abcxo.ifootball.service.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.abcxo.ifootball.service.models.Message.MessageType.COMMENT;

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
                     @RequestParam("tweet") MultipartFile tweetFile,
                     @RequestParam("image") MultipartFile[] images) throws IOException {

        //保持userTweet
        String tweetJSON = new String(tweetFile.getBytes());
        Tweet tweet = new Gson().fromJson(URLDecoder.decode(tweetJSON, "UTF8"), Tweet.class);
        long uid = tweet.getUid();
        User user = userRepo.findOne(uid);
        user.setTweetCount(user.getTweetCount() + 1);
        userRepo.saveAndFlush(user);

        if (user.getUserType() == User.UserType.SPECIAL && originTid > 0) {//超级用户
            Tweet t = tweetRepo.findOne(originTid);
            tweet = new Tweet();
            tweet.setUid(uid);
            tweet.setName(user.getName());
            tweet.setIcon(user.getAvatar());
            tweet.setTweetType(Tweet.TweetType.SPECIAL);
            tweet.setTweetContentType(tweet.getTweetContentType());
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
            content = content.replace(" ", "&nbsp;").replace("\n", "<br>");
            Pattern pattern = Pattern.compile("@[^\\p{P}|\\p{S}|\\p{Z}|\\p{M}]*");
            String mContent = content;
            Matcher matcher = pattern.matcher(mContent);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                String p = mContent.substring(start, end);
                content = content.replace(p, Constants.TWEET_ADD_PROMPT_HTML.replace(Constants.TWEET_HTML_PROMPT_TAG, p));
            }

            List<String> imageUrls = new ArrayList<>();
            StringBuffer imageContent = new StringBuffer();
            if (images != null) {
                for (MultipartFile image : images) {
                    String imageUrl = Utils.image(image);
                    if (!StringUtils.isEmpty(imageUrl)) {
                        imageUrls.add(imageUrl);
                        imageContent.append(Constants.TWEET_ADD_IMAGE_HTML.replace(Constants.TWEET_HTML_IMAGE_TAG, imageUrl));
                    }
                }
                tweet.setImages(String.join(";", imageUrls));
            }
            tweet.setContent(Utils.content(Constants.TWEET_ADD_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, content).replace(Constants.TWEET_HTML_IMAGES_TAG, imageContent)));
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
        PRO(1),
        TEAM(2),
        VIDEO(3),
        NEWS(4),
        USER(5),
        SEARCH(6);
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
        PageRequest pageRequest = new PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "date");
        Page<Tweet> tweets = null;
        if (getsType != GetsType.VIDEO) {
            List<Long> uids = new ArrayList<>();

            if (uid > 0 && (getsType == GetsType.HOME || getsType == GetsType.PRO || getsType == GetsType.USER)) {
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
                    users.add(userRepo.findByName(Constants.NEWS_VIDEO));

                }
                for (User user : users) {
                    if ((getsType == GetsType.HOME || getsType == GetsType.PRO) && (user.getUserType() == User.UserType.NORMAL ||
                            user.getUserType() == User.UserType.SPECIAL)) {
                        uids.add(user.getId());
                    } else if (getsType == GetsType.TEAM && user.getUserType() == User.UserType.TEAM) {
                        uids.add(user.getId());
                    } else if (getsType == GetsType.NEWS && user.getUserType() == User.UserType.NEWS) {
                        uids.add(user.getId());
                    }
                }

            }


            if (getsType != GetsType.SEARCH) {
                if (uids.size() > 0) {
                    List<Long> tids = userTweetRepo.findTidsByUidsAndUserTweetType(uids, UserTweet.UserTweetType.ADD);//获取推文
                    tweets = getsType == GetsType.USER ? tweetRepo.findByIdIn(tids, pageRequest) : tweetRepo.findByTweetTypeAndIdIn(getsType == GetsType.PRO ? Tweet.TweetType.PRO : Tweet.TweetType.NORMAL, tids, pageRequest);
                }
            } else if (!StringUtils.isEmpty(keyword)) {
                keyword = "%" + keyword + "%";
                tweets = tweetRepo.findByNameLikeIgnoreCaseOrTitleLikeIgnoreCaseOrSourceLikeIgnoreCaseOrSummaryLikeIgnoreCase(keyword, keyword, keyword, keyword, pageRequest);

            }
        } else {
            tweets = tweetRepo.findByTweetContentType(Tweet.TweetContentType.VIDEO, pageRequest);
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
                //获取评论
                PageRequest messageReq = new PageRequest(0, 3, Sort.Direction.DESC, "date");
                Page<Message> messages = messageRepo.findByTidAndMessageType(tweet.getId(), COMMENT, messageReq);
                tweet.setMessages(messages.getContent());
            }
            return tweets.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public class ListResponse {
        private Long total;
        private List<Tweet> content;

        public ListResponse(Long total, List<Tweet> content) {
            this.total = total;
            this.content = content;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public List<Tweet> getContent() {
            return content;
        }

        public void setContent(List<Tweet> content) {
            this.content = content;
        }
    }

    @RequestMapping(value = "/tweet/list2", method = RequestMethod.GET)
    public ListResponse gets2(@RequestParam("tweetType") Tweet.TweetType tweetType,
                              @RequestParam("uid") long uid,
                              @RequestParam("keyword") String keyword,
                              @RequestParam("pageIndex") int pageIndex,
                              @RequestParam("pageSize") int pageSize) {
        PageRequest pageRequest = new PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "date");
        Page<Tweet> tweets = null;
        if (uid > 0) {
            List<Long> uids = new ArrayList<>();
            uids.add(uid);
            List<Long> tids = userTweetRepo.findTidsByUidsAndUserTweetType(uids, UserTweet.UserTweetType.ADD);//获取推文
            if (!StringUtils.isEmpty(keyword)) {
                keyword = "%" + keyword + "%";
                tweets = tweetRepo.findByTweetTypeAndIdInAndTitleLikeIgnoreCase(tweetType, tids, keyword, pageRequest);
            } else {
                tweets = tweetRepo.findByTweetTypeAndIdIn(tweetType, tids, pageRequest);
            }
        }
        return new ListResponse(tweets != null ? tweets.getTotalElements() : 0, tweets != null ? tweets.getContent() : new ArrayList<>());
    }


    //新建Tweet
    @RequestMapping(value = "/tweet2", method = RequestMethod.POST)
    public Tweet add2(@RequestParam("tweet") String tweetStr, @RequestParam(value = "update", defaultValue = "false") boolean update) {
        Tweet tw = new Gson().fromJson(tweetStr, Tweet.class);
        Tweet tweet = update ? tweetRepo.findOne(tw.getId()) : tw;

        tweet.setTitle(tw.getTitle());
        tweet.setSummary(tw.getSummary());
        tweet.setImages(tw.getImages());
        tweet.setContent(Utils.content(Constants.TWEET_ADD_HTML.replace(Constants.TWEET_HTML_CONTENT_TAG, tw.getContent()).replace(Constants.TWEET_HTML_IMAGES_TAG, "")));
        tweet.setTime(Utils.getTime());
        if (!update) {
            tweet.setDate(System.currentTimeMillis());
        }
        tweet = tweetRepo.saveAndFlush(tweet);

        if (!update) {
            long uid = tweet.getUid();
            User user = userRepo.findOne(uid);
            user.setTweetCount(user.getTweetCount() + 1);
            userRepo.saveAndFlush(user);

            UserTweet userTweet = new UserTweet();
            userTweet.setUid(uid);
            userTweet.setTid(tweet.getId());
            userTweet.setUserTweetType(UserTweet.UserTweetType.ADD);
            userTweetRepo.saveAndFlush(userTweet);
        }


        return tweet;
    }

    //新建Tweet
    @RequestMapping(value = "/tweet/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("image") MultipartFile image) {
        return Utils.image(image);
    }


    @RequestMapping(value = "/tweet", method = RequestMethod.DELETE)
    public void delete(@RequestParam("tid") long tid) {
        Tweet tweet = tweetRepo.findOne(tid);
        long uid = tweet.getUid();
        User user = userRepo.findOne(uid);
        user.setTweetCount(user.getTweetCount() - 1);
        userRepo.saveAndFlush(user);
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


        message.setMessageType(COMMENT);
        message.setTime(Utils.getTime());
        message.setDate(System.currentTimeMillis());
        Utils.message(messageRepo.saveAndFlush(message));
    }

    @RequestMapping(value = "/tweet/grep", method = RequestMethod.POST)
    public String grep(@RequestParam("grepString") String grepString) {
        boolean isSuc = Utils.saveGrepString(grepString);
        if (!isSuc) {
            throw new Constants.SaveException();
        }
        return grepString;
    }

    @RequestMapping(value = "/tweet/grep", method = RequestMethod.GET)
    public String getGrep() {
        return Utils.getGrepString();
    }


}
