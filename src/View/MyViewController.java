package View;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import ViewModel.MyViewModel;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.scene.text.Text;

import java.io.*;
import java.util.*;

public class MyViewController implements Observer,IView {

    private String Anger = "/Images/character1.png";
    private String BlueMonster = "/Images/character2.png";
    private String Dora = "/Images/character3.png";
    private String Pikachu = "/Images/character4.png";
    private String Dexter = "/Images/character5.png";
    private String Blossom  = "/Images/character6.png";
    @FXML
    private MyViewModel myViewModel;
    private ObservableList<String> characters;
    public MazeDisplayer mazeDisplayer;
    public SolveDisplayer solveDisplayer;
    public CharacterDisplayer characterDisplayer;
    public ChoiceBox chosenCharacter;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    public javafx.scene.control.Button btn_playMusic;
    public javafx.scene.image.ImageView anger;
    public javafx.scene.image.ImageView blueMonster;
    public javafx.scene.image.ImageView dora;
    public javafx.scene.image.ImageView pikachu;
    public javafx.scene.image.ImageView dexter;
    public javafx.scene.image.ImageView blossom;
    public javafx.scene.image.ImageView Exit;
    public javafx.scene.text.Text angerText;
    public javafx.scene.text.Text blueMonsterText;
    public javafx.scene.text.Text doraText;
    public javafx.scene.text.Text pikachuText;
    public javafx.scene.text.Text dexterText;
    public javafx.scene.text.Text blossomText;
    public javafx.scene.text.Text ExitText;
    public javafx.scene.image.ImageView winner;
    public javafx.scene.control.Slider volumeController;

    public void setMyViewModel(MyViewModel myViewModel){
        this.myViewModel = myViewModel;
        bindProperties(myViewModel);
    }

    private void bindProperties(MyViewModel myViewModel) {
        lbl_rowsNum.textProperty().bind(myViewModel.characterPositionRow);
        lbl_columnsNum.textProperty().bind(myViewModel.characterPositionColumn);
    }
    @Override
    public void displayMaze(int[][] maze) {
        solveDisplayer.clear();
        int characterRowPosition = myViewModel.getCharacterPositionRow();
        int characterColumnPosition = myViewModel.getCharacterPositionColumn();
        int goalRowPosition = myViewModel.getGoalPositionRow();
        int goalColumnPosition = myViewModel.getGoalPositionColumn();
        characterChoose();
        solveDisplayer.setMaze(maze);
        characterDisplayer.setMaze(maze);
        characterDisplayer.setCharacterPosition(characterRowPosition,characterColumnPosition);
        mazeDisplayer.setCharacterPosition(characterRowPosition,characterColumnPosition);
        mazeDisplayer.setGoalPosition(goalRowPosition,goalColumnPosition);
        mazeDisplayer.setMaze(maze);
        this.characterRowPosition.set(characterRowPosition+"");
        this.characterColumnPosition.set(characterColumnPosition+"");
        this.goalRowPosition.set(goalRowPosition+"");
        this.goalColumnPosition.set(goalColumnPosition+"");
    }
    public void solveMaze(ActionEvent actionEvent){
        btn_generateMaze.setDisable(true);
        btn_solveMaze.setDisable(true);
        btn_playMusic.setDisable(true);
        myViewModel.solveMaze();
        btn_generateMaze.setDisable(false);
        btn_solveMaze.setDisable(false);
        btn_playMusic.setDisable(false);
    }
    public void generateMaze(){
        int row = Integer.valueOf(txtfld_rowsNum.getText());
        int column = Integer.valueOf(txtfld_columnsNum.getText());
        solveDisplayer.setSOL(null);
        btn_generateMaze.setDisable(true);
        btn_solveMaze.setDisable(true);
        btn_playMusic.setDisable(true);
        myViewModel.generateMaze(row,column);
        btn_generateMaze.setDisable(false);
        btn_solveMaze.setDisable(false);
        btn_playMusic.setDisable(false);
    }
    public void playMusic(){
        volumeController.setValue(100);
        myViewModel.playMusic();
        volumeController.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(javafx.beans.Observable observable) {
                myViewModel.setVolume(volumeController.getValue()/100);
            }
        });
    }
    public void KeyPressed(KeyEvent keyEvent){
        myViewModel.moveCharacter(keyEvent.getCode());
        btn_generateMaze.setDisable(false);
        keyEvent.consume();
    }
    public StringProperty characterRowPosition = new SimpleStringProperty();
    public StringProperty characterColumnPosition = new SimpleStringProperty();
    public StringProperty goalRowPosition = new SimpleStringProperty();
    public StringProperty goalColumnPosition = new SimpleStringProperty();

    public String getCharacterRowPosition(){
        return characterRowPosition.get();
    }
    public String getCharacterColumnPosition(){
        return characterColumnPosition.get();
    }
    public StringProperty characterPositionRowProperty() {
        return characterRowPosition;
    }
    public StringProperty characterPositionColumnProperty(){return characterColumnPosition;}

    public void setResizeEvent(Scene scene){

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                display();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                display();
            }
        });
    }

    private void display() {
        mazeDisplayer.redraw();
        characterDisplayer.redraw();
        if(solveDisplayer.initSol())
            solveDisplayer.redraw();
    }

    public void About(ActionEvent actionEvent){
        try{
            Stage stage = new Stage();
            stage.setTitle("AboutController");
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root,400,300);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {

        }
    }


    @Override
    public void update(Observable o, Object arg) {
        if(o==myViewModel){
            if(arg.equals("generate"))
                displayMaze(myViewModel.getMaze());
            else if(arg.equals("move")) {
                characterDisplayer.setCharacterPosition(myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionColumn());
                if(characterDisplayer.getCharacterPositionColumn()==myViewModel.getGoalPositionColumn() && characterDisplayer.getCharacterPositionRow()== myViewModel.getGoalPositionRow() && !solveDisplayer.initSol())
                    displayWin();
                else if(characterDisplayer.getCharacterPositionColumn()==myViewModel.getGoalPositionColumn() && characterDisplayer.getCharacterPositionRow()== myViewModel.getGoalPositionRow()){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Another game?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        generateMaze();
                    } else {

                        clearScreen();
                    }

                }

            }
            else if(!arg.equals("enter")) {
                solveDisplayer.setSOL(myViewModel.getSol());
                btn_solveMaze.setDisable(false);
            }
            btn_generateMaze.setDisable(false);

        }
    }

    private void displayWin() {
        Image Winner = new Image(this.getClass().getResource("/Images/giphy.gif").toExternalForm());
        winner.setImage(Winner);
        Text text = new Text();
        Group root = new Group();
        root.getChildren().add(winner);
        root.getChildren().add(text);
        Scene scene = new Scene(root,300,300);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Another game?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    generateMaze();
                } else {

                    clearScreen();
                }
            }
        });

    }


    public void loadMaze(ActionEvent actionEvent) {
        FileChooser load = new FileChooser();
        String fileName="";
        int fileTypeIndex=0;
        File mazeFile= null;
        load.setTitle("Open a maze");
        do {
            mazeFile = load.showOpenDialog(null);
            fileName = mazeFile.getName();
            fileTypeIndex = fileName.lastIndexOf('.');
            if(!fileName.substring(fileTypeIndex+1).equals("maze"))
                showAlert("Wrong file \n Please choose a .maze file");
        }
        while(!fileName.substring(fileTypeIndex+1).equals("maze"));
        try {
            InputStream br = new MyDecompressorInputStream(new FileInputStream(mazeFile));
            byte[] details = new byte[1];
            br.read(details,0,1);
            int length = details[0];
            details = new byte[length];
            br.read(details,0,length);
            length=0;
            for(int i=0;i<details.length-1;i++)
                length+=details[i];

            byte[] loadedMaze= new byte[length];
            br.read(loadedMaze);
            solveDisplayer.setSOL(null);
            myViewModel.generateMaze(loadedMaze);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void showAlert(java.lang.String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    public void saveMaze(ActionEvent actionEvent) {
        FileChooser save = new FileChooser();
        String fileName;
        int fileTypeIndex=0;
        save.setTitle("Save maze");
        File maze;
        do {
            maze = save.showSaveDialog(null);
            fileName = maze.getName();
            fileTypeIndex = fileName.lastIndexOf('.');
            if(!fileName.substring(fileTypeIndex+1).equals("maze"))
                showAlert("You must save the Maze as *.maze file type");
        }
        while(!fileName.substring(fileTypeIndex+1).equals("maze"));
        byte[] mazeInBytes = myViewModel.getMazeBytes();
        LinkedList<String> mazeFinal = new LinkedList<>();
        int index = 1;
        if(mazeInBytes.length<127)
            mazeFinal.add(String.valueOf(mazeInBytes.length));
        else{
            int length = mazeInBytes.length;
            index=0;
            while(length>127){
                mazeFinal.add((String.valueOf(127)));
                index++;
                length-=127;
            }
            mazeFinal.add(String.valueOf(length));
            index++;

        }
        mazeFinal.add(String.valueOf(-1));
        index++;
        mazeFinal.addFirst(String.valueOf(index));
        for(int i=0;i<mazeInBytes.length;i++)
            mazeFinal.add(String.valueOf(mazeInBytes[i]));
        byte[] mazeForSave = new byte[mazeFinal.size()];
        for(int j=0; j<mazeFinal.size();j++)
            mazeForSave[j] = Byte.parseByte(mazeFinal.get(j));

        try {
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(maze));
            out.write(mazeForSave);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public void prop(ActionEvent actionEvent) {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/config.properties"));
            Stage stage = new Stage();
            stage.setTitle("Properties");
            stage.initModality(Modality.APPLICATION_MODAL);
            String searchAlgo = properties.getProperty("searchingAlgorithm");
            String generateAlgo = properties.getProperty("generatingAlgo");
            Text text1 = new Text();
            Text text2 = new Text();
            Text headLine = new Text();
            headLine.setX(50);
            headLine.setY(50);
            text1.setX(50);
            text1.setY(120);
            text2.setX(50);
            text2.setY(150);
            headLine.setText("Hello and welcome to the game properties\nThe properties are:");
            text1.setText("The searching algorithm is: "+searchAlgo);
            text2.setText("The generation algorithm is: "+generateAlgo);
            headLine.setFont(Font.font("Ariel",FontWeight.BOLD,FontPosture.ITALIC,20));
            text1.setFont(Font.font("Ariel",FontWeight.BOLD,FontPosture.ITALIC,15));
            text2.setFont(Font.font("Ariel",FontWeight.BOLD,FontPosture.ITALIC,15));
            text1.setFill(Color.BURLYWOOD);
            text2.setFill(Color.BURLYWOOD);
            Group root = new Group(headLine,text1,text2);
            Scene scene = new Scene(root,500,550);
            stage.setScene(scene);
            stage.show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void exit() {
        myViewModel.closeServer();
    }

    public void help(ActionEvent actionEvent) {
        setHelpPics();
        setText();
        Stage stage = new Stage();
        stage.setTitle("Help");
        Group root = new Group(anger,blueMonster,dora,pikachu,dexter,blossom,Exit,angerText,blueMonsterText,doraText,pikachuText,dexterText,blossomText,ExitText);
        Scene scene = new Scene(root,700,700);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void setText() {
        angerText.setText("Anger is a very angry person.\nEvery hour he needs to go to the relax room or else he will explode.\nHelp him find the way to the relax room");
        blueMonsterText.setText("Blue monster is a monster who came from the monster world.\nThe blue moster tried to scare a little child but didn't succeed.\nHelp her run from the humans that are after her!");
        doraText.setText("Dora is a curious girl.\nShe likes to explore and find treasures.\nHelp her find the way to the outside world");
        pikachuText.setText("Pikachu is an electric Pokémon who got lost in the real world.\nHelp him find his way back to Ash Ketchum and the Pokémon world");
        dexterText.setText("Dexeter is the smarest kid on erath.\nYesterday he went out and forgot the way to the lab.\nHelp him find the way back!");
        blossomText.setText("Blossom is the leader of 'The Powerpuff Girls'.\nShe is very smart,responsable and loves the color pink.\nWhile the last mission her sisters were locked in a secret room.\nHelp here find the secret door");
        ExitText.setText("The exit door.\nTo win the game you need to find the way to get to this door");
        angerText.setFont(Font.font("Freestyle Script",FontWeight.BOLD,FontPosture.ITALIC,20));
        blueMonsterText.setFont(Font.font("Freestyle Script",FontWeight.BOLD,FontPosture.ITALIC,20));
        doraText.setFont(Font.font("Freestyle Script",FontWeight.BOLD,FontPosture.ITALIC,20));
        pikachuText.setFont(Font.font("Freestyle Script",FontWeight.BOLD,FontPosture.ITALIC,20));
        dexterText.setFont(Font.font("Freestyle Script",FontWeight.BOLD,FontPosture.ITALIC,20));
        blossomText.setFont(Font.font("Freestyle Script",FontWeight.BOLD,FontPosture.ITALIC,20));
        ExitText.setFont(Font.font("Freestyle Script",FontWeight.BOLD,FontPosture.ITALIC,20));


    }


    private void setHelpPics() {
        Image angerImage = new Image(this.getClass().getResourceAsStream(Anger));
        Image blueMonsterImage = new Image(this.getClass().getResourceAsStream(BlueMonster));
        Image doraImage = new Image(this.getClass().getResourceAsStream(Dora));
        Image pikachuImage = new Image(this.getClass().getResourceAsStream(Pikachu));
        Image dexterImage = new Image(this.getClass().getResourceAsStream(Dexter));
        Image blossomImage = new Image(this.getClass().getResourceAsStream(Blossom));
        Image exitImage = new Image(this.getClass().getResourceAsStream("/Images/exit.png"));
        anger.setImage(angerImage);
        blueMonster.setImage(blueMonsterImage);
        dora.setImage(doraImage);
        pikachu.setImage(pikachuImage);
        dexter.setImage(dexterImage);
        blossom.setImage(blossomImage);
        Exit.setImage(exitImage);


        anger.setX(50);
        anger.setY(10);
        blueMonster.setX(50);
        blueMonster.setY(70);
        dora.setX(50);
        dora.setY(130);
        pikachu.setX(50);
        pikachu.setY(190);
        dexter.setX(50);
        dexter.setY(250);
        blossom.setX(50);
        blossom.setY(310);
        Exit.setX(50);
        Exit.setY(370);
    }
    @FXML
    private void initialize(){

        characters = FXCollections.observableArrayList("Anger","BlueMonster","Dora","Pikachu","Dexter","Blossom");
        chosenCharacter.setItems(characters);
        chosenCharacter.setValue("Anger");


    }
    public void characterChoose() {
        String character = chosenCharacter.getSelectionModel().getSelectedItem().toString();
        switch (character){
            case "Anger":
                character = Anger;
                break;
            case "BlueMonster":
                character = BlueMonster;
                break;
            case "Dora":
                character = Dora;
                break;
            case "Pikachu":
                character = Pikachu;
                break;
            case "Dexter":
                character = Dexter;
                break;

            case "Blossom":
                character=Blossom;
                break;
        }
        characterDisplayer.setImageFileNameCharacter(character);
    }

    public void stopMusic(ActionEvent actionEvent) {
        myViewModel.stopMusic();
    }
    private void clearScreen(){
        mazeDisplayer.clearMaze();
        characterDisplayer.clearCharacter();
        solveDisplayer.clear();
    }
}
