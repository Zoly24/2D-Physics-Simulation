package com.example;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

public class MouseHandler {
    private static boolean isMousePressed = false;
    private static boolean isRightMousePressed = false;
    private static boolean isLeftMousePressed = false;

    public static void mousePressedEvents(Pane pane, Block[][] grid) {
        pane.setOnMousePressed(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                isMousePressed = true;
                isLeftMousePressed = true;
                Physics2D.mouseEvents(pane, grid, event.getX(), event.getY(), Physics2D.LMB);
            } else if(event.getButton() == MouseButton.SECONDARY) {
                isMousePressed = true;
                isRightMousePressed = true;
            }
        });

        pane.setOnMouseDragged(event -> {
            if(isMousePressed) {
                if(isRightMousePressed) {
                    Physics2D.mouseEvents(pane, grid, event.getX(), event.getY(), Physics2D.RMB);
                } else if(isLeftMousePressed) {
                    Physics2D.mouseEvents(pane, grid, event.getX(), event.getY(), Physics2D.LMB);
                }
            }
        });

        pane.setOnMouseReleased(event -> {
            if(isLeftMousePressed){
                isMousePressed = false;
                isLeftMousePressed = false;
            } else if(isRightMousePressed) {
                isMousePressed = false;
                isRightMousePressed = false;
            }
        });
    }
}

