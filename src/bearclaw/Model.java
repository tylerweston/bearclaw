package bearclaw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private final double ver = 0.2;

    private File saveDir = null;
    private String loadedFilename;

    private ArrayList<String> debugLog = new ArrayList<>();         // holds our debug messages

    private ArrayList<String> currentKwords = new ArrayList<>();    // holds our current keywords
    private ObservableList<String> observableKWords = FXCollections.observableArrayList(currentKwords);
                                                            // kwords observable
    private Controller controller;
    private ArrayList<String> hockeySets;

    private int currCategoryID = 64482; // default is sports cards & memorabilia
    private final String prefsFile = "prefs.bcs";
    private boolean hasChanged = false;

    //TODO:
    // -this eventually will hold all of our data, so we need to move the keywords that we
    // will be searching for into this class
    // - this means we need a way to both read and write from SOME SORT OF LIST here
    // - on startup, we need to load some data
    // - whatever directory we chose to save the data to (or have a set default option?)
    // - load whichever keyword list was the last one that we used
    // -
    private CategoryManager catManager;

    Model() {
        // init stuff here
        // eventually all of our list of tags, loading and parsing data
        // and associated things will go here

        // when we first get initialized we want to do some things:
        // find out if we have a default keyword list, and if yes, save it
        // restore the directory we decided to save our excel output to
        catManager = new CategoryManager();

//        loadHockeySets();
    }

    void setController(Controller c) {
        controller = c;
    }

    void loadHockeySets() {
        hockeySets = new ArrayList<>();
        File subsetFile= new File("subsets.txt");
        addToDebug("Grabbing subsets list");
        try {
            List<String> line = Files.readAllLines(subsetFile.toPath(), Charset.forName("UTF-8"));
            hockeySets.addAll(line.subList(0, line.size()));
            addToDebug("Succesfully found subsets list");
        }catch (Exception e) {
            addToDebug("Subsets list not found");
            e.printStackTrace();
        }
    }

    String getLoadedFilename() {
        return loadedFilename;
    }

    void setLoadedFilename(String s) {
        loadedFilename = s;
    }

    ArrayList<String> getHockeySets() {
        return hockeySets;
    }

    ObservableList<String> getObservableKWords() {
        return observableKWords;
    }

    String getPrefsFile() {
        return prefsFile;
    }

    ArrayList<String> getCurrentKwords(){
        return currentKwords;
    }

    void setCurrentKwords(ArrayList<String> cur) {
        // todo:
        // still a bug in here somewhere?
        hasChanged = false;
        currentKwords = cur;
//        observableKWords = FXCollections.observableArrayList(currentKwords);
        observableKWords.clear();
        observableKWords.setAll(cur);
    }

    int getCurrCategoryID() {
        return currCategoryID;
    }

    void setCurrCategoryID(int n) {
        currCategoryID = n;
        hasChanged = true;
    }

    void addToDebug(String toAdd) {
        if (controller.getGUI() != null) controller.getGUI().setDebugText(toAdd);
        debugLog.add(toAdd);
    }

    ArrayList<String> getDebugLog() {
        return debugLog;
    }

    void setSaveDir(File saveDir) {
        this.saveDir = saveDir;
    }

    File getSaveDir() {
        return this.saveDir;
    }

    CategoryManager getCatMan() {
        return catManager;
    }

    void addKeyword(String kword) {
        observableKWords.add(kword);
        hasChanged = true;
    }

    public void removeKeyword(String kword) {
        observableKWords.remove(kword);
        hasChanged = true;
    }

    void removeKeyword(int index) {
        observableKWords.remove(index);
        hasChanged = true;
    }

    void removeKeywords(ArrayList<String> kwords) {
        observableKWords.removeAll(kwords);
        hasChanged = true;
    }

    boolean isChanged() {
        return hasChanged;
    }

    void setHasChanged(boolean val) {
        hasChanged = val;
    }

    double getVer() {
        return ver;
    }
}
