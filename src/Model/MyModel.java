package Model;

import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;

public class MyModel extends Observable implements IModel {
    private Server mazeGeneratingServer;
    private Server solveSearchProblemServer;
    private Solution solMaze;
    private MediaPlayer clip;
    private MediaPlayer generalClip;
    private boolean stop=false;

    public MyModel() {
        //Raise the servers
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

    }

    public void startServers() {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }

    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
        stopMusic();
        Platform.exit();

    }

    private Maze maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private int goalPositionRow;
    private int goalPositionColumn;

    @Override
    public void generateMaze(int width, int height) {
        //Generate maze
        Platform.runLater(() -> {
            try {
                if(generalClip != null)
                    stopMusic();
                Socket theServer = new Socket(InetAddress.getLocalHost(),5400);
                ObjectOutputStream toServer = new ObjectOutputStream(theServer.getOutputStream());
                ObjectInputStream fromServer = new ObjectInputStream(theServer.getInputStream());
                toServer.flush();
                solMaze=null;
                int row = width;
                int column = height;
                if (row<6|| column<6){
                    row=6;
                    column=6;
                    showAlert("Wrong parameters. Generating a basic 6X6 maze");
                }
                int[] mazeDimensions = new int[]{row, column};
                toServer.writeObject(mazeDimensions);
                toServer.flush();
                byte[] compressedMaze = (byte[]) fromServer.readObject();
                InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                byte[] decompressedMaze = new byte[mazeDimensions[0] * mazeDimensions[1] + compressedMaze[0]];
                is.read(decompressedMaze);
                maze = new Maze(decompressedMaze);
                characterPositionColumn = maze.getStartPosition().getColumnIndex();
                characterPositionRow = maze.getStartPosition().getRowIndex();
                goalPositionColumn = maze.getGoalPosition().getColumnIndex();
                goalPositionRow = maze.getGoalPosition().getRowIndex();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers("generate");

        });
    }

    @Override
    public void solveMaze() {
        Platform.runLater(() -> {
            try {
                if(generalClip != null)
                    stopMusic();
                Socket theServer = new Socket(InetAddress.getLocalHost(), 5401);
                ObjectOutputStream toServer = new ObjectOutputStream(theServer.getOutputStream());
                ObjectInputStream fromServer = new ObjectInputStream(theServer.getInputStream());
                toServer.writeObject(maze);
                toServer.flush();
                Solution solution = (Solution) fromServer.readObject();
                solMaze=solution;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers("solve");
        });
    }

    @Override
    public void generateMaze(byte[] loadedMaze){
        Platform.runLater(() -> {
            maze = new Maze(loadedMaze);
            characterPositionColumn = maze.getStartPosition().getColumnIndex();
            characterPositionRow = maze.getStartPosition().getRowIndex();
            goalPositionColumn = maze.getGoalPosition().getColumnIndex();
            goalPositionRow = maze.getGoalPosition().getRowIndex();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers("generate");
        });
    }



    @Override
    public int[][] getMaze() {
        return maze.getMaze();
    }

    @Override
    public byte[] getMazeBytes() {
        return maze.toByteArray();
    }

    @Override
    public int getCharacterRowPosition() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterColumnPosition() {
        return characterPositionColumn;
    }

    @Override
    public int getGoalRowPosition() {
        return goalPositionRow;
    }

    @Override
    public int getGoalColumnPosition() {
        return goalPositionColumn;
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        String key = "move";
        switch (movement) {
            case UP:
                if(maze.getMaze()[characterPositionRow-1][characterPositionColumn]==0) {
                    characterPositionRow--;
                    playMove();
                }
                else
                    showAlert("Can't move up");
                break;
            case NUMPAD8:
                if(maze.getMaze()[characterPositionRow-1][characterPositionColumn]==0) {
                    characterPositionRow--;
                    playMove();
                }
                else
                    showAlert("Can't move up");
                break;
            case DOWN:
                if(maze.getMaze()[characterPositionRow+1][characterPositionColumn]==0) {
                    characterPositionRow++;
                    playMove();
                }
                else
                    showAlert("Can't move down");
                break;
            case NUMPAD2:
                if(maze.getMaze()[characterPositionRow+1][characterPositionColumn]==0) {
                    characterPositionRow++;
                    playMove();
                }
                else
                    showAlert("Can't move down");
                break;
            case RIGHT:
                if(maze.getMaze()[characterPositionRow][characterPositionColumn+1]==0) {
                    characterPositionColumn++;
                    playMove();
                }
                else
                    showAlert("Can't move right");
                break;
            case NUMPAD6:
                if(maze.getMaze()[characterPositionRow][characterPositionColumn+1]==0) {
                    characterPositionColumn++;
                    playMove();
                }
                else
                    showAlert("Can't move right");
                break;
            case LEFT:
                if(maze.getMaze()[characterPositionRow][characterPositionColumn-1]==0) {
                    characterPositionColumn--;
                    playMove();
                }
                else
                    showAlert("Can't move left");
                break;
            case NUMPAD4:
                if(maze.getMaze()[characterPositionRow][characterPositionColumn-1]==0) {
                    characterPositionColumn--;
                    playMove();
                }
                else
                    showAlert("Can't move left");
                break;
            case NUMPAD7:
                if(maze.getMaze()[characterPositionRow-1][characterPositionColumn-1]==0) {
                    characterPositionColumn--;
                    characterPositionRow--;
                    playMove();
                }
                else
                    showAlert("Can't move up-left");
                break;
            case NUMPAD1:
                if(maze.getMaze()[characterPositionRow+1][characterPositionColumn-1]==0) {
                    characterPositionColumn--;
                    characterPositionRow++;
                    playMove();
                }
                else
                    showAlert("Can't move down-left");
                break;
            case NUMPAD3:
                if(maze.getMaze()[characterPositionRow+1][characterPositionColumn+1]==0) {
                    characterPositionColumn++;
                    characterPositionRow++;
                    playMove();
                }
                else
                    showAlert("Can't move down-right");
                break;
            case NUMPAD9:
                if(maze.getMaze()[characterPositionRow-1][characterPositionColumn+1]==0) {
                    characterPositionColumn++;
                    characterPositionRow--;
                    playMove();
                }
                else
                    showAlert("Can't move up-right");
                break;
        }
        int rowGoal=getGoalRowPosition();
        int columnGoal=getGoalColumnPosition();
        if(rowGoal==getCharacterRowPosition()&&columnGoal==getCharacterColumnPosition() && movement != KeyCode.ENTER)
            playWin();
        if(movement == KeyCode.ENTER)
            key = "enter";
        setChanged();
        notifyObservers(key);
    }

    private void playWin(){
        try {
            final URL music = this.getClass().getResource("/sound/Short_triumphal_fanfare-John_Stracke-815794903.mp3");
            Media sound =new Media(music.toString());
            clip = new MediaPlayer(sound);
            clip.play();

        }catch (Exception e ){

        }
        if(generalClip!=null)
            stopMusic();
    }

    private void playMove() {
        try {
            final URL music = this.getClass().getResource("/sound/cartoon015.mp3");
            Media sound =new Media(music.toString());
            clip = new MediaPlayer(sound);
            clip.play();
        } catch (Exception e) {

        }
    }


    @Override
    public LinkedList<java.lang.String> getSolution() {
        LinkedList<java.lang.String> Sol=new LinkedList<>();
        ArrayList<AState> temp=solMaze.getSolutionPath();
        for( int i=0;i<temp.size();i++){
            Sol.add(temp.get(i).toString());
        }
        return Sol;
    }
    public void playMusic() {
        if(generalClip==null) {
            try {
                final URL music = this.getClass().getResource("/sound/Pacman_Introduction_Music-KP-826387403.mp3");
                Media sound =new Media(music.toString());
                generalClip = new MediaPlayer(sound);
                generalClip.setCycleCount(1000);
                generalClip.play();
            } catch (Exception e) {

            }
        }
    }

    public void stopMusic(){
        if(generalClip != null) {
            generalClip.stop();
            generalClip= null;
        }


    }

    @Override
    public void setVolume(double value) {
        if(generalClip != null)
            generalClip.setVolume(value);
    }

    private void showAlert(java.lang.String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

}
