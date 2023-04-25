package com.aqat.cidr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@SpringBootApplication
public class CidrManagementApplication {
    public static void createTable() {
        String createTableQuery= " create table if not exists ip_addresses \n" +
                "                (\n" +
                "                \tid long not null auto_increment,\n" +
                "                \tip varchar(15),\n" +
                "                \tstatus varchar(9) default true\n" +
                "                )";
        try {
            String jdbcURL = "jdbc:h2:mem:maindb";
            Connection connection = DriverManager.getConnection(jdbcURL,"sa","");

            // Step 2:Create a statement using connection object
            Statement statement = connection.createStatement();

            // Step 3: Execute the query or update query
            statement.execute(createTableQuery);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

        SpringApplication.run(CidrManagementApplication.class, args);
        createTable();

    }

}
