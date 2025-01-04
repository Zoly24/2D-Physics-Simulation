package com.example;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class RockBlock extends Block {

    public RockBlock(int x, int y, double height, double width) {
        super(x, y, height, width);
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
    public Rectangle getBlockInfo() {
        return super.getBlockInfo();
    }

    @Override
    public void blockColor() {
        getBlockInfo().setFill(Color.GRAY);
    }

    @Override
    public double getGravity() {
        return super.getGravity();
    }
}

