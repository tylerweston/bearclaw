package bearclaw;

import java.io.Serializable;
import java.util.ArrayList;


public class KeywordList implements Serializable {

    ArrayList<String> keywords = new ArrayList<>();
    int myCategory;

    public KeywordList() {
        // do any initialization here?
    }

    public void setMyCategory(int myCategory) {
        this.myCategory = myCategory;
    }

    int getMyCategory() {
        return myCategory;
    }

    ArrayList<String> getKeywords() {
        return keywords;
    }
}
