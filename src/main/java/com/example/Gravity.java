package com.example;

import java.util.Random;

import javafx.scene.layout.Pane;

public class Gravity {

    private static final int NO_MOVE = -1, MOVE_DOWN = 0, MOVE_LEFT = 1, MOVE_RIGHT = 2;

    private static final int MIDDLE_ROW = 0, LOWER_ROW = 1;

    private static final int LEFT_COLUMN = 0, MIDDLE_COLUMN = 1, RIGHT_COLUMN = 2;

    public static void updateGravity(Pane pane, Block[][] grid, long deltaTime) {
        for (int row = 0; row < Physics2D.GRID_ROWS; row++) {
            for (int column = Physics2D.GRID_COLUMNS - 1; column >= 0; column--) {
                if (grid[row][column] == null) {
                    continue;
                }
                Block currentBlock = grid[row][column];
<<<<<<< HEAD

=======
                
>>>>>>> parent of d2e4878 (Refactor Blocks to be more specific of type (Solid or Liquid))
                if (currentBlock.isAffectedByGravity() &&
                        row + 1 < Physics2D.GRID_ROWS) {

                    currentBlock.setElapsedTime(currentBlock.getElapsedTime() + deltaTime);

                    if (currentBlock.getElapsedTime() >= currentBlock.getGravity()) {
                        int left = column - 1;
                        int right = column + 1;

                        boolean validLeftBound = left >= 0;
                        boolean validRightBound = right < Physics2D.GRID_COLUMNS;

                        boolean[][] blockLocations = new boolean[][] {
                                { validLeftBound && grid[row][left] == null, grid[row][column] == null,
                                        validRightBound && grid[row][right] == null },
                                { validLeftBound && grid[row + 1][left] == null, grid[row + 1][column] == null,
                                        validRightBound && grid[row + 1][column + 1] == null }
                        };

                        boolean isSurrounded = !blockLocations[MIDDLE_ROW][LEFT_COLUMN] &&
                                !blockLocations[MIDDLE_ROW][RIGHT_COLUMN] &&
                                !blockLocations[LOWER_ROW][MIDDLE_COLUMN];

                        boolean isFalling = false;

                        if (!isSurrounded) {
                            if (blockLocations[LOWER_ROW][MIDDLE_COLUMN]) {
                                isFalling = true;
                                applyGravity(pane, grid, row, column, currentBlock, MOVE_DOWN, isFalling);
                            } else if (canMoveLeftOrRight(blockLocations, currentBlock)) {
                                isFalling = true;
                                int direction = new Random().nextInt(2) + 1;
                                if (direction == MOVE_RIGHT) {
                                    applyGravity(pane, grid, row, column, currentBlock, MOVE_RIGHT, isFalling);
                                } else {
                                    applyGravity(pane, grid, row, column, currentBlock, MOVE_LEFT, isFalling);
                                }
                            } else if (canMoveLeft(blockLocations, currentBlock)) {
                                isFalling = true;
                                applyGravity(pane, grid, row, column, currentBlock, MOVE_LEFT, isFalling);
                            } else if (canMoveRight(blockLocations, currentBlock)) {
                                isFalling = true;
                                applyGravity(pane, grid, row, column, currentBlock, MOVE_RIGHT, isFalling);
<<<<<<< HEAD
                            } else if (currentBlock instanceof WaterBlock) {
                                int direction = applyLiquidFlow(grid, row, column);
=======
                            } else if(currentBlock instanceof WaterBlock) {
                                int direction = applyWaterFlow(grid, row, column);
>>>>>>> parent of d2e4878 (Refactor Blocks to be more specific of type (Solid or Liquid))
                                applyGravity(pane, grid, row, column, currentBlock, direction, isFalling);
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
        return blockLocations[MIDDLE_ROW][LEFT_COLUMN] &&
                blockLocations[MIDDLE_ROW][RIGHT_COLUMN] &&
                blockLocations[LOWER_ROW][LEFT_COLUMN] &&
                blockLocations[LOWER_ROW][RIGHT_COLUMN] &&
                !currentBlock.isStable();
    }

    private static boolean canMoveLeft(boolean[][] blockLocations, Block currentBlock) {
        return blockLocations[LOWER_ROW][LEFT_COLUMN] &&
                blockLocations[MIDDLE_ROW][LEFT_COLUMN] &&
                !currentBlock.isStable();
    }

    private static boolean canMoveRight(boolean[][] blockLocations, Block currentBlock) {
        return blockLocations[LOWER_ROW][RIGHT_COLUMN] &&
                blockLocations[MIDDLE_ROW][RIGHT_COLUMN] &&
                !currentBlock.isStable();
    }

    public static int applyWaterFlow(Block[][] grid, int row, int column) {
        int aboveRow = row - 1;

        if (aboveRow >= 0) {
            boolean aboveRowNotFull = false;
            for (int currentColumn = 0; currentColumn < Physics2D.GRID_COLUMNS; currentColumn++) {
                if (grid[aboveRow][currentColumn] == null) {
                    aboveRowNotFull = true;
                    break;
                }
            }

            if (aboveRowNotFull && column + 1 < Physics2D.GRID_COLUMNS && column - 1 >= 0) {
                boolean foundWaterRight = false;
                boolean foundWaterLeft = false;
                boolean foundWaterAbove = grid[aboveRow][column] instanceof WaterBlock;

                for (int columnsRight = column; columnsRight < Physics2D.GRID_COLUMNS; columnsRight++) {
                    if (grid[aboveRow][columnsRight] instanceof WaterBlock) {
                        foundWaterRight = true;
                        break;
                    } else if (grid[aboveRow][columnsRight] != null) {
                        break;
                    }
                }

                for (int columnsLeft = column; columnsLeft >= 0; columnsLeft--) {
                    if (grid[aboveRow][columnsLeft] instanceof WaterBlock) {
                        foundWaterLeft = true;
                        break;
                    } else if (grid[aboveRow][columnsLeft] != null) {
                        break;
                    }
                }

                if (foundWaterRight) {
                    if (grid[row][column - 1] == null) {
                        return MOVE_LEFT;
                    } else {
                        return NO_MOVE;
                    }
                } else if (foundWaterLeft) {
                    if (grid[row][column + 1] == null) {
                        return MOVE_RIGHT;
                    } else {
                        return NO_MOVE;
                    }
                } else if (foundWaterAbove) {
                    if (grid[row][column - 1] == null) {
                        return MOVE_LEFT;
                    } else if (grid[row][column + 1] == null) {
                        return MOVE_RIGHT;
                    } else {
                        return NO_MOVE;
                    }
                }
            }
        }
        return NO_MOVE;
    }

    public static void applyGravity(Pane pane, Block[][] grid, int row, int column, Block block, int direction,
            boolean isFalling) {
        int newRow = row;
        int newColumn = column;
<<<<<<< HEAD

        if (isFalling) {
=======
        
        if(isFalling) {
>>>>>>> parent of d2e4878 (Refactor Blocks to be more specific of type (Solid or Liquid))
            newRow = row + 1;
        }

        if (direction == MOVE_LEFT) {
            newColumn = column - 1;
        } else if (direction == MOVE_RIGHT) {
            newColumn = column + 1;
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
