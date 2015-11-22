package com.abcxo.ifootball.service.models;

import javax.persistence.*;

/**
 * Created by SHARON on 15/10/29.
 */
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long uid;
    private long uid2;
    private long tid;

    private String icon;
    private String title;
    private String text;
    private String cover;
    private String url;
    private String lon;
    private String lat;
    private String images;
    private String time;

    private MessageType messageType = MessageType.NORMAL;

    @Transient
    private MessageMainType mainType = MessageMainType.NORMAL;

    @Transient
    private MessageDetailType detailType = MessageDetailType.NORMAL;


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

    public long getUid2() {
        return uid2;
    }

    public void setUid2(long uid2) {
        this.uid2 = uid2;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageMainType getMainType() {
        return mainType;
    }

    public void setMainType(MessageMainType mainType) {
        this.mainType = mainType;
    }

    public MessageDetailType getDetailType() {
        return detailType;
    }

    public void setDetailType(MessageDetailType detailType) {
        this.detailType = detailType;
    }

    public enum MessageType {

        NORMAL(0),
        FOCUS(1),
        COMMENT(2),
        PROMPT(3),
        STAR(4),
        CHAT(5),
        SPECIAL(6);

        private int index;

        MessageType(int index) {
            this.index = index;
        }

        public static int size() {
            return MessageType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }


    public enum MessageMainType {

        NORMAL(0),
        FOCUS(1),
        COMMENT(2),
        PROMPT(3),
        STAR(4),
        CHAT(5),
        SPECIAL(6),
        COMMENT_TWEET(7),
        CHAT_USER(8);


        private int index;

        MessageMainType(int index) {
            this.index = index;
        }

        public static int size() {
            return MessageType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }

    public enum MessageDetailType {

        NORMAL(0),
        USER(1),
        TWEET(2),
        COMMENT(3),
        CHAT(4),
        NONE(5);
        private int index;

        MessageDetailType(int index) {
            this.index = index;
        }

        public static int size() {
            return MessageType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }

}
