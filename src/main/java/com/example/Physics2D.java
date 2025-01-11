package com.example;

import java.util.EnumMap;
import java.util.Map;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/*
 * Next Update:
 * 
 * -Add Water Physics
 * -Add TNT
 * --Add TNT Explosion
 * 
 */
public class Physics2D extends Application {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 1200;

    public static final double GRID_THICKNESS = 1;

    public static final int GRID_ROWS = SCREEN_WIDTH / 20;
    public static final int GRID_COLUMNS = SCREEN_HEIGHT / 20;

    public static final int LMB = 0;
    public static final int RMB = 1;

    public static final Color BACKGROUND_COLOR = Color.rgb(61, 117, 122);

    public enum Material {
        SAND,
        ROCK,
        WATER,
        FEATHER,
        LINE
    }

    public static Material currentMaterial = Material.SAND;
    private static final Map<Material, Image> materialImages = new EnumMap<>(Material.class);
    public static int cursorSize = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        loadMaterialImage();

        Pane root = new Pane();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        Background background = new Background(new BackgroundFill(BACKGROUND_COLOR,
                CornerRadii.EMPTY,
                javafx.geometry.Insets.EMPTY));
        root.setBackground(background);

        Block[][] grid = new Block[GRID_ROWS][GRID_COLUMNS];

        createGrid(root);
        Keybinding.keyEvents(scene, root, grid);
        MouseHandler.mousePressedEvents(root, grid);
        Gravity.updateVisualsGravity(root, grid);
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                long deltaTime = now - lastTime;
                if(deltaTime > 4166667) {

                    Gravity.updateGravity(root, grid, deltaTime / 1000000);
                    lastTime = now;
                    if(deltaTime / 1000000 > 32) {
                        System.out.println("Update at: " + now + ", Delta: " + (deltaTime / 1000000) + " ms");
                    }
                    
                }
            }
        };
        timer.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Physics 2D");
        primaryStage.show();
    }

    

    public static void createGrid(Pane root) {
        double gridWidth = (double) SCREEN_WIDTH / GRID_COLUMNS;
        double gridHeight = (double) SCREEN_HEIGHT / GRID_ROWS;

        for (int rows = 0; rows <= GRID_ROWS; rows++) {
            Line line = new Line(rows * gridWidth, 0, rows * gridWidth, SCREEN_HEIGHT);

            line.setStrokeWidth(GRID_THICKNESS);
            line.setStroke(BACKGROUND_COLOR.darker());
            root.getChildren().add(line);
        }

        for (int columns = 0; columns <= GRID_COLUMNS; columns++) {
            Line line = new Line(0, columns * gridHeight, SCREEN_WIDTH, columns * gridHeight);

            line.setStrokeWidth(GRID_THICKNESS);
            line.setStroke(BACKGROUND_COLOR.darker());
            root.getChildren().add(line);
        }
    }

    public void loadMaterialImage() {
        materialImages.put(Material.SAND, new Image(getClass().getResourceAsStream("/images/sand_texture.jpg")));
        materialImages.put(Material.ROCK, new Image(getClass().getResourceAsStream("/images/rock_texture.jpg")));
        materialImages.put(Material.WATER, new Image(getClass().getResourceAsStream("/images/water_texture.png")));
        materialImages.put(Material.FEATHER, new Image(getClass().getResourceAsStream("/images/feather_texture.jpg")));
        materialImages.put(Material.LINE, new Image(getClass().getResourceAsStream("/images/line_texture.png")));
    }

    public static Image getMaterialImage(Material material) {
            return materialImages.get(material);
        }
    
        public static void mouseEvents(Pane pane, Block[][] grid, double mouseX, double mouseY, int mouseButton) {
            int gridLocationColumn = (int) (mouseX / (SCREEN_WIDTH / GRID_COLUMNS));
            int gridLocationRow = (int) (mouseY / (SCREEN_HEIGHT / GRID_ROWS));
            
            if (mouseButton == LMB) {
                addBlocks(pane, grid, gridLocationRow, gridLocationColumn, mouseX, mouseY);
                
            } else if (mouseButton == RMB) {
                deleteBlocks(pane, grid, gridLocationRow, gridLocationColumn);
                
            }
    
        }

        public static boolean gridWithinBounds(int gridLocationRow, int gridLocationColumn) {
            return gridLocationRow >= 0 && gridLocationColumn >= 0 && gridLocationColumn < GRID_COLUMNS && gridLocationRow < GRID_ROWS;
            
        }
    
        public static void addBlocks(Pane pane, Block[][] grid, int gridLocationRow, int gridLocationColumn, double mouseX, double mouseY) {
            if (gridWithinBounds(gridLocationRow, gridLocationColumn)) {
                    if(cursorSize > 0) {
                        for(int row = gridLocationRow + cursorSize; row >= gridLocationRow - cursorSize; row--) {
                            for(int column = gridLocationColumn + cursorSize; column >= gridLocationColumn - cursorSize; column--) {
                                if(row < GRID_ROWS && row >= 0 && column < GRID_COLUMNS && column >= 0) {
                                    if(grid[row][column] == null) {
                                        double placementX = column * ((double) SCREEN_WIDTH / GRID_COLUMNS);
                                        double placementY = row * ((double) SCREEN_HEIGHT / GRID_ROWS);
    
                                        Block newBlock = createNewObject(currentMaterial, placementX, placementY);
                                        pane.getChildren().add(newBlock.getBlockInfo());
                                        grid[row][column] = newBlock;
                                        
                                    }
                                }
                            }
                        }
                    } else if (grid[gridLocationRow][gridLocationColumn] == null) {
                        double placementX = findNewLocationSingleBlock(mouseX, HORIZONTAL);
                        double placementY = findNewLocationSingleBlock(mouseY, VERTICAL);
    
                        Block newBlock = createNewObject(currentMaterial, placementX, placementY);
    
                        grid[gridLocationRow][gridLocationColumn] = newBlock;
    
                        pane.getChildren().add(newBlock.getBlockInfo());
                    }
                }
        }
    
        public static void deleteBlocks(Pane pane, Block[][] grid, int gridLocationRow, int gridLocationColumn) {
            if (gridWithinBounds(gridLocationRow, gridLocationColumn)) {
                for(int row = gridLocationRow - cursorSize; row <= gridLocationRow + cursorSize; row++) {
                    for(int column = gridLocationColumn - cursorSize; column <= gridLocationColumn + cursorSize; column++) {
                        if(row < GRID_ROWS && row >= 0 && column < GRID_COLUMNS && column >= 0) {
                            if(grid[row][column] != null) {
                                pane.getChildren().remove(grid[row][column].getBlockInfo());
                                grid[row][column] = null;
                            
                            }
                        }
                    }
                }
            } else if(grid[gridLocationRow][gridLocationColumn] != null) {
                    pane.getChildren().remove(grid[gridLocationRow][gridLocationColumn].getBlockInfo());
                    grid[gridLocationRow][gridLocationColumn] = null;
            }
                    
        }
        
        public static Block createNewObject(Material material, double placementX, double placementY) {
            Block newBlock = null;
            Image image = getMaterialImage(material);

        switch (material) {
            case SAND:
                newBlock = new SandBlock((int) placementX,
                        (int) placementY,
                        (SCREEN_WIDTH / GRID_COLUMNS) - GRID_THICKNESS,
                        (SCREEN_HEIGHT / GRID_ROWS) - GRID_THICKNESS,
                        image);
                break;
            case ROCK:
                newBlock = new RockBlock((int) placementX,
                        (int) placementY,
                        (SCREEN_WIDTH / GRID_COLUMNS) - GRID_THICKNESS,
                        (SCREEN_HEIGHT / GRID_ROWS) - GRID_THICKNESS,
                        image);
                break;
            case WATER:
                newBlock = new WaterBlock((int) placementX,
                        (int) placementY,
                        (SCREEN_WIDTH / GRID_COLUMNS) - GRID_THICKNESS,
                        (SCREEN_HEIGHT / GRID_ROWS) - GRID_THICKNESS,
                        image);
                break;
            case FEATHER:
                newBlock = new FeatherBlock((int) placementX,
                        (int) placementY,
                        (SCREEN_WIDTH / GRID_COLUMNS) - GRID_THICKNESS,
                        (SCREEN_HEIGHT / GRID_ROWS) - GRID_THICKNESS,
                        image);
                break;
            case LINE:
                newBlock = new LineBlock((int) placementX,
                        (int) placementY,
                        (SCREEN_WIDTH / GRID_COLUMNS) - GRID_THICKNESS,
                        (SCREEN_HEIGHT / GRID_ROWS) - GRID_THICKNESS,
                        image);
                break;
            default:
                break;
        }

        return newBlock;
    }

    public static double findNewLocationSingleBlock(double currentPosition, int direction) {
        double BlockXSnap;

        switch (direction) {
            case VERTICAL:
                BlockXSnap = currentPosition % ((double) SCREEN_WIDTH / GRID_COLUMNS);
                break;
            case HORIZONTAL:
                BlockXSnap = currentPosition % ((double) SCREEN_HEIGHT / GRID_ROWS);
                break;
            default:
                return 0;
        }

        double newPlacement = currentPosition - BlockXSnap + ((double) GRID_THICKNESS / 2);

        return newPlacement;

    }
}
