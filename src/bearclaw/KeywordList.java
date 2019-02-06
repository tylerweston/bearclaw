package bearclaw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;


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
