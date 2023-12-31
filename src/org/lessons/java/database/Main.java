package org.lessons.java.database;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //parametri di connessione
        String url = "jdbc:mysql://localhost:3306/java_db_nations";
        String user = "root";
        String password = "";

        //provo ad aprire connessione al database
        try(Connection connection = DriverManager.getConnection(url, user, password)) {
            //stampo catalogo db presenti
            System.out.println(connection.getCatalog());

            //statement SQL da eseguire
            String sql = """ 
                    SELECT countries.country_id, countries.name, regions.name, continents.name
                    FROM `countries`
                    JOIN regions ON countries.region_id = regions.region_id 
                    JOIN continents ON regions.continent_id = continents.continent_id 
                    ORDER BY countries.name ASC;
                    """;
            String sql2 = """ 
                    SELECT countries.country_id, countries.name, regions.name, continents.name
                    FROM `countries`
                    JOIN regions ON countries.region_id = regions.region_id
                    JOIN continents ON regions.continent_id = continents.continent_id
                    WHERE countries.name LIKE ? OR regions.name LIKE ? OR continents.name LIKE ?
                    ORDER BY countries.name ASC;
                    """;

            //preparo statement
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                //eseguo statement che mi restituisce resultSet
                try(ResultSet rs = ps.executeQuery()) {
                    //itero sulle righe del resultSet
                    while (rs.next()){
                        String id = rs.getString("country_id");
                        String country = rs.getString("countries.name");
                        String region = rs.getString("regions.name");
                        String continent = rs.getString("continents.name");
                        System.out.println(id + "-" + country + "-" + region + "-" + continent);
                    }
                }
            }

            try(PreparedStatement ps = connection.prepareStatement(sql2)) {
                Scanner utente = new Scanner(System.in);
                System.out.println("Inserisci stringa da ricercare: ");
                String ricerca = utente.nextLine();
                ricerca = "%" + ricerca + "%";

                ps.setString(1,ricerca);
                ps.setString(2,ricerca);
                ps.setString(3,ricerca);
                //eseguo statement che mi restituisce resultSet
                try(ResultSet rs = ps.executeQuery()) {
                    System.out.println("Risultati della tua ricerca: ");
                    //itero sulle righe del resultSet
                    while (rs.next()){
                        String id = rs.getString("country_id");
                        String country = rs.getString("countries.name");
                        String region = rs.getString("regions.name");
                        String continent = rs.getString("continents.name");
                        System.out.println(id + "-" + country + "-" + region + "-" + continent);
                    }
                }
            }

        }catch (SQLException e){
            System.out.println("Unable to connect to database");
            e.printStackTrace();
        }
    }
}
