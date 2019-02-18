package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private int goalPositionRow;
    private int goalPositionColumn;

    public void setMaze(int[][] maze) {
        this.maze = maze;
        redraw();
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
    }
    public void setGoalPosition(int row,int column){
        goalPositionColumn = column;
        goalPositionRow = row;
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double minCanvas = Math.min(canvasHeight, canvasWidth);
            int minSize = Math.min(maze.length, maze[0].length);
            double cellHeight = minCanvas / minSize;
            double cellWidth = minCanvas / minSize;

            Image wallImage = new Image(this.getClass().getResourceAsStream(ImageFileNameWall.get()));
            Image goalImage = new Image(this.getClass().getResourceAsStream(ImageFileNameExit.get()));
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            //Draw Maze
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    if (maze[i][j] == 1) {
                        if (i == characterPositionRow && j == characterPositionColumn)
                            continue;
                        gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellHeight, cellWidth);

                    }
                }
            }

            //Draw Character
            gc.drawImage(goalImage, goalPositionColumn * cellHeight, goalPositionRow * cellWidth, cellHeight, cellWidth);
        }
    }
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameExit = new SimpleStringProperty();
    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }
    public String getImageFileNameExit(){
        return ImageFileNameExit.get();
    }
    public void setImageFileNameExit(String imageFileNameExit) {
        this.ImageFileNameExit.set(imageFileNameExit);
    }
    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }
    public void clearMaze(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
    }

}
