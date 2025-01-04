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
                isRightMousePressed = true;
                Physics2D.mouseEvents(pane, grid, event.getX(), event.getY(), Physics2D.LMB);
            } else if(event.getButton() == MouseButton.SECONDARY) {
                isMousePressed = true;
                isLeftMousePressed = true;
                Physics2D.mouseEvents(pane, grid, event.getX(), event.getY(), Physics2D.RMB);
            }
        });

        pane.setOnMouseDragged(event -> {
            if(isMousePressed && event.getButton() == MouseButton.PRIMARY && isRightMousePressed) {
                Physics2D.mouseEvents(pane, grid, event.getX(), event.getY(), Physics2D.LMB);
            } else if(isMousePressed && event.getButton() == MouseButton.SECONDARY && isLeftMousePressed) {
                Physics2D.mouseEvents(pane, grid, event.getX(), event.getY(), Physics2D.RMB);
            }
        });

        pane.setOnMouseReleased(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                isMousePressed = false;
                isRightMousePressed = false;
            } else if(event.getButton() == MouseButton.SECONDARY) {
                isMousePressed = false;
                isLeftMousePressed = false;
            }
        });
    }
}

