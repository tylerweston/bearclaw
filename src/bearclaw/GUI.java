package bearclaw;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI {
    static Text debug = new Text();

    private final Controller controller;
    private final Stage stage;
    private final MainMenu controllerMenu;

    public GUI(Stage primaryStage, Controller setController) {
        this.controller = setController;
        stage = primaryStage;

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.doExit();
            }
        });

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        primaryStage.setTitle("BearClaw v0.1");
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);

        HBox bottom = new HBox();

        //final Text debug = new Text();

        Button genButton = new Button("Generate");
        Button addButton = new Button("Add Term");
        Button remButton = new Button("Remove Term");

        javafx.scene.control.Label st = new javafx.scene.control.Label("Search Terms");
        final ListView<String> searchTermDisplay = new ListView<String>();

        final TextField addTagText = new TextField("Add tags here...");

        addTagText.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            // if we click on the box for the first time, erase it's contents
            if (addTagText.getCharacters().toString().compareTo("Add tags here...") == 0) {
                addTagText.setText("");
            }
            }
        });

        genButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            setDebugText("Generating");
            controller.generateReport();
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            setDebugText("Adding tag");
            controller.addItem(addTagText.getCharacters().toString());
            addTagText.setText("");
                    }
        });

        remButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            setDebugText("Removing tag");
            controller.removeItem(searchTermDisplay.getSelectionModel().getSelectedIndex());
                    }
        });

        bottom.getChildren().addAll(genButton, addButton, remButton);




        root.add(st,1,1);
        root.add(searchTermDisplay, 1, 2);
        root.add(addTagText, 1,3);
        root.add(bottom, 1, 4);
        root.add(debug, 1, 5);
        searchTermDisplay.setItems(controller.searchTermsObservable);

        controllerMenu = new MainMenu(controller);
        controllerMenu.prefWidthProperty().bind(scene.widthProperty());
        root.add(controllerMenu,1,0);

        primaryStage.show();
}
    void setDebugText(String msg) {
        debug.setText(msg);
        controller.addDebugLog(msg);
    }

    Stage getStage() {
        return stage;
    }
}
