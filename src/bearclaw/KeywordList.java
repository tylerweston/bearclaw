package bearclaw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;


public class KeywordList implements Serializable {
    // This is collecting keywords properly, just needs to be updated somewhere now
    ArrayList<String> keywords = new ArrayList<>();

    // currently in controller uses:
    // ObservableList<String> keywordsObservable = observableArrayList(keywords);
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


    ArrayList<String> getKeywords() {
        return keywords;
    }

    public void addKeyword(String kword) {
        System.out.println("adding a keyword in KeywordList!");
        keywords.add(kword);
        System.out.print(keywords);
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
