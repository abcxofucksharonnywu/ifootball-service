package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.Message;
import com.abcxo.ifootball.service.repos.*;
import com.abcxo.ifootball.service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shadow on 15/11/15.
 */
@RestController
public class MessageController {

    private Message testMessage() {
        Message message = new Message();
        message.setUid(0);
        message.setUid2(0);
        message.setTid(1);
        message.setTime("3小时前");
        message.setTitle("山东C罗");
        message.setText("里皮时代，恒大队的外援威震中超，尤其是孔卡、穆里奇、埃尔克森的南美前场铁三角组合，在2013年横扫亚洲赛场。“恒大靠外援”的标签，在那一年被贴得格外严实，撕都撕不掉。三人的进球，在那一年占了恒大队全队进球的七成。");
        message.setIcon("http://tse1.mm.bing.net/th?&id=OIP.Me12f5a011ec53760dd2ab88e4d24e115o0&w=300&h=300&c=0&pid=1.9&rs=0&p=0");

        return message;
    }

    public List<Message> testMesages() {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            messages.add(testMessage());
        }
        return messages;
    }


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
    public List<Message> gets(@RequestParam("uid") long uid,
                              @RequestParam("uid2") long uid2,
                              @RequestParam("tid") long tid,
                              @RequestParam("getsType") GetsType getsType,
                              @RequestParam("pageIndex") int pageIndex,
                              @RequestParam("pageSize") int pageSize) {

        Message message = testMessage();
        messageRepo.save(message);
        List<Message> messages = messageRepo.findAll();
        for (Message m : messages) {
            if (getsType == GetsType.COMMENT_TWEET) {
                m.setMainType(Message.MessageMainType.COMMENT_TWEET);
                m.setDetailType(Message.MessageDetailType.COMMENT);
            } else if (getsType == GetsType.CHAT_USER) {
                m.setMainType(Message.MessageMainType.CHAT_USER);
                m.setDetailType(Message.MessageDetailType.NONE);
            } else {
                Message.MessageMainType mainType = Message.MessageMainType.valueOf(m.getMessageType().name());
                if (mainType == Message.MessageMainType.FOCUS ||
                        mainType == Message.MessageMainType.STAR) {
                    m.setDetailType(Message.MessageDetailType.USER);
                } else if (mainType == Message.MessageMainType.COMMENT ||
                        mainType == Message.MessageMainType.PROMPT) {
                    m.setDetailType(Message.MessageDetailType.TWEET);
                } else if (mainType == Message.MessageMainType.CHAT) {
                    m.setDetailType(Message.MessageDetailType.CHAT);
                } else {
                    m.setDetailType(Message.MessageDetailType.NORMAL);
                }
                m.setMainType(mainType);

            }
        }
        return messages;
    }

    @RequestMapping(value = "/message/chat", method = RequestMethod.POST)
    public void chat(@RequestBody Message message) {
        message.setTime(Utils.getTime());
        Utils.message(messageRepo.saveAndFlush(message));
    }


}
