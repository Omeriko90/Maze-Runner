package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.LinkedList;

public class SolveDisplayer extends Canvas {

    private int[][] maze;
    private LinkedList<String> sol;

    public int[][] getMaze() {
        return maze;
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
    }

    public void  setSOL(LinkedList<String> sol){
        this.sol=sol;
        if(sol!=null)
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
            Image characterImage = new Image(this.getClass().getResourceAsStream(ImageFileNameSol.get()));
            GraphicsContext gp = getGraphicsContext2D();
            gp.clearRect(0, 0, getWidth(), getHeight());
            for (int i = 0; i < sol.size(); i++) {
                String position = sol.get(i);
                int comma = position.indexOf(',');
                int row = Integer.valueOf(position.substring(1, comma));
                int column = Integer.valueOf(position.substring(comma + 1, position.length() - 1));
                gp.drawImage(characterImage, column * cellHeight, row * cellWidth, cellHeight, cellWidth);
            }
        }
    }

    public void clear(){
        GraphicsContext gp = getGraphicsContext2D();
        gp.clearRect(0, 0, getWidth(), getHeight());
    }
    private StringProperty ImageFileNameSol = new SimpleStringProperty();
    public String getImageFileNameSol() {
        return ImageFileNameSol.get();
    }
    public void setImageFileNameSol(String imageFileNameSol) {
        this.ImageFileNameSol.set(imageFileNameSol);
    }
    public boolean initSol(){
        return sol!=null;
    }

}
