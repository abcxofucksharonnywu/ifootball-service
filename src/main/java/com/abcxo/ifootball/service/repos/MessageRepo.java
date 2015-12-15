package com.abcxo.ifootball.service.repos;

import com.abcxo.ifootball.service.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by shadow on 15/10/30.
 */
public interface MessageRepo extends JpaRepository<Message, Long> {


    Page<Message> findByUid2(long uid2, Pageable pageable);

    Message findByUidInAndUid2In(List<Long> uids, List<Long> uid2s);

    Page<Message> findByUid2AndMessageType(long uid2, Message.MessageType messageType, Pageable pageable);

    Page<Message> findByTidAndMessageType(long tid, Message.MessageType messageType, Pageable pageable);

    Page<Message> findByUidInAndUid2InAndMessageType(List<Long> uids, List<Long> uid2s, Message.MessageType messageType, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByTid(long tid);
}
