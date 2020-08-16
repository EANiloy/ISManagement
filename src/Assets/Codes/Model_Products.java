package Assets.Codes;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class Model_Products extends RecursiveTreeObject<Model_Products> {
    public int Prodcut_id;
    public String Product_Name;
    public String Category;
    public String Orientation;
    public int Quantity;


    public Model_Products() {
    }

    public Model_Products(int prodcut_id, String product_Name, String category, String orientation, int quantity) {
        Prodcut_id = prodcut_id;
        Product_Name = product_Name;
        Category = category;
        Orientation = orientation;
        Quantity = quantity;
    }

    public int getProdcut_id() {
        return Prodcut_id;
    }

    public void setProdcut_id(int prodcut_id) {
        Prodcut_id = prodcut_id;
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
}

