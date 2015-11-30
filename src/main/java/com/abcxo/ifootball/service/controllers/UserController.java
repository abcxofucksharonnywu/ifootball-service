package com.abcxo.ifootball.service.controllers;

/**
 * Created by shadow on 15/10/23.
 */

import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.models.UserUser;
import com.abcxo.ifootball.service.repos.UserRepo;
import com.abcxo.ifootball.service.repos.UserUserRepo;
import com.abcxo.ifootball.service.utils.Constants;
import com.abcxo.ifootball.service.utils.Constants.UserAlreadyExistException;
import com.abcxo.ifootball.service.utils.Constants.UserValidateException;
import com.abcxo.ifootball.service.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserUserRepo userUserRepo;

    //注册
    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public User login(@RequestParam(value = "email", defaultValue = "") String email,
                      @RequestParam(value = "password", defaultValue = "") String password) {

        User user = userRepo.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new UserValidateException();
        }
        return user;
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public User register(@RequestParam(value = "email", defaultValue = "") String email,
                         @RequestParam(value = "password", defaultValue = "") String password) {

        User user = userRepo.findByEmail(email);
        if (user != null) {
            throw new UserAlreadyExistException();
        } else {
            user = new User();
            user.setName(email.split("@")[0]);
            user.setSign("爱足球吧新人一枚,多多指教");
            user.setEmail(email);
            user.setPassword(password);
            user.setAvatar("http://tse1.mm.bing.net/th?&id=OIP.Me12f5a011ec53760dd2ab88e4d24e115o0&w=300&h=300&c=0&pid=1.9&rs=0&p=0");
            user.setCover("http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg");
            user = userRepo.saveAndFlush(user);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_YINGCHAO).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_XIJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_DEJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_YIJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_FAJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_ZHONGCHAO).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_HUABIAN).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.PUBLIC_IMPORTANT).getId(), true);
            return user;
        }
    }


    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public User edit(@RequestBody User user) {
        user.setIndex(Utils.getNameIndex(user.getName()));
        user = userRepo.saveAndFlush(user);
        return user;
    }


    @RequestMapping(value = "/user/avatar", method = RequestMethod.POST)
    public User avatar(@RequestParam(value = "uid") long uid,
                       @RequestParam("image") MultipartFile image
    ) {
        String imageUrl = Utils.image(image);
        User user = userRepo.findOne(uid);
        user.setAvatar(imageUrl);
        user = userRepo.saveAndFlush(user);
        return user;
    }

    @RequestMapping(value = "/user/cover", method = RequestMethod.POST)
    public User cover(@RequestParam(value = "uid") long uid,
                      @RequestParam("image") MultipartFile image
    ) {
        String imageUrl = Utils.image(image);
        User user = userRepo.findOne(uid);
        user.setCover(imageUrl);
        user = userRepo.saveAndFlush(user);
        return user;
    }


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User get(@RequestParam("uid") long uid, @RequestParam("uid2") long uid2) {
        User user = userRepo.findOne(uid2);
        user.setFocus(userUserRepo.findByUidAndUid2AndUserUserType(uid, uid2, UserUser.UserUserType.FOCUS) != null);
        return user;
    }


    public enum GetsType {
        NORMAL(0),
        FRIEND(1),
        FOCUS(2),
        FANS(3),
        DISCOVER(4);

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

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public List<User> gets(@RequestParam("uid") long uid,
                           @RequestParam("getsType") GetsType getsType,
                           @RequestParam("pageIndex") int pageIndex,
                           @RequestParam("pageSize") int pageSize) {
        if (getsType == GetsType.FRIEND) {
            List<Long> uids = userUserRepo.findUid2sByUidAndUserUserType(uid, UserUser.UserUserType.FOCUS);
            List<Long> uid2s = userUserRepo.findUidsByUidAndUserUserType(uid, UserUser.UserUserType.FOCUS);
            List<Long> uidSames = uid2s.stream().filter(id -> uids.contains(id)).collect(Collectors.toList());
            return userRepo.findAll(uidSames);
        } else if (getsType == GetsType.FOCUS) {
            List<Long> uids = userUserRepo.findUid2sByUidAndUserUserType(uid, UserUser.UserUserType.FOCUS);
            return userRepo.findAll(uids);
        } else if (getsType == GetsType.FANS) {
            List<Long> uids = userUserRepo.findUidsByUidAndUserUserType(uid, UserUser.UserUserType.FOCUS);
            return userRepo.findAll(uids);
        } else if (getsType == GetsType.DISCOVER) {
            return userRepo.findAll();
        }
        return null;
    }


    @RequestMapping(value = "/user/focus", method = RequestMethod.POST)
    public void focus(@RequestParam("uid") long uid, @RequestParam("uid2") long uid2, boolean focus) {
        if (focus) {
            UserUser userUser = new UserUser();
            userUser.setUid(uid);
            userUser.setUid2(uid2);
            userUser.setUserUserType(UserUser.UserUserType.FOCUS);
            userUserRepo.saveAndFlush(userUser);
        } else {
            userUserRepo.deleteByUid2s(uid, Arrays.asList(uid2), UserUser.UserUserType.FOCUS);
        }

    }


    @RequestMapping(value = "/user/team/focus", method = RequestMethod.POST)
    public void focusTeams(@RequestParam("uid") long uid, @RequestParam("uid2s") String uid2s) {
        String[] uid2Array = uid2s.split(";");
        Long[] uid2List = new Long[uid2Array.length];
        for (int i = 0; i < uid2Array.length; i++) {
            uid2List[i] = Long.parseLong(uid2Array[i]);
        }

        userUserRepo.deleteByUid2s(uid, Arrays.asList(uid2List), UserUser.UserUserType.FOCUS);
        for (long uid2 : uid2List) {
            UserUser userUser = new UserUser();
            userUser.setUid(uid);
            userUser.setUid2(uid2);
            userUser.setUserUserType(UserUser.UserUserType.FOCUS);
            userUserRepo.saveAndFlush(userUser);
        }
    }

    @RequestMapping(value = "/user/team/list", method = RequestMethod.GET)
    public List<User> getTeams(@RequestParam("uid") long uid, @RequestParam("groupName") String groupName) {
        List<User> users = userRepo.findByGroupNameAndUserType(groupName, User.UserType.TEAM);
        for (User user : users) {
            user.setFocus(userUserRepo.findByUidAndUid2AndUserUserType(uid, user.getId(), UserUser.UserUserType.FOCUS) != null);
        }
        return users;
    }


}

