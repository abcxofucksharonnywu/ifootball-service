package com.abcxo.ifootball.service.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    private String content;
    private String time;
    private double date;
    @NotNull
    private MessageType messageType = MessageType.NORMAL;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public String getDescription() {
        switch (messageType) {
            case FOCUS:
                return getTitle() + "关注了你";
            case COMMENT:
                return getTitle() + "评论了你";
            case PROMPT:
                return getTitle() + "@了你";
            case STAR:
                return getTitle() + "赞了你";

        }
        return getContent();
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
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


}
