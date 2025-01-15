package com.example;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RockBlock extends SolidBlock {

    public RockBlock(int x, int y, double height, double width, Image image) {
        super(x, y, height, width, image);
        super.setGravity(45);
        super.setStabilityFactor(new Random().nextInt(51) + 25);

        super.setDensity(2.8);
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

