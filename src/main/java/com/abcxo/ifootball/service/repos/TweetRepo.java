package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.Tweet;
import com.abcxo.ifootball.service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by shadow on 15/10/30.
 */
public interface TweetRepo extends JpaRepository<Tweet, Long> {

    Tweet findByTitle(String title);

    @Modifying
    @Transactional
    void deleteByTweetType(Tweet.TweetType tweetType);
}
