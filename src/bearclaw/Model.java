package bearclaw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.ArrayList;

public class Model {

    File saveDir = null;

    ArrayList<String> debugLog = new ArrayList<>();         // holds our debug messages

    ArrayList<String> currentKwords = new ArrayList<>();    // holds our current keywords
    ObservableList<String> observableKWords = FXCollections.observableArrayList(currentKwords);
                                                            // kwords observable

    GUI gui;
    int currCategoryID = 64482; // default is sports cards & memorabilia
    final String prefsFile = "prefs.bcs";

    //TODO:
    // -this eventually will hold all of our data, so we need to move the keywords that we
    // will be searching for into this class
    // - this means we need a way to both read and write from SOME SORT OF LIST here
    // - on startup, we need to load some data
    // - whatever directory we chose to save the data to (or have a set default option?)
    // - load whichever keyword list was the last one that we used
    // -
    CategoryManager catManager;

    public Model() {
        // init stuff here
        // eventually all of our list of tags, loading and parsing data
        // and associated things will go here

        // when we first get initialized we want to do some things:
        // find out if we have a default keyword list, and if yes, save it
        // restore the directory we decided to save our excel output to
        catManager = new CategoryManager();
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
        currentKwords = cur;
        observableKWords.setAll(cur);
    }

//    Categories getCurrCategory() {
//
//    }

    int getCurrCategoryID() {
        return currCategoryID;
    }

    void setCurrCategoryID(int n) {
        currCategoryID = n;
    }

    void addToDebug(String toAdd) {
        if (gui != null) gui.setDebugText(toAdd);
        debugLog.add(toAdd);
    }

    void setGui(GUI g) {
        gui = g;
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

    public void addKeyword(String kword) {
        observableKWords.add(kword);
    }

    public void removeKeyword(String kword) {
        observableKWords.remove(kword);
    }

    public void removeKeyword(int index) {
        observableKWords.remove(index);
    }

    public void removeKeywords(ArrayList<String> kwords) {
        observableKWords.removeAll(kwords);
    }
}
