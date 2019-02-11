package bearclaw;

import java.io.Serializable;
import java.util.ArrayList;

class KeywordList implements Serializable {

    ArrayList<String> keywords = new ArrayList<>();
    private int myCategory;

    KeywordList() {
        // do any initialization here?
    }

    void setMyCategory(int myCategory) {
        this.myCategory = myCategory;
    }

    int getMyCategory() {
        return myCategory;
    }

    ArrayList<String> getKeywords() {
        return keywords;
    }
}
