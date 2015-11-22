package com.abcxo.ifootball.service.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by shadow on 15/11/15.
 */
@Entity
public class TweetTweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long tid;
    private long tid2;
    private TweetTweetType tweetTweetType;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public long getTid2() {
        return tid2;
    }

    public void setTid2(long tid2) {
        this.tid2 = tid2;
    }

    public TweetTweetType getTweetTweetType() {
        return tweetTweetType;
    }

    public void setTweetTweetType(TweetTweetType tweetTweetType) {
        this.tweetTweetType = tweetTweetType;
    }

    public enum TweetTweetType {

        REPEAT(0),
        QUOTE(1);
        private int index;

        TweetTweetType(int index) {
            this.index = index;
        }

        public static int size() {
            return TweetTweetType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }
}
