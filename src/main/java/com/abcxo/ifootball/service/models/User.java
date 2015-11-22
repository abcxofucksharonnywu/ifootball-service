package com.abcxo.ifootball.service.models;

import com.abcxo.ifootball.service.utils.Utils;

import javax.persistence.*;

/**
 * Created by SHARON on 15/10/29.
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Transient
    private String index;
    private String username;
    private String email;
    private String name;
    private String sign;
    private String password;
    private String avatar;
    private String cover;
    @Transient
    private String distance;
    private String time;
    private String lon;
    private String lat;
    private int focusCount;
    private int fansCount;
    private GenderType gender = GenderType.MALE;
    private UserType userType = UserType.NORMAL;

    @Transient
    private UserMainType mainType = UserMainType.NORMAL;

    @Transient
    private boolean focus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setIndex(Utils.getNameIndex(name));

    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public int getFocusCount() {
        return focusCount;
    }

    public void setFocusCount(int focusCount) {
        this.focusCount = focusCount;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserMainType getMainType() {
        return mainType;
    }

    public void setMainType(UserMainType mainType) {
        this.mainType = mainType;
    }

    public enum UserMainType {

        NORMAL(0),
        CONTACT(1),
        DISCOVER(2),
        SPECIAL(3);
        private int index;

        UserMainType(int index) {
            this.index = index;
        }

        public static int size() {
            return UserMainType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }

    public enum GenderType {

        MALE(0),
        FEMALE(1);
        private int index;

        GenderType(int index) {
            this.index = index;
        }

        public static int size() {
            return GenderType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }

    public enum UserType {

        NORMAL(0),
        TEAM(1),
        VIP(2),
        SUPER(3);
        private int index;

        UserType(int index) {
            this.index = index;
        }

        public static int size() {
            return UserType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }
}

