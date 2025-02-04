package com.example;

import java.util.Random;

import javafx.scene.image.Image;

public abstract class SolidBlock extends Block {

    public SolidBlock(int x, int y, double height, double width, Image image) {
        super(x, y, height, width, image);
        super.setStabilityFactor(new Random().nextInt(50) + 1);
    }

    @Override
    public void setDensity(double density) {
        super.setDensity(density);
    }
}
