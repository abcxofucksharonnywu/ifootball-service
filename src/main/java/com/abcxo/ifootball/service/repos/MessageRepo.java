package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by shadow on 15/10/30.
 */
public interface MessageRepo extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (uid2 = (:uid2) AND messageType not in (:messageTypes)) OR (messageType = (:messageType) AND (uid in (:uids) OR uid2 in (:uids)))")
    Page<Message> findByAll(@Param("uid2") long uid2, @Param("messageTypes") List<Message.MessageType> messageTypes, @Param("messageType") Message.MessageType messageType, @Param("uids") List<Long> uids, Pageable pageable);

    Message findByUidInAndUid2In(List<Long> uids, List<Long> uid2s);

    Page<Message> findByUid2AndMessageType(long uid2, Message.MessageType messageType, Pageable pageable);

    Page<Message> findByTidAndMessageType(long tid, Message.MessageType messageType, Pageable pageable);


    @Query("SELECT m FROM Message m WHERE messageType = (:messageType) AND (uid in (:uids) OR uid2 in (:uids))")
    Page<Message> findByMessageTypeAndUidInOrUid2In(@Param("messageType") Message.MessageType messageType, @Param("uids") List<Long> uids, Pageable pageable);


    Page<Message> findByUidInAndUid2InAndMessageType(List<Long> uids, List<Long> uid2s, Message.MessageType messageType, Pageable pageable);


    @Modifying
    @Transactional
    void deleteByTid(long tid);


    @Modifying
    @Transactional
    void deleteByUidInAndUid2InAndMessageType(List<Long> uids, List<Long> uid2s, Message.MessageType messageType);
}
