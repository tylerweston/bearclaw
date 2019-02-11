package bearclaw;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

class MainMenu extends MenuBar {

    private final BooleanProperty setDisabled = new SimpleBooleanProperty(true);

    /**
     * Creates a new main Menu for the program.
     *
     * @param controller    a controller object to manage
     *                      executive functions
     */
    MainMenu(Controller controller) {

        // file menu

        final Menu fileMenu = new Menu("_File");

        MenuItem fileOpen = new MenuItem("_Open");
        fileOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        fileOpen.setOnAction((ae) -> controller.fileLoad());
        fileMenu.getItems().add(fileOpen);

        MenuItem fileSave = new MenuItem("_Save");
        fileSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        fileSave.setOnAction((ae) -> controller.fileSave());
        fileMenu.getItems().add(fileSave);

        MenuItem moveItem = new MenuItem("_Choose");
        moveItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
        moveItem.setOnAction((ae) -> controller.chooseDir());
        fileMenu.getItems().add(moveItem);

        fileMenu.getItems().add(new SeparatorMenuItem());

        MenuItem fileExit = new MenuItem("E_xit");
        fileExit.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN));
        fileExit.setOnAction((ae) -> controller.doExit());
        fileMenu.getItems().add(fileExit);

        // view menu



        // tools menu

        final Menu toolsMenu = new Menu("_Tools");

        MenuItem toolsBatchGenerate = new MenuItem("_Batch Generate");
        toolsBatchGenerate.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN));
        toolsBatchGenerate.setOnAction((ae) -> controller.batchGenerate());
        //toolsBatchGenerate.disableProperty().bind(setDisabled);

        MenuItem toolsEditSubsets = new MenuItem("_Edit Subsets");
        toolsEditSubsets.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
        toolsEditSubsets.setOnAction((ae) -> controller.doEdit());
        toolsEditSubsets.disableProperty().bind(setDisabled);

        MenuItem toolsDebugLog = new MenuItem("_Log");
        toolsDebugLog.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
        toolsDebugLog.setOnAction((ae) -> controller.openLog());
//        toolsRenamingLog.disableProperty().bind(setDisabled);


        MenuItem toolsSetDefaultKeywords = new MenuItem("Set _Default Keywords");
        toolsSetDefaultKeywords.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN));
        toolsSetDefaultKeywords.setOnAction((ae) -> controller.saveKeywords());

        MenuItem toolsSetDefaultFolder = new MenuItem("Set Default _Folder");
        toolsSetDefaultFolder.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN));
        toolsSetDefaultFolder.setOnAction((ae) -> controller.setDefaultFolder());


        toolsMenu.getItems().add(toolsBatchGenerate);
//        toolsMenu.getItems().add(toolsEditSubsets);
        toolsMenu.getItems().add(toolsSetDefaultKeywords);
        toolsMenu.getItems().add(toolsSetDefaultFolder);
//        toolsMenu.getItems().add(new SeparatorMenuItem());

        // Debug menu

        final Menu debugMenu = new Menu("Debug");

        MenuItem debugDumpKwords = new MenuItem("Dump KWords");
        debugDumpKwords.setOnAction((ae) -> controller.dumpKwords());

        debugMenu.getItems().add(toolsDebugLog);
        debugMenu.getItems().add(debugDumpKwords);


        // about menu

        final Menu aboutMenu = new Menu("_About");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction((ae) -> controller.showAbout());
        aboutMenu.getItems().add(aboutItem);

        this.getMenus().addAll(fileMenu , toolsMenu, /*debugMenu,*/ aboutMenu);    // add tools back in if needed
    }

    /**
     * Enable menu options that should only be available when a photo is loaded
     */
    void enableView() {
        setDisabled.setValue(false);
    }
}

