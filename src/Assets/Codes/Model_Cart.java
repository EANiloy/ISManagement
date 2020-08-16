package Assets.Codes;

import com.jfoenix.controls.JFXTextField;

public class Model_Cart {
    private String Company;
    private String Product_Name;
    private String Category;
    private String Orientation;
    private int Quantity;
    private JFXTextField Price;

    public JFXTextField getPrice() {
        return Price;
    }

    public void setPrice(JFXTextField price) {
        Price = price;
    }

    public Model_Cart(String company, String product_Name, String category, String orientation, int quantity,JFXTextField price) {
        Company = company;
        Product_Name = product_Name;
        Category = category;
        Orientation = orientation;
        Quantity = quantity;
        this.Price = new JFXTextField();
        Price.setPromptText("Please Enter Price");
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getOrientation() {
        return Orientation;
    }

    public void setOrientation(String orientation) {
        Orientation = orientation;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public Model_Cart() {
    }
}
