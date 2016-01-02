package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.Message;
import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.repos.*;
import com.abcxo.ifootball.service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by shadow on 15/11/15.
 */
@RestController
public class MessageController {


    @Autowired
    private TweetRepo tweetRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TweetTweetRepo tweetTweetRepo;
    @Autowired
    private UserTweetRepo userTweetRepo;
    @Autowired
    private UserUserRepo userUserRepo;
    @Autowired
    private MessageRepo messageRepo;


    public enum GetsType {
        ALL(0),
        CHAT(1),
        CHAT_USER(2),
        COMMENT(3),
        COMMENT_TWEET(4),
        PROMPT(5),
        FOCUS(6),
        STAR(7),
        OTHER(8);

        private int index;

        GetsType(int index) {
            this.index = index;
        }

        public static int size() {
            return GetsType.values().length;
        }

        public int getIndex() {
            return index;
        }

    }

    @RequestMapping(value = "/message/list", method = RequestMethod.GET)
    public List<Message> gets(
            @RequestParam("getsType") GetsType getsType,
            @RequestParam("uid") long uid,
            @RequestParam("uid2") long uid2,
            @RequestParam("tid") long tid,
            @RequestParam("pageIndex") int pageIndex,
            @RequestParam("pageSize") int pageSize) {
        PageRequest pageRequest = new PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "date");
        if (getsType == GetsType.ALL) {
            return messageRepo.findByAll(uid2, Arrays.asList(Message.MessageType.CHAT, Message.MessageType.CHAT_GROUP), Message.MessageType.CHAT_GROUP, Arrays.asList(uid, uid2), pageRequest).getContent();
        } else if (getsType == GetsType.CHAT) {
            return messageRepo.findByMessageTypeAndUidInOrUid2In(Message.MessageType.CHAT_GROUP, Arrays.asList(uid, uid2), pageRequest).getContent();
        } else if (getsType == GetsType.CHAT_USER) {
            List<Message> pageMessages = messageRepo.findByUidInAndUid2InAndMessageType(Arrays.asList(uid, uid2), Arrays.asList(uid, uid2), Message.MessageType.CHAT, pageRequest).getContent();
            List<Message> messages = new ArrayList<Message>(pageMessages);
            Collections.reverse(messages);
            return messages;
        } else if (getsType == GetsType.COMMENT) {
            return messageRepo.findByUid2AndMessageType(uid2, Message.MessageType.COMMENT, pageRequest).getContent();
        } else if (getsType == GetsType.COMMENT_TWEET) {
            return messageRepo.findByTidAndMessageType(tid, Message.MessageType.COMMENT, pageRequest).getContent();
        } else if (getsType == GetsType.PROMPT) {
            return messageRepo.findByUid2AndMessageType(uid2, Message.MessageType.PROMPT, pageRequest).getContent();
        } else if (getsType == GetsType.FOCUS) {
            return messageRepo.findByUid2AndMessageType(uid2, Message.MessageType.FOCUS, pageRequest).getContent();
        } else if (getsType == GetsType.STAR) {
            return messageRepo.findByUid2AndMessageType(uid2, Message.MessageType.STAR, pageRequest).getContent();
        } else if (getsType == GetsType.OTHER) {
            return messageRepo.findByUid2AndMessageType(uid2, Message.MessageType.SPECIAL, pageRequest).getContent();
        }
        return new ArrayList<>();

    }

    @RequestMapping(value = "/message/chat", method = RequestMethod.POST)
    public void chat(@RequestBody Message message) {
        message.setTime(Utils.getTime());
        message.setDate(System.currentTimeMillis());
        Utils.message(messageRepo.saveAndFlush(message));

        List<Long> uids = Arrays.asList(message.getUid(), message.getUid2());
        PageRequest pageRequest = new PageRequest(0, 1, Sort.Direction.DESC, "date");
        List<Message> pageMessages = messageRepo.findByUidInAndUid2InAndMessageType(uids, uids, Message.MessageType.CHAT_GROUP, pageRequest).getContent();
        Message group = new Message();
        if (pageMessages != null && pageMessages.size() > 0) {
            group = pageMessages.get(0);
        }
        group.setUid(message.getUid());
        group.setUid2(message.getUid2());
        group.setTid(message.getTid());
        group.setIcon(message.getIcon());
        User user2 = userRepo.findOne(message.getUid2());
        group.setIcon2(user2.getAvatar());
        group.setContent(message.getContent());
        group.setDate(message.getDate());
        group.setTitle(message.getTitle());
        group.setMessageType(Message.MessageType.CHAT_GROUP);
        group.setTime(message.getTime());
        messageRepo.saveAndFlush(group);

    }


}
