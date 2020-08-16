package Assets.Codes;

public class Model_Report {
    int Serial;
    String Name;
    int Amount;
    int Percentage;

    public int getSerial() {
        return Serial;
    }

    public void setSerial(int serial) {
        Serial = serial;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getPercentage() {
        return Percentage;
    }

    public void setPercentage(int percentage) {
        Percentage = percentage;
    }

    public Model_Report() {
    }

    public Model_Report(int serial, String name, int amount, int percentage) {
        Serial = serial;
        Name = name;
        Amount = amount;
        Percentage = percentage;
    }
}
