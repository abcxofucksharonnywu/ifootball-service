package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by shadow on 15/10/30.
 */
public interface TweetRepo extends JpaRepository<Tweet, Long> {

    Tweet findByTitle(String title);

    @Query("SELECT t FROM Tweet t WHERE id IN (:tids) ORDER BY date DESC ")
    List<Tweet> findAllByOrderByDateDesc(@Param("tids") List<Long> tids);

    Page<Tweet> findByIdIn(Collection<Long> tids, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByTweetType(Tweet.TweetType tweetType);
}
