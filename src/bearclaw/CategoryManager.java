package bearclaw;

import java.util.ArrayList;
/*
            Collectibles	                    1
            Antiques	                        20081
            Art	                                550
            Books	                            267
            Video Games & Consoles	            1249
            Coins & Paper Money	                11116
            Music	                            11233
            Toys & Hobbies	                    220
            Sporting Goods	                    888
            Sports Mem, Cards & Fan Shop	    64482
            Entertainment Memorabilia	        45100
*/

public class CategoryManager {

    private static ArrayList<Categories> categories = new ArrayList<Categories>();

    public CategoryManager() {
        categories.add(new Categories("Collectibles", 1));
        categories.add(new Categories("Antiques", 20081));
        categories.add(new Categories("Art", 550));
        categories.add(new Categories("Books", 267));
        categories.add(new Categories("Video Games & Consoles", 1249));
        categories.add(new Categories("Coins & Paper Money", 11116));
        categories.add(new Categories("Music", 11233));
        categories.add(new Categories("Toys & Hobbies", 220));
        categories.add(new Categories("Sporting Goods", 888));
        categories.add(new Categories("Sports Memorabilia & Cards", 64482));
        categories.add(new Categories("Entertainment Memorabilia", 45100));
    }

    public ArrayList<Categories> getCategories() {
        return categories;
    }

    public static int getID(String s) {
        // return category ID associated with string, -1 if category DNE
        // runs worst case theta(n)
        for (Categories c: categories) {
            if (s.compareTo(s) == 0) return c.getID();
        }
        return -1;
    }

}
