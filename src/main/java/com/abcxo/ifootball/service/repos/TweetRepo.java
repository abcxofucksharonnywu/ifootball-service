package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by shadow on 15/10/30.
 */
public interface TweetRepo extends JpaRepository<Tweet, Long> {

    List<Tweet> findByTitle(String title);

    Tweet findByDate(long date);

    Page<Tweet> findByTweetContentType(Tweet.TweetContentType tweetContentType, Pageable pageable);

    Page<Tweet> findByIdIn(Collection<Long> tids, Pageable pageable);

    Page<Tweet> findByNameLikeIgnoreCaseOrTitleLikeIgnoreCaseOrSourceLikeIgnoreCaseOrSummaryLikeIgnoreCase(String name, String title, String source, String summary, Pageable pageable);
}
