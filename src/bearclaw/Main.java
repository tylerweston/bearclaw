package bearclaw;

/*
    bearclaw ebay price history amalgamator / spreadsheet generator
    tyler weston, dec 31st 2018

    todo:

    - testing, testing, testing
    - can this handle weird characters?
    - is capitalization important?
    - batch processing - set a default batch processing folder
    - write filename should be name of loaded list + date + .bc
    - should prefs.bcs have a separate file for default writing XL and default batch processing?
    - is there any way to generate a useful graph from this
        data (???)
    - parsing of cards that get returned, separate
        - DATE (how to get something that looks like a date?)
        - run of cards (pull from database)
    - right now its just list spaghetti, got to optimize and pretty it up
    - detect if keywords has changed since last save, prompt to save if so

    - add more options (ie what data to write to file !! )
    - allow different category IDs (!)
    - cleaup GUI, move to XML with controllers and whatnot? how does that even work?
    - debug log window is buggy LUL
    - change report generating to handle different category IDS
    - move things to packages?

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
        controller.loadDefaults();
    }

    public static void main(String[] args) {
        launch(args);
        // calls start on nonjavafx enabled machines (?)
    }

}
