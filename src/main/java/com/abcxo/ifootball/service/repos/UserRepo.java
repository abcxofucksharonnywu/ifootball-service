package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.models.UserTweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by shadow on 15/10/30.
 */
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);

    @Query("SELECT id FROM User WHERE userType = (:userType)")
    List<Long> findUidsByUserType(@Param("userType") User.UserType userType);
}
