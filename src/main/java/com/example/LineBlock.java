package com.example;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LineBlock extends Block {

    public LineBlock(int x, int y, double height, double width, Image image) {
        super(x, y, height, width, image);
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
    public ImageView getBlockInfo() {
        return super.getBlockInfo();
    }

    @Override
    public double getGravity() {
        return super.getGravity();
    }
}
