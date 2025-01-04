package com.example;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WaterBlock extends Block {

    private final double flowRate = 1.0;

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

    public double getFlowRate() {
        return this.flowRate;
    }
}

