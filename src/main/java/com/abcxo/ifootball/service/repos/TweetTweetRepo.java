package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.TweetTweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by shadow on 15/10/30.
 */
public interface TweetTweetRepo extends JpaRepository<TweetTweet, Long> {
    TweetTweet findByTidAndTweetTweetType(long tid, TweetTweet.TweetTweetType tweetTweetType);

    @Modifying
    @Transactional
    void deleteByTid(long tid);
}
