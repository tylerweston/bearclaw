package bearclaw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GUI {

    static Text debug = new Text();

    private final Controller controller;
    private final Stage stage;
    final Timer timer = new Timer(true);

    private final MainMenu controllerMenu;
    ArrayList<String> sItems;
    TextField addTagText;
    final ComboBox idChoice;

    ListView<String> searchTermDisplay;


    public GUI(Stage primaryStage, Controller setController) {
        this.controller = setController;
        stage = primaryStage;
        // set icon
        stage.getIcons().add(
                new Image(GUI.class.getResourceAsStream( "icon.png" )));

        // handle exit
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.doExit();
            }
        });

        TimerTask task = new TimerTask()
        {
            public void run()
            {
                setDebugText("");
            }

        };
        timer.schedule(task, 5000l,5000l);
        // automatically clear debug text every now and then

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        primaryStage.setTitle("BearClaw v" + controller.getModel().getVer());
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);

        HBox bottom = new HBox();

        //final Text debug = new Text();

        Button genButton = new Button("Generate");
        Button addButton = new Button("Add Term");
        Button remButton = new Button("Remove Term");

        javafx.scene.control.Label st = new javafx.scene.control.Label("Search Terms");

        searchTermDisplay = new ListView<String>();
        searchTermDisplay.setItems(controller.getModel().getObservableKWords());

        searchTermDisplay.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        searchTermDisplay.getSelectionModel().selectedItemProperty()
                .addListener((c, oldSelect, newSelect) ->
                        sItems = new ArrayList<>(searchTermDisplay.getSelectionModel().getSelectedItems()));

        // remove items by pressing delete or backspace key
        searchTermDisplay.setOnKeyPressed((ke) -> {
            if (ke.getCode() == KeyCode.DELETE || ke.getCode() == KeyCode.BACK_SPACE) {
                doDelete();
            }
        });

        addTagText = new TextField("Add tags here...");

        // if you clicked the text field and it still says "Add tags here..." clear it for user
        addTagText.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            // if we click on the box for the first time, erase it's contents
                if (addTagText.getCharacters().toString().compareTo("Add tags here...") == 0) {
                    addTagText.setText("");
                }
            }
        });

        // add tags by pressing enter
        addTagText.setOnKeyPressed((ke) -> {
            if (ke.getCode() == KeyCode.ENTER) {
                controller.addDebugLog("Adding keyword!");
                addKeyword();
            }
        });

        // generate a report
        genButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            setDebugText("Generating");
            controller.generateReport();
            }
        });

        // add a keyword button
        addButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
                addKeyword();
            }
        });



        // remove a keyword
        remButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                doDelete();
            }
        });

        ObservableList<String> options =
                FXCollections.observableArrayList();
        for (Categories c: controller.getModel().getCatMan().getCategories()) {
            options.add(c.toString());
        }

        idChoice = new ComboBox(options);
        idChoice.getSelectionModel().select("Sports Memorabilia & Cards");
        idChoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setDebugText(idChoice.getSelectionModel().getSelectedItem().toString());
                controller.setCategory(idChoice.getSelectionModel().getSelectedItem().toString());
            }
        });

        // build bottom buttons
        bottom.getChildren().addAll(genButton, addButton, remButton);
        bottom.setAlignment(Pos.CENTER);

        // set default insets
        Insets defIn = new Insets(5,5,5,5);
        GridPane.setMargin(st, defIn);
        GridPane.setMargin(searchTermDisplay, defIn);
        GridPane.setMargin(addTagText, defIn);
        GridPane.setMargin(idChoice, defIn);
        GridPane.setHalignment(idChoice, HPos.CENTER);
        GridPane.setMargin(debug, defIn);

        // add everything to our gridpane
        root.add(st,1,1);
        root.add(searchTermDisplay, 1, 2);
        root.add(addTagText, 1,3);
        root.add(bottom, 1, 4);
        root.add(idChoice, 1, 5);
        root.add(debug, 1, 6);


        // build main menu
        controllerMenu = new MainMenu(controller);
        controllerMenu.prefWidthProperty().bind(scene.widthProperty());
        root.add(controllerMenu,1,0);

        // show everything!
        primaryStage.show();
    }

    void setDebugText(String msg) {
        debug.setText(msg);
    }

    void addKeyword() {
        controller.addItem(addTagText.getCharacters().toString());
        controller.addDebugLog("Adding keyword " + addTagText.getCharacters().toString());
        addTagText.setText("");
        addTagText.requestFocus();
    }

    void setSearchTermDisplay(ObservableList<String> s) {
        searchTermDisplay.setItems(s);
    }

    void doDelete() {
        controller.addDebugLog("Removing tag");

        if (sItems.size() != 0) {
            controller.removeItems(sItems);
            controller.addDebugLog("Deleting multiple items!");
        } else {
            int selectIndex = searchTermDisplay.getSelectionModel().getSelectedIndex();
            if (selectIndex != -1) controller.removeItem(selectIndex);
        }
    }

    void setComboBox(String s) {
        idChoice.getSelectionModel().select(s);
    }

    Stage getStage() {
        return stage;
    }







}
