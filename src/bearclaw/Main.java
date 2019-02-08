package bearclaw;

/*
    bearclaw ebay price history amalgamator / spreadsheet generator
    tyler weston, dec 31st 2018

    todo:

    - ONCE WE LOAD A KEYWORD LIST, OBSERVABLES ARE NOT UPDATED PROPERLY
    - detect if keywords has changed since last save, prompt to save if so!
    - batch processing - test and check this out
    - problem with deleting keywords!

    - simple built in text editor for subset list (not necessary, cut entirely? or leave till last)
        - save subset text
        - fix up editor gui

    - testing, testing, testing
    - can this handle weird characters?
    - is capitalization important?
    - should prefs.bcs have a separate file for default writing XL and default batch processing?
    - is there any way to generate a useful graph from this
        data (???)

    - add more options (ie what data to write to file !! )
    - cleaup GUI, move to XML with controllers and whatnot? how does that even work?

 */


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {


    private Controller controller;
    @Override

    public void start(Stage primaryStage) throws Exception {

        Model model = new Model();
        controller = new Controller(model);
        model.setController(controller);
        GUI gui = new GUI(primaryStage, controller);

        // model.setGui(gui);
        controller.setGUI(gui);
        controller.loadDefaults();
    }

    public static void main(String[] args) {
        launch(args);
        // calls start on nonjavafx enabled machines (?)
    }

}
