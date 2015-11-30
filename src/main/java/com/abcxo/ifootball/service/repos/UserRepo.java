package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.models.UserTweet;
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
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);
    User findByName(String name);

    List<User> findByGroupNameAndUserType(String groupName, User.UserType userType);


    @Modifying
    @Transactional
    void deleteByUserType(User.UserType userType);
}
