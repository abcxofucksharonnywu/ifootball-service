package com.abcxo.ifootball.service.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by shadow on 15/11/15.
 */
@Entity
public class UserUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long uid;
    private long uid2;
    private UserUserType userUserType;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUid2() {
        return uid2;
    }

    public void setUid2(long uid2) {
        this.uid2 = uid2;
    }

    public UserUserType getUserUserType() {
        return userUserType;
    }

    public void setUserUserType(UserUserType userUserType) {
        this.userUserType = userUserType;
    }

    public enum UserUserType {

        FOCUS(0),
        STAR(1),
        TEAM(2),
        SPECIAL(3);
        private int index;

        UserUserType(int index) {
            this.index = index;
        }

        public static int size() {
            return UserUserType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }
}
