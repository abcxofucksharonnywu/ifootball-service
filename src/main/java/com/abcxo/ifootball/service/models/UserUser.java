package com.abcxo.ifootball.service.models;

import javax.persistence.*;

/**
 * Created by shadow on 15/11/15.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"uid", "uid2", "userUserType"})})
public class UserUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long uid;
    private long uid2;
    @Column(nullable=false, columnDefinition="boolean default false")
    private int team;
    private UserUserType userUserType = UserUserType.FOCUS;


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


    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
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
        SPECIAL(2);
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
