package bearclaw;

import java.io.File;

public class Model {

    File saveDir = null;

    //TODO:
    // -this eventually will hold all of our data, so we need to move the keywords that we
    // will be searching for into this class
    // - this means we need a way to both read and write from SOME SORT OF LIST here
    // - on startup, we need to load some data
    // - whatever directory we chose to save the data to (or have a set default option?)
    // - load whichever keyword list was the last one that we used
    // -

    public Model() {
        // init stuff here
        // eventually all of our list of tags, loading and parsing data
        // and associated things will go here
    }

    void setSaveDir(File saveDir) {
        this.saveDir = saveDir;
    }

    File getSaveDir() {
        return this.saveDir;
    }
}
