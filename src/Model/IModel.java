package Model;

import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;

import java.util.LinkedList;

public interface IModel {
    void generateMaze(int rows,int columns);
    void solveMaze();
    LinkedList<String> getSolution();
    void moveCharacter(KeyCode direction);
    int[][] getMaze();
    void generateMaze(byte[] loadedMaze);
    byte[] getMazeBytes();
    int getCharacterRowPosition();
    int getCharacterColumnPosition();
    int getGoalRowPosition();
    int getGoalColumnPosition();
    void stopServers();
    void playMusic();

    void stopMusic();

    void setVolume(double value);
}
