package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class CharacterDisplayer extends Canvas {

    private int[][] maze;
    private int characterPositionRow;
    private int characterPositionColumn;

    public int[][] getMaze() {
        return maze;
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
    }
    public void setCharacterPosition(int row,int column){
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double minCanvas = Math.min(canvasHeight, canvasWidth);
            int minSize = Math.min(maze.length, maze[0].length);
            double cellHeight = minCanvas / minSize;
            double cellWidth = minCanvas / minSize;
            Image characterImage = new Image(this.getClass().getResourceAsStream(ImageFileNameCharacter.get()));
            GraphicsContext gp = getGraphicsContext2D();
            gp.clearRect(0, 0, getWidth(), getHeight());
            gp.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);

        }
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }
    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public void clearCharacter() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
    }
}
