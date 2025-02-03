package com.example;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class Keybinding {
    public static void keyEvents(Scene scene, Pane pane, Block[][] grid) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case S:
                    System.out.println("Switched to Sand");
                    Physics2D.currentMaterial = Physics2D.Material.SAND;
                    break;
                case R:
                    System.out.println("Switched to Rock");
                    Physics2D.currentMaterial = Physics2D.Material.ROCK;
                    break;
                case W:
                    System.out.println("Switched to Water");
                    Physics2D.currentMaterial = Physics2D.Material.WATER;
                    break;
                case F:
                    System.out.println("Switched to Feather");
                    Physics2D.currentMaterial = Physics2D.Material.FEATHER;
                    break;
                case L:
                    System.out.println("Switched to Line");
                    Physics2D.currentMaterial = Physics2D.Material.LINE;
                    break;
                default:
                    break;
            }

            switch (event.getCode()) {
                case UP:
                    Physics2D.cursorSize += 1;
                    break;
                case DOWN:
                    Physics2D.cursorSize -= 1;
                    break;
                default:
                    break;
            }

            if (Physics2D.cursorSize < 0) {
                Physics2D.cursorSize = 0;
            }

            if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }

            if (event.getCode() == KeyCode.DELETE) {
                for (int row = 0; row < Physics2D.GRID_ROWS; row++) {
                    for (int column = 0; column < Physics2D.GRID_COLUMNS; column++) {
                        if (grid[row][column] != null) {
                            pane.getChildren().remove(grid[row][column].getBlockInfo());
                            grid[row][column] = null;
                        }
                    }
                }
            }

        });
    }
}
