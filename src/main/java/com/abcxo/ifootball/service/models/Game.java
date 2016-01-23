package com.abcxo.ifootball.service.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shadow on 16/1/9.
 */
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String content;
    @Transient
    private String section;
    @Transient
    private boolean focus;


    private long uid;
    private String icon;
    private String name;
    private String score;
    private long uid2;
    private String icon2;
    private String name2;
    private String score2;
    private StateType stateType = StateType.PREPARE;
    @Column(length = 5000)
    private ArrayList<Live> lives=new ArrayList<>();
    private String time;
    private long date;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public long getUid2() {
        return uid2;
    }

    public void setUid2(long uid2) {
        this.uid2 = uid2;
    }

    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }


    public String getScore2() {
        return score2;
    }

    public StateType getStateType() {
        return stateType;
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    public ArrayList<Live> getLives() {
        return lives;
    }

    public void setLives(ArrayList<Live> lives) {
        this.lives = lives;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public enum StateType {
        PREPARE(0),
        ING(1),
        END(2);
        private int index;

        StateType(int index) {
            this.index = index;
        }

        public static int size() {
            return StateType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }


    public static class Live implements Serializable {
        private String title;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
