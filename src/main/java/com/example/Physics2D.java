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

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800 ;

    public static final double GRID_THICKNESS = 1;

    public static final int GRID_SIZEX = SCREEN_WIDTH / 20;
    public static final int GRID_SIZEY = SCREEN_HEIGHT / 20;

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

        Block[][] grid = new Block[GRID_SIZEX][GRID_SIZEY];

        createGrid(root);
        Keybinding.keyEvents(scene, root, grid);
        MouseHandler.mousePressedEvents(root, grid);
        Gravity.updateVisualsGravity(root, grid);
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if(now - lastTime > 16666666) {

                    Gravity.updateGravity(root, grid, now / 1000000 - lastTime / 1000000);
                    lastTime = now;
                }
            }
        };
        timer.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Physics 2D");
        primaryStage.show();
    }

    

    public static void createGrid(Pane root) {
        double gridWidth = (double) SCREEN_WIDTH / GRID_SIZEX;
        double gridHeight = (double) SCREEN_HEIGHT / GRID_SIZEY;

        for (int rows = 0; rows <= GRID_SIZEX; rows++) {
            Line line = new Line(rows * gridWidth, 0, rows * gridWidth, SCREEN_HEIGHT);

            line.setStrokeWidth(GRID_THICKNESS);
            line.setStroke(BACKGROUND_COLOR.darker());
            root.getChildren().add(line);
        }

        for (int columns = 0; columns <= GRID_SIZEY; columns++) {
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
            if (mouseButton == LMB) {
                int gridLocationX = (int) (mouseX / (SCREEN_WIDTH / GRID_SIZEX));
                int gridLocationY = (int) (mouseY / (SCREEN_HEIGHT / GRID_SIZEY));

                System.out.println("Adding Block at: " + gridLocationX + ", " + gridLocationY);
    
                addBlocks(pane, grid, gridLocationX, gridLocationY, mouseX, mouseY);
                
            } else if (mouseButton == RMB) {
                int gridLocationX = (int) (mouseX / (SCREEN_WIDTH / GRID_SIZEX));
                int gridLocationY = (int) (mouseY / (SCREEN_HEIGHT / GRID_SIZEY));
    
                System.out.println("Deleting Block at: " + gridLocationX + ", " + gridLocationY);
                deleteBlocks(pane, grid, gridLocationX, gridLocationY);
                
            }
    
        }
    
        public static void addBlocks(Pane pane, Block[][] grid, int gridLocationX, int gridLocationY, double mouseX, double mouseY) {
            if (gridLocationX >= 0 &&
                gridLocationY >= 0 &&
                gridLocationX < GRID_SIZEX &&
                gridLocationY < GRID_SIZEY) {
                    if(cursorSize > 0) {
                        for(int i = gridLocationX + cursorSize; i >= gridLocationX - cursorSize; i--) {
                            for(int j = gridLocationY + cursorSize; j >= gridLocationY - cursorSize; j--) {
                                if(i < GRID_SIZEX && i >= 0 && j < GRID_SIZEY && j >= 0) {
                                    if(grid[i][j] == null) {
                                        double placementX = i * ((double) SCREEN_WIDTH / GRID_SIZEX);
                                        double placementY = j * ((double) SCREEN_HEIGHT / GRID_SIZEY);
    
                                        Block newBlock = createNewObject(currentMaterial, placementX, placementY);
                                        System.out.println("added block at: " + i + ", " + j);
                                        pane.getChildren().add(newBlock.getBlockInfo());
                                        System.out.println("rendered");
                                        grid[i][j] = newBlock;
                                        
                                    }
                                }
                            }
                        }
                    } else if (grid[gridLocationX][gridLocationY] == null) {
                        double placementX = findNewLocationSingleBlock(mouseX, VERTICAL);
                        double placementY = findNewLocationSingleBlock(mouseY, HORIZONTAL);
    
                        Block newBlock = createNewObject(currentMaterial, placementX, placementY);
    
                        grid[gridLocationX][gridLocationY] = newBlock;
    
                        pane.getChildren().add(newBlock.getBlockInfo());
                    }
                }
        }
    
        public static void deleteBlocks(Pane pane, Block[][] grid, int gridLocationX, int gridLocationY) {
            if (gridLocationX >= 0 &&
                gridLocationY >= 0 &&
                gridLocationX < GRID_SIZEX &&
                gridLocationY < GRID_SIZEY) {
                if(cursorSize > 0) {
                    for(int i = gridLocationX - cursorSize; i <= gridLocationX + cursorSize; i++) {
                        for(int j = gridLocationY - cursorSize; j <= gridLocationY + cursorSize; j++) {
                            if(i < GRID_SIZEX && i >= 0 && j < GRID_SIZEY && j >= 0) {
                                if(grid[i][j] != null) {
                                    pane.getChildren().remove(grid[i][j].getBlockInfo());
                                    grid[i][j] = null;
                                    System.out.println("Removed");
                                    System.out.println(cursorSize);
                                
                                }
                            }
                        }
                    }
                } else if(grid[gridLocationX][gridLocationY] != null) {
                        pane.getChildren().remove(grid[gridLocationX][gridLocationY].getBlockInfo());
                        grid[gridLocationX][gridLocationY] = null;
                }
                    
            }
    
        }
        
        public static Block createNewObject(Material material, double placementX, double placementY) {
            Block newBlock = null;
            Image image = getMaterialImage(material);

        switch (material) {
            case SAND:
                newBlock = new SandBlock((int) placementX,
                        (int) placementY,
                        (SCREEN_WIDTH / GRID_SIZEX) - GRID_THICKNESS,
                        (SCREEN_HEIGHT / GRID_SIZEY) - GRID_THICKNESS,
                        image);
                break;
            case ROCK:
                newBlock = new RockBlock((int) placementX,
                        (int) placementY,
                        (SCREEN_WIDTH / GRID_SIZEX) - GRID_THICKNESS,
                        (SCREEN_HEIGHT / GRID_SIZEY) - GRID_THICKNESS,
                        image);
                break;
            case WATER:
                newBlock = new WaterBlock((int) placementX,
                        (int) placementY,
                        (SCREEN_WIDTH / GRID_SIZEX) - GRID_THICKNESS,
                        (SCREEN_HEIGHT / GRID_SIZEY) - GRID_THICKNESS,
                        image);
                break;
            case FEATHER:
                newBlock = new FeatherBlock((int) placementX,
                        (int) placementY,
                        (SCREEN_WIDTH / GRID_SIZEX) - GRID_THICKNESS,
                        (SCREEN_HEIGHT / GRID_SIZEY) - GRID_THICKNESS,
                        image);
                break;
            case LINE:
                newBlock = new LineBlock((int) placementX,
                        (int) placementY,
                        (SCREEN_WIDTH / GRID_SIZEX) - GRID_THICKNESS,
                        (SCREEN_HEIGHT / GRID_SIZEY) - GRID_THICKNESS,
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
                BlockXSnap = currentPosition % ((double) SCREEN_WIDTH / GRID_SIZEX);
                break;
            case HORIZONTAL:
                BlockXSnap = currentPosition % ((double) SCREEN_HEIGHT / GRID_SIZEY);
                break;
            default:
                return 0;
        }

        double newPlacement = currentPosition - BlockXSnap + ((double) GRID_THICKNESS / 2);

        return newPlacement;

    }
}
