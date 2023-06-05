package praca;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.JOptionPane;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *Trieda slúžiaca na vytvorenie prepojenia aplikačnej časti informačného systému ZZS, s databázou
 * @author Peter Rendek
 */
public class DBConnection {
    Connection conn = null;
    /**
     *Metóta, ktorá vracia spojenie s databázou ZZS
     * Údaje potrebné na získanie spojenia musia byť uložené v súbore:
     * C:\Users\Desktop\ZZS\config.txt
     * @return con - spojenie sa databázou
     */
    public static Connection DBConnection() {
        try {           
            File file =  new File(System.getProperty("user.home")+"\\desktop\\ZZS\\config.txt");
            Scanner sc = new Scanner(file);
            String ip = sc.next();
            String port = sc.next();
            String DBName = sc.next();
            String username = sc.next();
            String password = sc.next();           
            sc.close();            
            String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            Class.forName(driverName);            
            String url = "jdbc:sqlserver://" + ip + ":" + port + ";databaseName="
                    + DBName + ";user=" + username + ";password=" + password;
            Connection con = DriverManager.getConnection(url);
            System.out.println("Succesfully Connected to the database !!! ");
            //JOptionPane.showMessageDialog(null, "Podarilo sa pripojiť k databáze", "Info", JOptionPane.INFORMATION_MESSAGE);
            return con;
        } catch (ClassNotFoundException e) {
            //System.out.println("Could not find the database driver !!! " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Nenašiel sa databázový ovládač \n" + e.getMessage(), "Chyba",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (SQLException e) {
            //System.out.println("Could not connect to database !!!" + e.getMessage());
            JOptionPane.showMessageDialog(null, "Nepodarilo sa pripojiť k databáze \n" + e.getMessage(), "Chyba",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            //System.out.println("Could not connect to database !!!" + e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Chyba", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
 /**
     *Metóta, ktorá vracia spojenie s databázou ZZS
     * využívaná pri vývoji aplikácie, pre beh nie je potrebná
     * @return con - spojenie sa databázou
     */
//public static Connection DBConnection1() {
//    try {
//        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//        Class.forName(driverName);String DBName = "BakalarkaDB;";
//        String username = "remo;";String password = "rendo";
//        String port = "1433;";String url = "jdbc:sqlserver://192.168.1.32:"
//        + port + "databaseName="      + DBName + "user=" + username +
//        "password=" + password;
//        Connection con = DriverManager.getConnection(url);
//        System.out.println("Succesfully Connected to the database !!! ");            
//        return con;
//    } catch (ClassNotFoundException e) {            
//     JOptionPane.showMessageDialog(null, "Nenašiel sa databázový ovládač \n"
//     + e.getMessage(), "Chyba",JOptionPane.ERROR_MESSAGE);
//     return null;
//    } catch (SQLException e) {            
//     JOptionPane.showMessageDialog(null, "Nepodarilo sa pripojiť k databáze"
//     + " \n" + e.getMessage(), "Chyba",JOptionPane.ERROR_MESSAGE);
//     return null;}}
}
