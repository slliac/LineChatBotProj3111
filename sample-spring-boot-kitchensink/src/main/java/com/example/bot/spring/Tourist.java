package com.example.bot.spring;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Logger;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;


/***
 * 
 * 
 * @author Gordon
 * 
 * Tourist info would be stored in here.
 *
 */

public class Tourist{

	public String name;
	public String TourName;
	public int status;
	/**
	 * 
	 * 
	 * @param t is the tourist
	 */
	public Tourist(Tourist t) {
		this.name = t.name;
		this.TourName = t.TourName;
		this.status = t.status;
	}
	
	/**
	 * 
	 * 
	 * @param name of constructor
	 * @param status is the name of status
	 */
	public Tourist(String name,int status) {
		this.name = name;
		this.status = status;
	}
	/**
	 * 
	 * 
	 * 
	 * @param tourist is object of tourist
	 * @return the string that tour name and return details
	 */
	public String getTourName(Tourist tourist) {
    	try {
    		int number = 0 ; 
    		Connection connection = getConnection3();
    		PreparedStatement stmt = connection.prepareStatement("SELECT tripname FROM TourInf where name = ? ");
    		stmt.setString(1,tourist.getName());
    		ResultSet rs = stmt.executeQuery();
    		while(rs.next()) {
    			TourName = rs.getString("tripname");
    		}
            rs.close();
            stmt.close();
            connection.close();
         return TourName;
    		}catch(Exception e) {
    		
    		}
    	return TourName;
	}
	/*
	public void CancelTour(Tourist tourist,String tripName) {
	try {
		Connection connection = getConnection3();
		PreparedStatement stmt = connection.prepareStatement("DELETE FROM TourInf where name = ? AND tripname = ?");
		stmt.setString(1,tourist.getName());	
		stmt.setString(2,tripName);	
		stmt.executeQuery();
        stmt.close();
        connection.close();
	}
	catch(Exception e) 
	{
	}
	}
	*/
	
	/**
	 * 
	 * 
	 * @return name of customer
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * @param status of the customer
	 * @param tourName of the tour that users want to searched
	 */
	public void update(int status,String tourName) {
		
		this.status = status;
			//changes status of users 
		Connection connection = null;
		PreparedStatement stmt = null;
		//try {
			
		//}
/*
		catch (URISyntaxException e1) {
			//log.info("URISyntaxException while closing file: {}", e1.toString());
		}
		catch (SQLException e2) {
			//log.info("URISyntaxException while closing file: {}", e2.toString());
		}
*/		
			try {
				   connection = getConnection3();
				   stmt = connection.prepareStatement("UPDATE TourInf SET tripstatus = ? WHERE name = ? and tripname = ?  ");
				   stmt.setInt(1,status);
				   stmt.setString(2,name);
				   stmt.setString(3,tourName);
				   stmt.executeUpdate();
			       stmt.close();
			       connection.close();
				}		catch (Exception e) {
					//log.info("SQLException while closing file: {}", e3.toString());
				}
				
		}
	/**
	 * 
	 * 
	 * 
	 * 
	 * @return Conneciton that we want
 	 * @throws URISyntaxException url exception
	 * @throws SQLException sql exception 
	 */
	private Connection getConnection3() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI("postgres://itniqlvmjieeeq:1bf7632721043bf02f0f498974ece3939ed3e947b9c96435ca0ced9d06593f33@ec2-107-22-235-167.compute-1.amazonaws.com:5432/daj5i9364g5dfv");
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}
	
	}
	


	

