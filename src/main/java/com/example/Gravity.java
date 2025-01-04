package com.example;

import java.util.Random;

import javafx.scene.layout.Pane;

public class Gravity{

    private static final int DOWN = 0, LEFT = 1, RIGHT = 2;

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

                        boolean canGoLeft = left >= 0 && grid[left][j + 1] == null;
                        boolean canGoRight = right < Physics2D.GRID_SIZEX && grid[right][j + 1] == null;
                        boolean canGoDown = grid[i][j + 1] == null;

                        boolean blockRight = right < Physics2D.GRID_SIZEX && grid[right][j] != null;
                        boolean blockLeft = left >= 0 && grid[left][j] != null;
                        boolean blockBelow = grid[i][j + 1] != null;

                        boolean isSurrounded = blockRight && blockLeft && blockBelow;
                        if(currentBlock instanceof WaterBlock){
                            applyWaterFlow(pane, grid, i, j, (WaterBlock) currentBlock);
                        } else if (canGoDown && !isSurrounded) {
                            applyGravity(pane, grid, i, j, currentBlock, DOWN);
                        } else if (canGoLeft && canGoRight && !canGoDown && !currentBlock.isStable() && !isSurrounded) {
                            int direction = new Random().nextInt(2) + 1;
                            if (direction == RIGHT) {
                                applyGravity(pane, grid, i, j, currentBlock, RIGHT);
                            } else {
                                applyGravity(pane, grid, i, j, currentBlock, LEFT);
                            }
                        } else if (canGoLeft && !canGoDown && !currentBlock.isStable()&& !blockLeft) {
                            applyGravity(pane, grid, i, j, currentBlock, LEFT);
                        } else if (canGoRight && !canGoDown && !currentBlock.isStable()&& !blockRight) {
                            applyGravity(pane, grid, i, j, currentBlock, RIGHT);
                        }

                        currentBlock.setElapsedTime(0);
                        currentBlock.setGravity(currentBlock.getGravity() / 1.01);
                    }
                }
            }
        }
    }

    public static void applyWaterFlow(Pane pane, Block[][] grid, int x, int y, WaterBlock waterBlock) {
        int newXRight = x + 1;
        int newXLeft = x - 1;
        int newY = y + 1;

        if(newY < Physics2D.GRID_SIZEY && grid[x][newY] == null) {
            grid[x][newY] = waterBlock;
            grid[x][y] = null;
            waterBlock.setY(newY * (Physics2D.SCREEN_HEIGHT / Physics2D.GRID_SIZEY));
        } else {
            boolean canFlowRight = newXRight < Physics2D.GRID_SIZEX && grid[newXRight][y] == null;
            boolean canFlowLeft = newXLeft >= 0 && grid[newXLeft][y] == null;

            if(canFlowLeft && canFlowRight) {
                int direction = new Random().nextInt(2) + 1;
                if (direction == RIGHT) {
                    grid[newXRight][y] = waterBlock;
                    grid[x][y] = null;
                    waterBlock.setX(newXRight * (Physics2D.SCREEN_HEIGHT / Physics2D.GRID_SIZEX));
                } else {
                    grid[newXLeft][y] = waterBlock;
                    grid[x][y] = null;
                    waterBlock.setX(newXLeft * (Physics2D.SCREEN_HEIGHT / Physics2D.GRID_SIZEX));
                }
            } else if (canFlowLeft) {
                grid[newXLeft][y] = waterBlock;
                grid[x][y] = null;
                waterBlock.setX(newXLeft * (Physics2D.SCREEN_HEIGHT / Physics2D.GRID_SIZEX));
            } else if (canFlowRight) {
                grid[newXRight][y] = waterBlock;
                grid[x][y] = null;
                waterBlock.setX(newXRight * (Physics2D.SCREEN_HEIGHT / Physics2D.GRID_SIZEX));
            }
        }

        updateVisualsGravity(pane, grid);
    }

    public static void applyGravity(Pane pane, Block[][] grid, int x, int y, Block block, int direction) {
        int newY = y + 1;
        int newX = x;

        if (direction == LEFT) {
            newX = x - 1;
        } else if (direction == RIGHT) {
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

