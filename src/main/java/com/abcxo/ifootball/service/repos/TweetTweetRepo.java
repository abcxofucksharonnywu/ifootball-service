package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.TweetTweet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by shadow on 15/10/30.
 */
public interface TweetTweetRepo extends JpaRepository<TweetTweet, Long> {
    TweetTweet findByTidAndTweetTweetType(long tid, TweetTweet.TweetTweetType tweetTweetType);
}
