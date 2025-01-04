package com.example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class LineBlock extends Block {

    public LineBlock(int x, int y, double height, double width) {
        super(x, y, height, width);
        super.setGravity(0);
        super.setStabilityFactor(100);
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
        getBlockInfo().setFill(Color.BLACK);
    }

    @Override
    public double getGravity() {
        return super.getGravity();
    }
}

