package Assets.Codes;

public class Model_Customer {
    private int ID;
    private String Name;
    private String Contact;
    private String Address;
    private int Paid;
    private int Due;
    private int Advance;
    private int Quantity;

    public Model_Customer() {
    }

    public Model_Customer(int ID, String name, String contact, String address, int paid, int due, int advance, int quantity) {
        this.ID = ID;
        Name = name;
        Contact = contact;
        Address = address;
        Paid = paid;
        Due = due;
        Advance = advance;
        Quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getPaid() {
        return Paid;
    }

    public void setPaid(int paid) {
        Paid = paid;
    }

    public int getDue() {
        return Due;
    }

    public void setDue(int due) {
        Due = due;
    }

    public int getAdvance() {
        return Advance;
    }

    public void setAdvance(int advance) {
        Advance = advance;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
