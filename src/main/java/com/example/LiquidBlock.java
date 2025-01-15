package com.example;

import java.util.Random;

import javafx.scene.image.Image;

public abstract class LiquidBlock extends Block {

    public LiquidBlock(int x, int y, double height, double width, Image image) {
        super(x, y, height, width, image);
        super.setStabilityFactor(new Random().nextInt(50) + 1);
    }
}