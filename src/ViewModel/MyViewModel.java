package ViewModel;

import Model.IModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;
    private int goalPositionRowIndex;
    private int goalPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding
    public StringProperty goalPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty goalPositionColumn = new SimpleStringProperty("1"); //For Binding

    public MyViewModel(IModel model){
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            characterPositionRowIndex = model.getCharacterRowPosition();
            characterPositionRow.set(characterPositionRowIndex + "");
            characterPositionColumnIndex = model.getCharacterColumnPosition();
            characterPositionColumn.set(characterPositionColumnIndex + "");
            goalPositionRowIndex = model.getGoalRowPosition();
            goalPositionRow.set(goalPositionRowIndex + "");
            goalPositionColumnIndex = model.getGoalColumnPosition();
            goalPositionColumn.set(goalPositionColumnIndex + "");
            setChanged();
            notifyObservers(arg);
        }
    }

    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
    }
    public void generateMaze(byte[] loadedMaze){model.generateMaze(loadedMaze);}

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }

    public int[][] getMaze() {
        return model.getMaze();
    }
    public byte[] getMazeBytes(){
        return model.getMazeBytes();
    }
    public void solveMaze(){
        model.solveMaze();
    }
    public LinkedList<String> getSol(){return model.getSolution();}

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }

    public int getGoalPositionRow() {
        return goalPositionRowIndex;
    }

    public int getGoalPositionColumn() {
        return goalPositionColumnIndex;
    }
    public void closeServer(){
        model.stopServers();
    }
    public void playMusic(){
        model.playMusic();
    }

    public void stopMusic() {
        model.stopMusic();
    }

    public void setVolume(double value) {
        model.setVolume(value);
    }
}
