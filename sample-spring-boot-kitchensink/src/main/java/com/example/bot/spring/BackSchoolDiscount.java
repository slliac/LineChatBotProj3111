package com.example.bot.spring;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




/**
 * 
 * BackschoolDiscount class extends from Activity interface
 * 
 * @author Gordon
 *
 */
public class BackSchoolDiscount implements Activity{
	
	public Person p;
	
	
	/**
	 * this function is wraps the subject of person into Activity and then used 
	 * 
	 * @param p is a person objects for child 
	 */
	public BackSchoolDiscount(Person p) {
		this.p = p ;
	}
	
	
	/**
	 * this function would wrapped the price of backschool discount and replaced the original price of child.
	 * 
	 * @return price of child that every childs would have backschool discount
	 */
	public int getPrice() { // 20 % off for BackSchool Discount
		return  p.getPrice()/10*8;
	}
	
}
