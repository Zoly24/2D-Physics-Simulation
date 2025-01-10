package com.example;

import java.util.Random;

import javafx.scene.layout.Pane;

public class Gravity{

    private static final int MOVE_DOWN = 0, MOVE_LEFT = 1, MOVE_RIGHT = 2;

    private static final int MIDDLE_ROW = 0, LOWER_ROW = 1;

    private static final int LEFT_COLUMN = 0, MIDDLE_COLUMN = 1, RIGHT_COLUMN = 2;

    public static void updateGravity(Pane pane, Block[][] grid, long deltaTime) {
        for (int i = 0; i < Physics2D.GRID_SIZEX; i++) {
            for (int j = Physics2D.GRID_SIZEY - 1; j >= 0; j--) {
                if(grid[i][j] == null){
                    continue;
                }
                Block currentBlock = grid[i][j];
                
                if (currentBlock.isAffectedByGravity() &&
                    j + 1 < Physics2D.GRID_SIZEY) {

                    currentBlock.setElapsedTime(currentBlock.getElapsedTime() + deltaTime);

                    if (currentBlock.getElapsedTime() >= currentBlock.getGravity()) {
                        int left = i - 1;
                        int right = i + 1;

                        boolean validRightBound = right < Physics2D.GRID_SIZEX;
                        boolean validLeftBound = left >= 0;

                        boolean[][] blockLocations = new boolean[][]{
                            {validLeftBound && grid[left][j] == null, grid[i][j] == null, validRightBound && grid[right][j] == null},
                            {validLeftBound && grid[left][j + 1] == null, grid[i][j + 1] == null, validRightBound && grid[right][j + 1] == null}
                        };

                        boolean isSurrounded =   !blockLocations[MIDDLE_ROW][LEFT_COLUMN] &&
                                                 !blockLocations[MIDDLE_ROW][RIGHT_COLUMN] &&
                                                 !blockLocations[LOWER_ROW][MIDDLE_COLUMN];

                        if(!isSurrounded){
                            if(currentBlock instanceof WaterBlock){
                                applyWaterFlow(pane, grid, i, j, (WaterBlock) currentBlock, blockLocations);
                            } else if (blockLocations[LOWER_ROW][MIDDLE_COLUMN]) {
                                applyGravity(pane, grid, i, j, currentBlock, MOVE_DOWN);
                            } else if (canMoveLeftOrRight(blockLocations, currentBlock)) {
                                int direction = new Random().nextInt(2) + 1;
                                if (direction == MOVE_RIGHT) {
                                    applyGravity(pane, grid, i, j, currentBlock, MOVE_RIGHT);
                                } else {
                                    applyGravity(pane, grid, i, j, currentBlock, MOVE_LEFT);
                                }
                            } else if (canMoveLeft(blockLocations, currentBlock)) {
                                applyGravity(pane, grid, i, j, currentBlock, MOVE_LEFT);
                            } else if (canMoveRight(blockLocations, currentBlock)) {
                                applyGravity(pane, grid, i, j, currentBlock, MOVE_RIGHT);
                            }
                        }
                        

                        currentBlock.setElapsedTime(0);
                        currentBlock.setGravity(currentBlock.getGravity() / 1.01);
                    }
                }
            }
        }
    }

    private static boolean canMoveLeftOrRight(boolean[][] blockLocations, Block currentBlock) {
        return  blockLocations[MIDDLE_ROW][LEFT_COLUMN] &&
                blockLocations[MIDDLE_ROW][RIGHT_COLUMN] &&
                blockLocations[LOWER_ROW][LEFT_COLUMN] &&
                blockLocations[LOWER_ROW][RIGHT_COLUMN] &&
                !currentBlock.isStable();
    }

    private static boolean canMoveLeft(boolean[][] blockLocations, Block currentBlock) {
        return  blockLocations[LOWER_ROW][LEFT_COLUMN] &&
                blockLocations[MIDDLE_ROW][LEFT_COLUMN] &&
                !currentBlock.isStable();
    }

    private static boolean canMoveRight(boolean[][] blockLocations, Block currentBlock) {
        return  blockLocations[LOWER_ROW][RIGHT_COLUMN] &&
                blockLocations[MIDDLE_ROW][RIGHT_COLUMN] &&
                !currentBlock.isStable();
    }

    public static void applyWaterFlow(Pane pane, Block[][] grid, int x, int y, WaterBlock waterBlock, boolean[][] blockLocations) {
        
    }

    public static void applyGravity(Pane pane, Block[][] grid, int x, int y, Block block, int direction) {
        int newY = y + 1;
        int newX = x;

        if (direction == MOVE_LEFT) {
            newX = x - 1;
        } else if (direction == MOVE_RIGHT) {
            newX = x + 1;
        }

        if (newY < Physics2D.GRID_SIZEY && newX >= 0 && newX < Physics2D.GRID_SIZEX) {
            if (grid[newX][newY] == null) {

                grid[newX][newY] = block;
                grid[x][y] = null;
            }
        }

        block.setX(newX * (Physics2D.SCREEN_WIDTH / Physics2D.GRID_SIZEX));
        block.setY(newY * (Physics2D.SCREEN_HEIGHT / Physics2D.GRID_SIZEY));

        updateVisualsGravity(pane, grid);
    }

    public static void updateVisualsGravity(Pane pane, Block[][] grid) {
        for (int i = 0; i < Physics2D.GRID_SIZEX; i++) {
            for (int j = 0; j < Physics2D.GRID_SIZEY; j++) {
                Block currentBlock = grid[i][j];
                if (currentBlock != null) {
                    if (!pane.getChildren().contains(currentBlock.getBlockInfo())) {
                        pane.getChildren().add(currentBlock.getBlockInfo());
                    }
                }
            }
        }
    }
}

