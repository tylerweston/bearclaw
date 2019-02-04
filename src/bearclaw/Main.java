package bearclaw;

/*
    bearclaw ebay price history amalgamator / spreadsheet generator
    tyler weston, dec 31st 2018

    todo:

    - give jane big kisses
    - loading keywords does NOT work, this needs to
    - detect if keywords has changed since last save, prompt to save if so
    - loading keywords is not acting as expected right now
    - need to select a default category for category ID?

    - add more options (ie what data to write to file !! )
    - add keyword name to top of each sheet (?)
    - allow different category IDs (!)
    - don't generate repots if keywords are empty
    - cleaup GUI, move to XML with controllers and whatnot? how does that even work?
    - debug log window is buggy LUL
    - include saving the category ID with list so that you can have different lists
      for different categories (ie, load a list and it will automatically set category ID)
        - this will involve changing the keyword list to be an object that includes both the
          list and the category that is attached to the list
    - change report generating to handle different category IDS
    - move things to packages?
    - Loading keywords does NOT work yet, fix this



    bugs:
    - menu shows up in the wrong place sometimes (?) only seen once, javafx problem /w ubuntu?
 */


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {


    private Controller controller;
    @Override

    public void start(Stage primaryStage) throws Exception {

        Model model = new Model();
        controller= new Controller(model);
        GUI gui = new GUI(primaryStage, controller);

        controller.setGUI(gui);
    }

    public static void main(String[] args) {
        launch(args);
        // calls start on nonjavafx enabled machines (?)
    }

}
