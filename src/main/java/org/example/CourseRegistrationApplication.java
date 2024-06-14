//baigiamoji versija
package org.example;
import java.sql.*;

public class CourseRegistrationApplication {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/baigiamasis"; // JÅ«sÅ³ MySQL duomenÅ³ bazÄ—s adresas ir pavadinimas
        String username = "root"; // JÅ«sÅ³ MySQL vartotojo vardas
        String password = "jakuliene"; // JÅ«sÅ³ MySQL slaptaÅ¾odis

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Sekmingai prisijungta prie MySQL duomenu bazes!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
