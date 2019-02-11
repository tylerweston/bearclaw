package bearclaw;

public class Categories {

    private String categoryName;
    private int categoryID;

    Categories(String myName, int myID) {
        categoryName = myName;
        categoryID = myID;
    }

    public String toString() {
        return categoryName;
    }

    int getID() {
        return categoryID;
    }

    String getCategoryName() {
        return categoryName;
    }

}
