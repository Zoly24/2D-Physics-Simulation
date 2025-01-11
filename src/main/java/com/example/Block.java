package com.example;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Block {

    private double x;
    private double y;
    private final ImageView ImageView;
    private double gravity = 60;
    private double elapsedTime;
    private int stabilityFactor;

    public Block(int x, int y, double width, double height, Image image) {
        this.x = x;
        this.y = y;

        this.ImageView = new ImageView(image);
        this.ImageView.setX(x);
        this.ImageView.setY(y);
        this.ImageView.setFitWidth(width);
        this.ImageView.setFitHeight(height);

        this.elapsedTime = 0;
        this.stabilityFactor = new Random().nextInt(99) + 1;
    }


    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
        this.ImageView.setX(x);
    }

    public void setY(double y) {
        this.y = y;
        this.ImageView.setY(y);
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public double getGravity() {
        return this.gravity;
    }

    public boolean isAffectedByGravity() {
        return gravity != 0;
    }

    public int getStabilityFactor() {
        return this.stabilityFactor;
    }

    public void setStabilityFactor(int stabilityFactor) {
        this.stabilityFactor = stabilityFactor;
    }

    public boolean isStable() {
        return stabilityFactor > 50;
    }

    public double getElapsedTime() {
        return this.elapsedTime;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public ImageView getBlockInfo() {
        return this.ImageView;
    }

    public void placeBlock(int x, int y) {
        this.x = x;
        this.y = y;
        this.ImageView.setX(x);
        this.ImageView.setY(y);
    }
}

