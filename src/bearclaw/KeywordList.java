package bearclaw;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;


public class KeywordList implements Serializable {
    ArrayList<String> keywords;
    // or use ObservableList<T> observableList = FXCollections.observableArrayList(); ??

    // currently in controller uses:
    // ArrayList<String> searchTerms = new ArrayList<>();    // do it this way so we can serialize this
    // ObservableList<String> searchTermsObservable = observableArrayList(searchTerms);
    Categories myCategory;

    public KeywordList() {
        // do any initialization here?
    }

    public void setMyCategory(Categories myCategory) {
        this.myCategory = myCategory;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void addKeyword(String kword) {
        keywords.add(kword);
    }

    public void removeKeyword(String kword) {
        keywords.remove(kword);
    }

    public void removeKeyword(int index) {
        keywords.remove(index);
    }

    public void removeKeywords(ArrayList<String> kwords) {
        keywords.removeAll(kwords);
    }

}
