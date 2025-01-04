package com.example;

import java.util.Random;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class RockBlock extends Block {

    public RockBlock(int x, int y, double height, double width, Image image) {
        super(x, y, height, width, image);
        super.setStabilityFactor(new Random().nextInt(51) + 25);
    }
    @Override
    public double getX() {
        return super.getX();
    }
    @Override
    public double getY() {
        return super.getY();
    }
    @Override
    public ImageView getBlockInfo() {
        return super.getBlockInfo();
    }

    @Override
    public double getGravity() {
        return super.getGravity();
    }
}

