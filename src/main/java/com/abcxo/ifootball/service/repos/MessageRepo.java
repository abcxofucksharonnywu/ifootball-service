package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.Message;
import com.abcxo.ifootball.service.models.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by shadow on 15/10/30.
 */
public interface MessageRepo extends JpaRepository<Message, Long> {


    List<Message> findByUid2(long uid2);

    List<Message> findByUid2AndMessageType(long uid2, Message.MessageType messageType);

    List<Message> findByTidAndMessageType(long tid, Message.MessageType messageType);

    @Query("SELECT m FROM Message m WHERE uid IN (:uids) AND uid2 IN (:uid2s)  AND messageType = (:messageType)")
    List<Message> findByUidsAndUid2sAndMessageType(@Param("uids") List<Long> uids,@Param("uid2s") List<Long> uid2s,@Param("messageType") Message.MessageType messageType);

    Page<Message> findByIdIn(Collection<Long> tids, Pageable pageable);
}
