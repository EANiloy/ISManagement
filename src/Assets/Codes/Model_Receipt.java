package Assets.Codes;

public class Model_Receipt {

    public Model_Receipt(String productname, int quantity, int price, int total,int code) {
        Productname = productname;
        Quantity = quantity;
        Price = price;
        Total = total;
        Code = code;
    }

    String Productname;
    int Quantity;
    int Price;
    int Total;
    int Code;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String productname) {
        Productname = productname;
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

    public Model_Receipt() {
    }
}
