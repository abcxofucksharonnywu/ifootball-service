package com.abcxo.ifootball.service.models;

import com.abcxo.ifootball.service.utils.Constants;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by SHARON on 15/10/29.
 */
@Entity
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private long uid;
    private String icon;
    private String name;

    //操作
    private int commentCount;
    private int repeatCount;
    private int starCount;


    //推文内容
    private String title;
    private String source;
    private String summary;
    private String content;

    @Column(length = 3000)
    private String images;
    private String time;

    private double lon;
    private double lat;

    private TweetType tweetType = TweetType.NORMAL;

    @Transient
    private Tweet originTweet;
    @Transient
    private boolean star;

    private long date;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String text) {
        String summary = "";
        if (!StringUtils.isEmpty(text)) {
            summary = text.substring(0, text.length() > Constants.MAX_SUMMARY ? Constants.MAX_SUMMARY : text.length() - 1);
        }
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Tweet getOriginTweet() {
        return originTweet;
    }

    public void setOriginTweet(Tweet originTweet) {
        this.originTweet = originTweet;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public TweetType getTweetType() {
        return tweetType;
    }

    public void setTweetType(TweetType tweetType) {
        this.tweetType = tweetType;
    }

    public enum TweetType {

        NORMAL(0),
        TEAM(1),
        NEWS(2),
        PUBLIC(3),
        SPECIAL(4);
        private int index;

        TweetType(int index) {
            this.index = index;
        }

        public static int size() {
            return TweetType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }
}
