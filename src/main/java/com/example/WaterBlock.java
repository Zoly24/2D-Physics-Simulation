package com.example;

import java.util.Random;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class WaterBlock extends Block {

    public WaterBlock(int x, int y, double height, double width, Image image) {
        super(x, y, height, width, image);
        super.setGravity(40);
        super.setStabilityFactor(new Random().nextInt(50) + 1);
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

