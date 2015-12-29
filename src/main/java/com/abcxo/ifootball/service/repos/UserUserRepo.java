package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.UserUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by shadow on 15/10/30.
 */
public interface UserUserRepo extends JpaRepository<UserUser, Long> {
    @Query("SELECT uid2 FROM UserUser WHERE uid =(:uid) and userUserType = (:userUserType)")
    List<Long> findUid2sByUidAndUserUserType(@Param("uid") long uid, @Param("userUserType") UserUser.UserUserType userUserType);

    UserUser findByUidAndUid2AndUserUserType(long uid, long uid2, UserUser.UserUserType userUserType);

    @Query("SELECT uid FROM UserUser WHERE uid2 =(:uid2) and userUserType = (:userUserType)")
    List<Long> findUidsByUidAndUserUserType(@Param("uid2") long uid2, @Param("userUserType") UserUser.UserUserType userUserType);

    @Modifying
    @Transactional
    void deleteByUidAndUid2InAndUserUserType(long uid, List<Long> uid2s, UserUser.UserUserType userUserType);

    @Modifying
    @Transactional
    void deleteByUidAndUid2InAndTeamAndUserUserType(long uid, List<Long> uid2s, boolean team, UserUser.UserUserType userUserType);
}
