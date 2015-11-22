package com.abcxo.ifootball.service.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by shadow on 15/11/15.
 */
@Entity
public class UserTweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long uid;
    private long tid;
    private UserTweetType userTweetType;

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

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public UserTweetType getUserTweetType() {
        return userTweetType;
    }

    public void setUserTweetType(UserTweetType userTweetType) {
        this.userTweetType = userTweetType;
    }

    public enum UserTweetType {

        ADD(0),
        COMMENT(1),
        REPEAT(2),
        STAR(3),
        PROMPT(4);
        private int index;

        UserTweetType(int index) {
            this.index = index;
        }

        public static int size() {
            return UserTweetType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }
}
