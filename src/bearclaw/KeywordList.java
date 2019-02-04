package bearclaw;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;


public class KeywordList implements Serializable {
    ArrayList<String> keywords = new ArrayList<>();
    // or use ObservableList<T> observableList = FXCollections.observableArrayList(); ??

    // currently in controller uses:
    // ArrayList<String> searchTerms = new ArrayList<>();    // do it this way so we can serialize this
     ObservableList<String> keywordsObservable = observableArrayList(keywords);
    Categories myCategory;

    public KeywordList() {
        // do any initialization here?
    }

    public void setMyCategory(Categories myCategory) {
        this.myCategory = myCategory;
    }

    String getMyCategory() {
        return myCategory.getCategoryName();
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    ObservableList<String> getKeywordsObservable() {
        return keywordsObservable;
    }

    public void addKeyword(String kword) {
        keywordsObservable.add(kword);
    }

    public void removeKeyword(String kword) {
        keywordsObservable.remove(kword);
    }

    public void removeKeyword(int index) {
        keywordsObservable.remove(index);
    }

    public void removeKeywords(ArrayList<String> kwords) {
        keywordsObservable.removeAll(kwords);
    }

}
