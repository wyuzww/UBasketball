package com.ethan.entity;

import java.io.Serializable;

public class Player implements Serializable {
    private String number;
    private String name;
    private int score;
    private int three_point;
    private int two_point;
    private int one_point;
    private int assist;
    private int backboard;
    private int block;
    private int steal;
    private int foul;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = "#" + number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return this.getThree_point() * 3 + this.getTwo_point() * 2 + this.getOne_point() * 1;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getThree_point() {
        return three_point;
    }

    public void setThree_point(int three_point) {
        this.three_point += three_point;
    }

    public int getTwo_point() {
        return two_point;
    }

    public void setTwo_point(int two_point) {
        this.two_point += two_point;
    }

    public int getOne_point() {
        return one_point;
    }

    public void setOne_point(int one_point) {
        this.one_point += one_point;
    }

    public int getAssist() {
        return assist;
    }

    public void setAssist(int assist) {
        this.assist += assist;
    }

    public int getBackboard() {
        return backboard;
    }

    public void setBackboard(int backboard) {
        this.backboard += backboard;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block += block;
    }

    public int getSteal() {
        return steal;
    }

    public void setSteal(int steal) {
        this.steal += steal;
    }

    public int getFoul() {
        return foul;
    }

    public void setFoul(int foul) {
        this.foul += foul;
    }
}
