package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.UserTweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by shadow on 15/10/30.
 */
public interface UserTweetRepo extends JpaRepository<UserTweet, Long> {
    @Query("SELECT tid FROM UserTweet WHERE uid IN (:uids) AND userTweetType = (:userTweetType)")
    List<Long> findTidsByUidsAndUserTweetType(@Param("uids") List<Long> uids, @Param("userTweetType") UserTweet.UserTweetType userTweetType);


    @Modifying
    @Transactional
    long deleteByUidAndTidAndUserTweetType(long uid, long tid, UserTweet.UserTweetType userTweetType);

    UserTweet findByUidAndTidAndUserTweetType(long uid, long tid, UserTweet.UserTweetType userTweetType);
}
