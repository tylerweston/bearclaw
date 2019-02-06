package bearclaw;

public class Categories {

    private String categoryName;
    private int categoryID;

    public Categories(String myName, int myID) {
        categoryName = myName;
        categoryID = myID;
    }

    public String toString() {
        return categoryName;
    }

    public int getID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

}
