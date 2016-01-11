package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.Game;
import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.models.UserUser;
import com.abcxo.ifootball.service.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shadow on 15/11/15.
 */
@RestController
public class GameController {
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

    @Autowired
    private GameRepo gameRepo;

    @RequestMapping(value = "/game/list", method = RequestMethod.GET)
    public List<Game> gets(@RequestParam("uid") long uid, @RequestParam("pageIndex") int pageIndex,
                           @RequestParam("pageSize") int pageSize) {
        PageRequest pageRequest = new PageRequest(pageIndex, 1000, Sort.Direction.ASC, "date");
        List<User> users = userRepo.findAll(userUserRepo.findUid2sByUidAndUserUserType(uid, UserUser.UserUserType.FOCUS));
        List<Long> uids = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType() == User.UserType.TEAM) {
                uids.add(user.getId());
            }
        }


        if (uids.size() > 0) {
            List<Game> games = new ArrayList<>();
            List<Game> focusGames = gameRepo.findByUidInOrUid2In(uids, uids, pageRequest).getContent();
            for (Game game : focusGames) {
                game.setSection("关注");
            }

            games.addAll(focusGames);

            List<Game> hotGames = gameRepo.findByUidNotInAndUid2NotIn(uids, uids, pageRequest).getContent();
            for (Game game : hotGames) {
                game.setSection("热门");
            }

            games.addAll(hotGames);

            return games;
        } else {
            List<Game> games = gameRepo.findAll(pageRequest).getContent();
            for (Game game : games) {
                game.setSection("热门");
            }
            return games;
        }

    }


}
