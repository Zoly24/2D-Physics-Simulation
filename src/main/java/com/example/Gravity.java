package com.example;

import java.util.Random;

import javafx.scene.layout.Pane;

public class Gravity{

    private static final int MOVE_DOWN = 0, MOVE_LEFT = 1, MOVE_RIGHT = 2;

    private static final int MIDDLE_ROW = 0, LOWER_ROW = 1;

    private static final int LEFT_COLUMN = 0, MIDDLE_COLUMN = 1, RIGHT_COLUMN = 2;

    public static void updateGravity(Pane pane, Block[][] grid, long deltaTime) {
        for (int row = 0; row < Physics2D.GRID_ROWS; row++) {
            for (int column = Physics2D.GRID_COLUMNS - 1; column >= 0; column--) {
                if(grid[row][column] == null){
                    continue;
                }
                Block currentBlock = grid[row][column];
                
                if (currentBlock.isAffectedByGravity() &&
                    row + 1 < Physics2D.GRID_ROWS) {

                    currentBlock.setElapsedTime(currentBlock.getElapsedTime() + deltaTime);

                    if (currentBlock.getElapsedTime() >= currentBlock.getGravity()) {
                        int left = column - 1;
                        int right = column + 1;

                        boolean validLeftBound = left >= 0;
                        boolean validRightBound = right < Physics2D.GRID_COLUMNS;

                        boolean[][] blockLocations = new boolean[][]{
                            {validLeftBound && grid[row][left] == null, grid[row][column] == null, validRightBound && grid[row][right] == null},
                            {validLeftBound && grid[row + 1][left] == null, grid[row + 1][column] == null, validRightBound && grid[row + 1][column + 1] == null}
                        };

                        boolean isSurrounded =   !blockLocations[MIDDLE_ROW][LEFT_COLUMN] &&
                                                 !blockLocations[MIDDLE_ROW][RIGHT_COLUMN] &&
                                                 !blockLocations[LOWER_ROW][MIDDLE_COLUMN];

                        if(!isSurrounded){
                            if (blockLocations[LOWER_ROW][MIDDLE_COLUMN]) {
                                applyGravity(pane, grid, row, column, currentBlock, MOVE_DOWN);
                            } else if (canMoveLeftOrRight(blockLocations, currentBlock)) {
                                int direction = new Random().nextInt(2) + 1;
                                if (direction == MOVE_RIGHT) {
                                    applyGravity(pane, grid, row, column, currentBlock, MOVE_RIGHT);
                                } else {
                                    applyGravity(pane, grid, row, column, currentBlock, MOVE_LEFT);
                                }
                            } else if (canMoveLeft(blockLocations, currentBlock)) {
                                applyGravity(pane, grid, row, column, currentBlock, MOVE_LEFT);
                            } else if (canMoveRight(blockLocations, currentBlock)) {
                                applyGravity(pane, grid, row, column, currentBlock, MOVE_RIGHT);
                            } else if(currentBlock instanceof WaterBlock) {
                                applyWaterFlow(grid, row, column,(WaterBlock) currentBlock, blockLocations, isSurrounded);
                            }
                        }
                        

                        currentBlock.setElapsedTime(0);
                        currentBlock.setGravity(currentBlock.getGravity() / 1);
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

    public static void applyWaterFlow(Block[][] grid, int row, int column,WaterBlock waterBlock, boolean[][] blockLocations, boolean isSurrounded) {
        
    }

    public static void applyGravity(Pane pane, Block[][] grid, int row, int column, Block block, int direction) {
        int newRow = row + 1;
        int newColumn = column;

        if (direction == MOVE_LEFT) {
            newColumn = column - 1;
        } else if (direction == MOVE_RIGHT) {
            newColumn  = column + 1;
        }

        if (newRow < Physics2D.GRID_ROWS && newColumn >= 0 && newColumn < Physics2D.GRID_COLUMNS) {
            if (grid[newRow][newColumn] == null) {

                grid[newRow][newColumn] = block;
                grid[row][column] = null;
            }
        }

        block.setX(newColumn * (Physics2D.SCREEN_WIDTH / Physics2D.GRID_COLUMNS));
        block.setY(newRow * (Physics2D.SCREEN_HEIGHT / Physics2D.GRID_ROWS));

        updateVisualsGravity(pane, grid);
    }

    public static void updateVisualsGravity(Pane pane, Block[][] grid) {
        for (int row = 0; row < Physics2D.GRID_ROWS; row++) {
            for (int column = 0; column < Physics2D.GRID_COLUMNS; column++) {
                Block currentBlock = grid[row][column];
                
                if (currentBlock != null) {
                    if (!pane.getChildren().contains(currentBlock.getBlockInfo())) {
                        pane.getChildren().add(currentBlock.getBlockInfo());
                    }
                }
            }
        }
    }
}

