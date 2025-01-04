package com.example;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class SandBlock extends Block {

    public SandBlock(int x, int y, double height, double width) {
        super(x, y, height, width);
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
    public Rectangle getBlockInfo() {
        return super.getBlockInfo();
    }

    @Override
    public void blockColor() {
        getBlockInfo().setFill(Color.YELLOW);
    }
    @Override
    public double getGravity() {
        return super.getGravity();
    }
}
