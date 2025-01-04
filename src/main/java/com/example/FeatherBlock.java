package com.example;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class FeatherBlock extends Block {

    public FeatherBlock(int x, int y, double height, double width) {
        super(x, y, height, width);
        super.setGravity(500);
        super.setStabilityFactor(new Random().nextInt(51) + 40);
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
    public Rectangle getBlockInfo() {
        return super.getBlockInfo();
    }

    @Override
    public void blockColor() {
        getBlockInfo().setFill(Color.WHITE);
    }
    @Override
    public double getGravity() {
        return super.getGravity();
    }
}
