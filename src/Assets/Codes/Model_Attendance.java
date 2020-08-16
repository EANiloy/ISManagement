package Assets.Codes;

public class Model_Attendance {
    private String Date;
    private String Login_time;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLogin_time() {
        return Login_time;
    }

    public void setLogin_time(String login_time) {
        Login_time = login_time;
    }

    public String getLogout_time() {
        return Logout_time;
    }

    public void setLogout_time(String logout_time) {
        Logout_time = logout_time;
    }

    private String Logout_time;

    public Model_Attendance(){}
    public Model_Attendance(String date, String login_time, String logout_time) {
        Date = date;
        Login_time = login_time;
        Logout_time = logout_time;
    }
}
