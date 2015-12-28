package com.abcxo.ifootball.service.models;

import com.abcxo.ifootball.service.utils.Utils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by SHARON on 15/10/29.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}),@UniqueConstraint(columnNames = {"name"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String letter;
    private String groupName;
    @NotNull
    private String email;
    @NotNull
    private String name;
    private String sign;
    private String password;
    private String avatar;
    private String cover;
    private String position;

    private double lon;
    private double lat;
    private int focusCount;
    private int fansCount;

    private GenderType gender = GenderType.MALE;
    private UserType userType = UserType.NORMAL;

    @Transient
    private boolean focus;
    @Transient
    private String distance;

    @Transient
    private long distanceLong;
    private String deviceToken;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
        setLetter(Utils.getNameIndex(name));
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public long getDistanceLong() {
        return distanceLong;
    }

    public void setDistanceLong(long distanceLong) {
        this.distanceLong = distanceLong;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
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
        NEWS(2),
        PUBLIC(3),
        SPECIAL(4);
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


}

