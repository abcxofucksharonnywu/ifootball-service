package com.abcxo.ifootball.service.models;

import javax.persistence.*;

/**
 * Created by SHARON on 15/10/29.
 */
@Entity
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long uid;


    private int commentCount;
    private int repeatCount;
    private int starCount;
    @Transient
    private boolean star;



    private String icon;
    private String name;
    private String title;
    private String source;
    private String content;
    private String cover;
    private String url;
    private String lon;
    private String lat;
    private String images;
    private String time;

    private TweetMainType mainType = TweetMainType.NORMAL;
    private TweetDetailType detailType = TweetDetailType.TWEET;


    @Transient
    private Tweet originTweet;


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

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
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



    public String getContent() {
        return content;
    }

    public void setContent(String text) {
        this.content = text;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
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

    public TweetMainType getMainType() {
        return mainType;
    }

    public void setMainType(TweetMainType mainType) {
        this.mainType = mainType;
    }

    public TweetDetailType getDetailType() {
        return detailType;
    }

    public void setDetailType(TweetDetailType detailType) {
        this.detailType = detailType;
    }

    public Tweet getOriginTweet() {
        return originTweet;
    }

    public void setOriginTweet(Tweet originTweet) {
        this.originTweet = originTweet;
    }

    public enum TweetMainType {

        NORMAL(0),
        TEAM(1),
        NEWS(2),
        SPECIAL(3);
        private int index;

        TweetMainType(int index) {
            this.index = index;
        }

        public static int size() {
            return TweetMainType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }

    public enum TweetDetailType {

        TWEET(0),
        NEWS(1);
        private int index;

        TweetDetailType(int index) {
            this.index = index;
        }

        public static int size() {
            return TweetDetailType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }
}
