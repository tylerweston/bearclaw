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
//        fileOpen.setOnAction((ae) -> controller.openFile());
        fileMenu.getItems().add(fileOpen);

        MenuItem moveItem = new MenuItem("_Move");
        moveItem.setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN));
//        moveItem.setOnAction((ae) -> controller.moveFile());
        fileMenu.getItems().add(moveItem);

        fileMenu.getItems().add(new SeparatorMenuItem());

        MenuItem fileExit = new MenuItem("E_xit");
        fileExit.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN));
//        fileExit.setOnAction((ae) -> controller.doExit());
        fileMenu.getItems().add(fileExit);

        // view menu

        final Menu viewMenu = new Menu("_View");

        MenuItem viewActualSize = new MenuItem("Actual Size");
//        viewActualSize.setOnAction((ae) -> controller.zoomActualSize());
        viewActualSize.disableProperty().bind(setDisabled);
        viewMenu.getItems().add(viewActualSize);

        MenuItem viewFitWindow = new MenuItem("Fit to window");
//        viewFitWindow.setOnAction((ae) -> controller.zoomFitWindow());
        viewFitWindow.disableProperty().bind(setDisabled);
        viewMenu.getItems().add(viewFitWindow);

        MenuItem view50Zoom = new MenuItem("Zoom 50%");
//        view50Zoom.setOnAction((ae) -> controller.zoom50());
        view50Zoom.disableProperty().bind(setDisabled);
        viewMenu.getItems().add(view50Zoom);

        MenuItem viewZoomIn = new MenuItem("Zoom In");
//        viewZoomIn.setOnAction((ae) -> controller.zoomIn());
        viewZoomIn.disableProperty().bind(setDisabled);
        viewZoomIn.setAccelerator(new KeyCodeCombination(KeyCode.EQUALS));
        viewMenu.getItems().add(viewZoomIn);

        MenuItem viewZoomOut = new MenuItem("Zoom In");
//        viewZoomOut.setOnAction((ae) -> controller.zoomOut());
        viewZoomOut.disableProperty().bind(setDisabled);
        viewZoomOut.setAccelerator(new KeyCodeCombination(KeyCode.MINUS));
        viewMenu.getItems().add(viewZoomOut);

        viewMenu.getItems().add(new SeparatorMenuItem());

        MenuItem showFolder = new MenuItem("Show File in _Browser");
        showFolder.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN));
        showFolder.disableProperty().bind(setDisabled);
//        showFolder.setOnAction((ae) -> controller.showInBrowser());
        viewMenu.getItems().add(showFolder);

        // tools menu

        final Menu toolsMenu = new Menu("_Tools");

        MenuItem toolsSuggestTag = new MenuItem("Suggest _Tags");
        toolsSuggestTag.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN));
//        toolsSuggestTag.setOnAction((ae) -> controller.getSuggestedTags());
        toolsSuggestTag.disableProperty().bind(setDisabled);


        MenuItem toolsRenamingLog = new MenuItem("_Renaming Log");
        toolsRenamingLog.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
//        toolsRenamingLog.setOnAction((ae) -> controller.openLog());

        MenuItem toolsTagFolder = new MenuItem("Tag _Folder");
        toolsTagFolder.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN));
//        toolsTagFolder.setOnAction((ae) -> controller.TagFolder());



        toolsMenu.getItems().add(toolsSuggestTag);
        toolsMenu.getItems().add(new SeparatorMenuItem());
        toolsMenu.getItems().add(toolsRenamingLog);
        toolsMenu.getItems().add(toolsTagFolder);


        // about menu

        final Menu aboutMenu = new Menu("_About");
        MenuItem aboutItem = new MenuItem("About");
//        aboutItem.setOnAction((ae) -> controller.showAbout());
        aboutMenu.getItems().add(aboutItem);

        this.getMenus().addAll(fileMenu, viewMenu, toolsMenu, aboutMenu);
    }

    /**
     * Enable menu options that should only be available when a photo is loaded
     */
    void enableView() {
        setDisabled.setValue(false);
    }
}

