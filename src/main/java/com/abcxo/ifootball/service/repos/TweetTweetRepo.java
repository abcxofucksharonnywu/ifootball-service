package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.TweetTweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by shadow on 15/10/30.
 */
public interface TweetTweetRepo extends JpaRepository<TweetTweet, Long> {
    @Query("SELECT tid2 FROM TweetTweet WHERE tid = (:tid) AND tweetTweetType = (:tweetTweetType)")
    Long findTid2ByTidAndTweetTweetType(@Param("tid") long tid, @Param("tweetTweetType") TweetTweet.TweetTweetType tweetTweetType);
}
