package Assets.Codes;

public class Model_Employee {
    private int emp_id;
    private String emp_name;
    private String cont_no;
    private String address;

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getCont_no() {
        return cont_no;
    }

    public void setCont_no(String cont_no) {
        this.cont_no = cont_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Model_Employee(int emp_id, String emp_name, String cont_no, String address) {
        this.emp_id = emp_id;
        this.emp_name = emp_name;
        this.cont_no = cont_no;
        this.address = address;
    }
    public Model_Employee(){}
}
