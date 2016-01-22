package com.abcxo.ifootball.service.controllers;

/**
 * Created by shadow on 15/10/23.
 */

import com.abcxo.ifootball.service.models.Message;
import com.abcxo.ifootball.service.models.User;
import com.abcxo.ifootball.service.models.UserUser;
import com.abcxo.ifootball.service.repos.MessageRepo;
import com.abcxo.ifootball.service.repos.UserRepo;
import com.abcxo.ifootball.service.repos.UserUserRepo;
import com.abcxo.ifootball.service.utils.Constants;
import com.abcxo.ifootball.service.utils.Constants.UserAlreadyExistException;
import com.abcxo.ifootball.service.utils.Constants.UserNotFoundException;
import com.abcxo.ifootball.service.utils.Constants.UserValidateException;
import com.abcxo.ifootball.service.utils.Utils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserUserRepo userUserRepo;

    @Autowired
    private MessageRepo messageRepo;


    //注册
    @RequestMapping(value = "/user/password", method = RequestMethod.GET)
    public User password(@RequestParam(value = "email", defaultValue = "") String email) {

        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }
        String password = RandomStringUtils.randomAlphanumeric(8);
        user.setPassword(Utils.md52(password));
        user = userRepo.saveAndFlush(user);
        Utils.email(email, password);
        return user;
    }

    //注册
    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public User login(@RequestParam(value = "email", defaultValue = "") String email,
                      @RequestParam(value = "password", defaultValue = "") String password,
                      @RequestParam(value = "deviceToken", defaultValue = "") String deviceToken) {

        User user = userRepo.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new UserValidateException();
        }
        user.setDeviceToken(deviceToken);
        return user;
    }


    //注册
    @RequestMapping(value = "/user/loginsso", method = RequestMethod.POST)
    public User loginsso(@RequestParam(value = "email", defaultValue = "") String email,
                         @RequestParam(value = "password", defaultValue = "") String password,
                         @RequestParam(value = "name", defaultValue = "") String name,
                         @RequestParam(value = "avatar", defaultValue = "") String avatar,
                         @RequestParam(value = "gender") User.GenderType gender,
                         @RequestParam(value = "deviceToken", defaultValue = "") String deviceToken) {

        User user = userRepo.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setSign("足球狗新人一枚,多多指教");
            user.setEmail(email);
            user.setPassword(password);
            user.setAvatar(avatar);
            user.setGender(gender);
            user.setCover("http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg");
            user.setDeviceToken(deviceToken);
            user = userRepo.saveAndFlush(user);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_YINGCHAO).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_XIJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_DEJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_YIJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_FAJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_ZHONGCHAO).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_OUGUAN).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_HUABIAN).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_VIDEO).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.SPECIAL_BREAK).getId(), true);
        }
        return user;
    }


    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    public void logout(@RequestParam(value = "uid") long uid) {
        User user = userRepo.findOne(uid);
        user.setDeviceToken(null);
        userRepo.saveAndFlush(user);
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public User register(@RequestParam(value = "email", defaultValue = "") String email,
                         @RequestParam(value = "password", defaultValue = "") String password,
                         @RequestParam(value = "deviceToken", defaultValue = "") String deviceToken) {

        User user = userRepo.findByEmail(email);
        if (user != null) {
            throw new UserAlreadyExistException();
        } else {
            user = new User();
            user.setName(email.split("@")[0]);
            user.setSign("足球狗一只，多多指教");
            user.setEmail(email);
            user.setPassword(password);
            user.setAvatar("http://7xosf3.com1.z0.glb.clouddn.com/2A1A28C6-1777-4193-8968-A1AD0234DA41.png");
            user.setCover("http://img.izhuti.cn/public/picture/20140506012/1381373364545.jpg");
            user.setDeviceToken(deviceToken);
            user = userRepo.saveAndFlush(user);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_YINGCHAO).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_XIJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_DEJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_YIJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_FAJIA).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_ZHONGCHAO).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_OUGUAN).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_HUABIAN).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.NEWS_VIDEO).getId(), true);
            focus(user.getId(), userRepo.findByName(Constants.SPECIAL_BREAK).getId(), true);
            return user;
        }
    }


    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public User edit(@RequestBody User user) {
        User user1 = userRepo.findByName(user.getName());
        if (user1 != null && user1.getId() != user.getId()) {
            throw new Constants.UserNameAlreadyExistException();
        }
        if (userRepo.findOne(user.getId()) != null) {
            user.setLetter(Utils.getNameIndex(user.getName()));
            user = userRepo.saveAndFlush(user);
        }
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

    @RequestMapping(value = "/user/name", method = RequestMethod.GET)
    public User get(@RequestParam("uid") long uid, @RequestParam("name") String name) {
        User user = userRepo.findByName(name);
        user.setFocus(userUserRepo.findByUidAndUid2AndUserUserType(uid, user.getId(), UserUser.UserUserType.FOCUS) != null);
        return user;
    }


    public enum GetsType {
        NORMAL(0),
        FRIEND(1),
        FOCUS(2),
        FANS(3),
        DISCOVER(4),
        SEARCH(5);

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
    public List<User> gets(@RequestParam("getsType") GetsType getsType,
                           @RequestParam("uid") long uid,
                           @RequestParam("keyword") String keyword,
                           @RequestParam("pageIndex") int pageIndex,
                           @RequestParam("pageSize") int pageSize) {
        List<User> getsUsers = new ArrayList<>();

        if (uid > 0) {
            if (getsType == GetsType.FRIEND || getsType == GetsType.FOCUS || getsType == GetsType.FANS) {
                PageRequest pageRequest = new PageRequest(pageIndex, pageSize, Sort.Direction.ASC, "letter");
                if (getsType == GetsType.FRIEND) {
                    List<Long> uids = userUserRepo.findUid2sByUidAndUserUserType(uid, UserUser.UserUserType.FOCUS);
                    List<Long> uid2s = userUserRepo.findUidsByUidAndUserUserType(uid, UserUser.UserUserType.FOCUS);
                    List<Long> uidSames = uid2s.stream().filter(id -> uids.contains(id)).collect(Collectors.toList());
                    getsUsers.addAll(userRepo.findByIdInAndUserType(uidSames, User.UserType.NORMAL, pageRequest).getContent());
                } else if (getsType == GetsType.FOCUS) {
                    List<Long> uids = userUserRepo.findUid2sByUidAndUserUserType(uid, UserUser.UserUserType.FOCUS);
                    getsUsers.addAll(userRepo.findByIdInAndUserType(uids, User.UserType.NORMAL, pageRequest).getContent());
                } else if (getsType == GetsType.FANS) {
                    List<Long> uids = userUserRepo.findUidsByUidAndUserUserType(uid, UserUser.UserUserType.FOCUS);
                    getsUsers.addAll(userRepo.findByIdInAndUserType(uids, User.UserType.NORMAL, pageRequest).getContent());
                }
            } else if (getsType == GetsType.DISCOVER) {
                User user = userRepo.findOne(uid);
                List<User> users = userRepo.findByUserType(User.UserType.NORMAL);
                List<User> userList = new ArrayList<>();
                for (int i = 0; i < users.size(); i++) {
                    User user2 = users.get(i);
                    if (user.getLat() != 0 && user.getLon() != 0 && user2.getLat() != 0 && user2.getLon() != 0) {
                        user2.setDistanceLong(Utils.distance(user.getLon(), user.getLat(), user2.getLon(), user2.getLat()));
                        String str;
                        if (user2.getDistanceLong() < 1000) {
                            str = String.format("%dm", user2.getDistanceLong());
                        } else {
                            str = String.format("%.2fkm", user2.getDistanceLong() / 1000.0f);
                        }
                        user2.setDistance(str);
                    } else {
                        user2.setDistanceLong(Long.MAX_VALUE);
                        user2.setDistance("很远");
                    }
                    if (user.getId() != user2.getId()) {
                        userList.add(user2);
                    }
                }

                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return ((Long) o1.getDistanceLong()).compareTo(o2.getDistanceLong());
                    }
                });


                for (int i = 0; i < userList.size(); i++) {
                    if (i > (pageIndex - 1) * pageSize && i < pageIndex * pageSize) {
                        continue;
                    } else if (i < (pageIndex + 1) * pageSize && i >= pageIndex * pageSize) {
                        User user2 = userList.get(i);
                        getsUsers.add(user2);

                    } else {
                        break;
                    }
                }

            }

        }

        if (getsType == GetsType.SEARCH && !StringUtils.isEmpty(keyword)) {
            PageRequest pageRequest = new PageRequest(pageIndex, pageSize, Sort.Direction.ASC, "letter");
            keyword = "%" + keyword + "%";
            getsUsers.addAll(userRepo.findByNameLikeIgnoreCaseOrSignLikeIgnoreCase(keyword, keyword, pageRequest).getContent());
        }


        for (User user2 : getsUsers) {
            user2.setFocus(userUserRepo.findByUidAndUid2AndUserUserType(uid, user2.getId(), UserUser.UserUserType.FOCUS) != null);
        }

        return getsUsers;

    }


    @RequestMapping(value = "/user/focus", method = RequestMethod.POST)
    public void focus(@RequestParam("uid") long uid, @RequestParam("uid2") long uid2, boolean focus) {
        if (focus) {
            if (userUserRepo.findByUidAndUid2AndUserUserType(uid, uid2, UserUser.UserUserType.FOCUS) == null) {

                UserUser userUser = new UserUser();
                userUser.setUid(uid);
                userUser.setUid2(uid2);
                userUser.setUserUserType(UserUser.UserUserType.FOCUS);
                userUserRepo.saveAndFlush(userUser);

                User user = userRepo.findOne(uid);
                user.setFocusCount(user.getFocusCount() + 1);
                userRepo.saveAndFlush(user);


                User user2 = userRepo.findOne(uid2);
                user2.setFansCount(user2.getFansCount() + 1);
                userRepo.saveAndFlush(user2);

                Message message = new Message();
                message.setUid(uid);
                message.setUid2(uid2);
                message.setMessageType(Message.MessageType.FOCUS);
                message.setTitle(user.getName());
                message.setContent(user.getName());
                message.setIcon(user.getAvatar());
                message.setTime(Utils.getTime());
                message.setDate(System.currentTimeMillis());
                Utils.message(messageRepo.saveAndFlush(message));
            }

        } else {
            if (userUserRepo.findByUidAndUid2AndUserUserType(uid, uid2, UserUser.UserUserType.FOCUS) != null) {
                User user = userRepo.findOne(uid);
                user.setFocusCount(user.getFocusCount() - 1);
                userRepo.saveAndFlush(user);

                User user2 = userRepo.findOne(uid2);
                user2.setFansCount(user2.getFansCount() - 1);
                userRepo.saveAndFlush(user2);

                userUserRepo.deleteByUidAndUid2InAndUserUserType(uid, Arrays.asList(uid2), UserUser.UserUserType.FOCUS);
            }

        }

    }


    @RequestMapping(value = "/user/team/focus", method = RequestMethod.POST)
    public void focusTeams(@RequestParam("uid") long uid, @RequestParam("uid2s") String uid2s) {
        String[] uid2Array = uid2s.split(";");
        Long[] uid2List = new Long[uid2Array.length];
        for (int i = 0; i < uid2Array.length; i++) {
            uid2List[i] = Long.parseLong(uid2Array[i]);
        }

        userUserRepo.deleteByUidAndTeamAndUserUserType(uid, 1, UserUser.UserUserType.FOCUS);

        long teamUid = 0;
        for (long uid2 : uid2List) {
            if (teamUid == 0) {
                teamUid = uid2;
            }
            UserUser userUser = new UserUser();
            userUser.setUid(uid);
            userUser.setUid2(uid2);
            userUser.setTeam(1);
            userUser.setUserUserType(UserUser.UserUserType.FOCUS);
            userUserRepo.saveAndFlush(userUser);
        }
        if (teamUid > 0) {
            User user = userRepo.findOne(uid);
            User teamUser = userRepo.findOne(teamUid);
            user.setTeamIcon(teamUser.getAvatar());
            userRepo.saveAndFlush(user);
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

