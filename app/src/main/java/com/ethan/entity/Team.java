package com.ethan.entity;

import java.io.Serializable;

public class Team implements Serializable {
    private String name;
    private int person;
    private int score;
    private int pause;
    private int foul;
    private Player player[] = new Player[10];

    public Team() {
        for (int i = 0; i < 10; i++) {
            this.player[i] = new Player();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public int getScore() {
        score = 0;
        for (int i = 0; i < this.getPerson(); i++) {
            score += getPlayer(i).getScore();
        }
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPause() {
        return pause;
    }

    public void initPause(int pause) {
        this.pause = pause;
    }

    public void setPause(int pause) {
        this.pause -= pause;
    }

    public int getFoul() {
        foul = 0;
        for (int i = 0; i < person; i++) {
            foul += player[i].getFoul();
        }
        return foul;
    }

    public void setFoul(int foul) {
        this.foul = foul;
    }

    public Player getPlayer(int i) {
        return player[i];
    }

//    public void setPlayer(int person) {
//        for (int i=0;i<person;i++) {
//            this.player[i] = new Player();
//        }
//    }
}
