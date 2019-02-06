package bearclaw;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SimpleEditor {

    Controller controller;

    public void SimpleEditor(Controller c){
        controller = c;
    }

    public void showEditor() {

        Stage editStage = new Stage();
        BorderPane root = new BorderPane();
        root.setPrefSize(640, 480);
        Scene editScene = new Scene(root);

        editStage.setScene(editScene);
        editStage.setTitle("Subset Editor");

        TextArea logListView = new TextArea();

        try {
            StringBuilder sr = new StringBuilder();
            File file = new File("subsets.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                sr.append(sc.nextLine());
                sr.append("\n");
            }

            logListView.setText(sr.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        VBox logBox = new VBox();
        logBox.setPadding(new Insets(10, 10, 10, 10));
        logBox.getChildren().add(logListView);

        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(10, 10, 10, 10));
        Button OK = new Button("OK");
        OK.setOnMouseClicked((ae) -> {
            // do saving and whatnot here if necessary
            // how to get contents of textArea
            // and write out to file
            editStage.close();
        });
        bottom.getChildren().add(OK);

        root.setCenter(logBox);
        root.setBottom(bottom);

        editStage.show();
    }
}
