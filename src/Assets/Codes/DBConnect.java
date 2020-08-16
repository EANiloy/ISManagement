package Assets.Codes;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
    public static Connection connect;
        public Connection connectDb(){
                try{

                    final String url="jdbc:mysql://127.0.0.1:3306/db_ussc";
                    final String user="USSC";
                    final String pass="sqlroot";
                    connect= DriverManager.getConnection(url,user,pass);
                    return connect;
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null,"Connection to the database is lost. Please check the connection and restart the application");
                        System.exit(0);
                }
                return null;
        }

    }
