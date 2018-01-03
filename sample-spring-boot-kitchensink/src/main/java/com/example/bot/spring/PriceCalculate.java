package com.example.bot.spring;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

/**
 * 
 * PriceCalculate class extends from Gordon 
 * 
 * @author Gordon
 *
 */
public class PriceCalculate {

	public Tourist tourist;
	
	public List<Integer> childnumList;
	public List<Integer> AdultnumList;
	public List<Integer> toodlernumList;
	public int childnum;
	public int adultnum;
	public int toodlernum;
	public List<String> TripList;
	public String [] Description;
	//public List<Integer> price ; 
	public Person price1;
	public Person price2;
	public Person price3;
	public List<Integer> paid;
	public boolean back_school = false;
	public Activity activities_discount;
	public int [] price ;
	Connection connection = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	
	/**
	 * 
	 * We would likely to develop the function for calculated the price
	 * by different class that we used.
	 * 
	 * 
	 * 
	 * @param t is the tourist that come from customer
	 * 
	 * 
	 */
	public PriceCalculate(Tourist t) {
		childnumList = new ArrayList<Integer>();
		AdultnumList= new ArrayList<Integer>();
		toodlernumList= new ArrayList<Integer>();
		TripList = new ArrayList<String>();
		paid = new ArrayList<Integer>();
		try {
		int tripPrice = getTripprice("tripB");
		this.tourist = t;
		try {
			connection = getConnection4();
		}
		catch (URISyntaxException e1) {
			//log.info("URISyntaxException while closing file: {}", e1.toString());
		}
		catch (SQLException e2) {
			//log.info("URISyntaxException while closing file: {}", e2.toString());
		}
		stmt = connection.prepareStatement("SELECT childnum,adultnum,toodlernum,tripname,amount_paid from TourInf where name = ? ");
		stmt.setString(1,tourist.getName());
        rs = stmt.executeQuery();
	    while(rs.next()) {
	    	childnumList.add(rs.getInt("childnum"));
	    	AdultnumList.add(rs.getInt("adultnum"));
	    	toodlernumList.add(rs.getInt("toodlernum"));
	    	TripList.add(rs.getString("tripname"));
	    	paid.add(rs.getInt("amount_paid"));
	    }
	    price1 = new Child(tripPrice); // inherited from Person class
	    activities_discount = new BackSchoolDiscount(price1);
	    price2 = new Adult(tripPrice);
	    price3 = new Toodler(tripPrice);
	    price = new int[childnumList.size()]; // refer as the number of query not the size
        Description = new String[childnumList.size()+1];
        Description[0] = "*** Hello , here are the price and money that you pay for trip *** \n ";
for(int q = 0 ; q < childnumList.size(); q++) {
	    int ChildPrice = 0 ; 
	    int AdultPrice = 0 ; 
	    int ToodlerPrice = 0;
	    Description[q] = "\n  \n "+Integer.toString(q+1) +". Trip Name : " + TripList.get(q) + "\n";
	    for(int i = 0 ; i < childnumList.get(q) ; i++) {
	    price[q] += activities_discount.getPrice();
	    ChildPrice += activities_discount.getPrice();
	    }
	    Description[q] += "Children x "+childnumList.get(q)+" 's Price (After 80 % on for Child Policy and 80 % on again BackSchool Discount) : " + ChildPrice + "\n";
	    for(int i = 0 ; i < AdultnumList.get(q) ; i++) {
	    price[q] += price2.getPrice();
	    AdultPrice += price2.getPrice();
	    }
	    Description[q] += "Adult x " + AdultnumList.get(q)+" 's Price (Normal Price) : " + AdultPrice + "\n";
	    for(int i = 0 ; i < toodlernumList.get(q) ; i++) {
	    price[q] += price3.getPrice();
	    ToodlerPrice += price3.getPrice();
	    }
	    Description[q] += "Toodler x "+ toodlernumList.get(q)+ " (Free) : " + ToodlerPrice + "\n";
	    Description[q] += "Total Price :  " + price[q] + "\n";
	    Description[q] += "Paid  Money :  " + paid.get(q) + "\n";
	    Description[q] += "Money that you still to pay for this trip :  " + (price[q]- paid.get(q))  + "\n \n";
        //System.out.println(Description[q]);
}

		}catch(Exception e) {
			//log.info("SQLException while closing file: {}", e3.toString());
		}
		finally {
			try {
				if(rs!=null)
					rs.close();
				if(stmt!=null)
					stmt.close();
				if(connection!=null)
					connection.close();
			}
			catch (SQLException e4) {
				//log.info("SQLException while closing file: {}", e4.toString());
			}
		}
		
	}
	
	/**
	 * 
	 * it calculated the tripprice finally
	 * 
	 * @param tripName is the tripName that we used
	 * @return the tripprice
	 */
	
	
	public int getTripprice(String tripName) {
		int price  = 0 ;
		try {
			connection = getConnection4();
			stmt = connection.prepareStatement("SELECT tourfee from Tour WHERE tourname = ? ");
			stmt.setString(1,tripName);
			rs = stmt.executeQuery();
			while(rs.next()) {		
			   price = rs.getInt("tourfee");
			}
		    rs.close();
		    stmt.close();
		    connection.close();
		    //System.out.println("Here are haha price"+price);
		    return price;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return price;
	}
	
	
	/**
	 * get the coupon finally
	 * @return the coupon number that we used.
	 */
	public int getCoupon() {
	
		int CouponNum = 0;
		try {
		connection = getConnection4();
		stmt = connection.prepareStatement("SELECT coupon FROM client where engname = ? ");
		stmt.setString(1,tourist.getName());
		rs = stmt.executeQuery();
		while(rs.next()) {
			CouponNum = rs.getInt("coupon");			
		}
		
		System.out.println(CouponNum);
		return CouponNum;
	
		}catch(Exception e) {
			
		}
		return CouponNum;
	}
	
	
	/**
	 * 
	 * price of calculated each trip
	 * 
	 * @param tripName is inputted by customer recently
	 * @return the price of each trip 
	 */

	public int PriceCalculateForTrip(String tripName) {
		try {
			//int r = getTripprice("tripB");
			int test = 0 ;
			int tripPrice = getTripprice(tripName);
			int childnum  = 0;
			int adultnum  = 0;
			int toodlernum = 0;
			int Couponnum = getCoupon();
			connection = getConnection4();
			stmt = connection.prepareStatement("SELECT childnum,adultnum,toodlernum,tripname,amount_paid from TourInf where name = ? and tripname = ? order by date DESC");
			stmt.setString(1,tourist.getName());
			stmt.setString(2,tripName);
			rs = stmt.executeQuery();
			while(rs.next()) {
				childnum = rs.getInt("childnum");
				adultnum = rs.getInt("adultnum");
				toodlernum = rs.getInt("toodlernum");				
			}
			
			  price1 = new Child(tripPrice); 
			  activities_discount = new BackSchoolDiscount(price1);
			  price2 = new Adult(tripPrice);
			  //System.out.println("Coupon 's Discount 1: " + price2.getPrice());
			  Activity activity_discount2  = new Coupon(price2);
			  //System.out.println("Coupon 's Discount 2 : " + activity_discount2.getPrice());
			  price3 = new Toodler(tripPrice);
		    int price =  0 ; 
		    for(int i = 0 ; i < childnum ; i++)
		    price += activities_discount.getPrice();
		    
		    int counter = 0;
		    if(adultnum - Couponnum >= 0) {
		    for(; counter < Couponnum; counter++) {
		    	price += price2.getPrice();  
		    }
		    
		    for(int i = 0 ; i < (adultnum - Couponnum) ; i++)
		    price += activity_discount2.getPrice();  
		    }else {
		    	  for(int i = 0 ; i < adultnum ; i++)
		  		  price += price2.getPrice();  
		    	
		    }
		    
		    price += price3.getPrice();    
		    rs.close();
		    stmt.close();
		    connection.close();
		    System.out.println("Hello ,  here are your tested price : "+price);
		    return price;
		    
		    
		}catch(Exception e){
			System.out.println(e.getMessage());
			return 10000;
		}
	  
	}
	
	/**
	 * 
	 * get the price of customer
	 * 
	 * @param i = index of price List
	 * @return the price according to index
	 */
	
	public int getPriceOfCustomer(int i) {
		// for final payment use
		return price[i];   
	}
	
	
	/**
	 * 
	 * get the description of customer
	 * 
	 * @param i  = index of Description 
	 * @return description with the index
	 */
	public String getDescription(int i) {
		// for shown the out balance to customer
		return Description[i]; 
	}
	
	
	/**
	 * 
	 * get the size of customers
	 * 
	 * 
	 * @return the size of customer list
	 */
	public int getSizeOfCustomer() {
		// for given size of customer to reference
		return childnumList.size();
	}
	
	
	/**
	 * 
	 * get connection function
	 * 
	 * @return
	 * @throws URISyntaxException exception of url
	 * @throws SQLException sql exception 
	 */
	private Connection getConnection4() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI("postgres://itniqlvmjieeeq:1bf7632721043bf02f0f498974ece3939ed3e947b9c96435ca0ced9d06593f33@ec2-107-22-235-167.compute-1.amazonaws.com:5432/daj5i9364g5dfv");
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
		//log.info("Username: {} Password: {}", username, password);
		//log.info ("dbUrl: {}", dbUrl);
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}
	
	
	
	
}
