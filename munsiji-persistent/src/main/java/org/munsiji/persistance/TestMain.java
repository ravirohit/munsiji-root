package org.munsiji.persistance;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TestMain {

	public static void main(String[] args) {
		System.out.println("checking JDBC connection to the mySql");
		try {
			Connection con =  DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "password");
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("select * from test");
			System.out.println("connection established successfully");
			System.out.println("Please find the below data from test table:");
			while(rs.next()){
				System.out.println("vaue in rs:\n ID:"+rs.getInt(1)+" emailId: "+rs.getString(2) + " Name: "+rs.getString(3));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
      System.out.println("database connnected successfully");
	}

}
