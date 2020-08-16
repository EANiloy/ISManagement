package Assets.Codes;

public class Model_Sales {
    String Product;
    String Orientation;
    int Quantity;
    int Price;
    int Total;

    public Model_Sales() {
    }

    public Model_Sales(String product, String orientation, int quantity, int price, int total) {
        Product = product;
        Orientation = orientation;
        Quantity = quantity;
        Price = price;
        Total = total;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
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

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }
}
