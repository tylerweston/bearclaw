package bearclaw;

import java.io.File;

public class Model {

    File saveDir;

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
