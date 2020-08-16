package Assets.Codes;

public class Model_Bill {
    private String Product_Name;
    private String Orientation;
    private String Category;


    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getOrientation() {
        return Orientation;
    }

    public void setOrientation(String orientation) {
        Orientation = orientation;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public Model_Bill() {
    }

    public Model_Bill(String product_Name, String orientation, String category) {
        Product_Name = product_Name;
        Orientation = orientation;
        Category = category;
    }
}