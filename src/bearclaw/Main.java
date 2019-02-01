package bearclaw;

/*
    bearclaw ebay price history amalgamator / spreadsheet generator
    tyler weston, dec 31st 2018

    todo:

    - detect if keywords has changed since last save, prompt to save if so
    -

    - add more options (ie what data to write to file !! )
    - add keyword name to top of each sheet (?)
    - allow different category IDs (!)
    - don't generate repots if keywords are empty
    - cleaup GUI, move to XML with controllers and whatnot? how does that even work?
    - debug log window is buggy LUL
    - include saving the category ID with list so that you can have different lists
      for different categories (ie, load a list and it will automatically set category ID)
    - change report generating to handle different category IDS
    - put category ids into a list to make choosing easier

    bugs:
    - menu shows up in the wrong place sometimes (?) only seen once, javafx problem /w ubuntu?

    useful category IDS:
    Collectibles	                    1
    Antiques	                        20081
    Art	                                550
    Books	                            267
    Video Games & Consoles	            1249
    Coins & Paper Money	                11116
    Music	                            11233
    Toys & Hobbies	                    220
    Sporting Goods	                    888
    Sports Mem, Cards & Fan Shop	    64482
    Entertainment Memorabilia	        45100
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
