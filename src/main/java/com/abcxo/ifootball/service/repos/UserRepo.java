package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by shadow on 15/10/30.
 */
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);

    User findByName(String name);

    List<User> findByGroupNameAndUserType(String groupName, User.UserType userType);

    Page<User> findByIdInAndUserType(Collection<Long> ids,User.UserType userType, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByUserType(User.UserType userType);

    List<User> findByUserType(User.UserType userType);
}
