package bearclaw;

/*
    bearclaw ebay price history amalgamator / spreadsheet generator
    tyler weston, dec 31st 2018

    todo:

    - Ok, so now if we DELETE a keyword, we can no longer add keywords?? (?)
    - ONCE WE LOAD A KEYWORD LIST, OBSERVABLES ARE NOT UPDATED PROPERLY
    - if the first keyword is empty the rest of report will not be generated?
    - problem with deleting keywords!
    - simple built in text editor for subset list (not necessary, cut entirely? or leave till last)
        - save subset text
        - fix up editor gui
    - testing, testing, testing
    - can this handle weird characters?
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

        controller.setGUI(gui);
        controller.loadDefaults();
    }

    public static void main(String[] args) {
        launch(args);
        // calls start on nonjavafx enabled machines (?)
    }

}
