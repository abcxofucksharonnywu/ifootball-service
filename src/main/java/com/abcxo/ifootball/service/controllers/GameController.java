package com.abcxo.ifootball.service.controllers;

import com.abcxo.ifootball.service.models.Game;
import com.abcxo.ifootball.service.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        PageRequest pageRequest = new PageRequest(pageIndex, pageSize, Sort.Direction.ASC, "date");
        List<Game> games = gameRepo.findAll(pageRequest).getContent();
        for (Game game : games) {
            game.setSection("关注");
        }
        return games;
    }


}
