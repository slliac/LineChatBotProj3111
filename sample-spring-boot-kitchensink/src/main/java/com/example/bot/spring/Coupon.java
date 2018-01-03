package com.example.bot.spring;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


class Coupon implements Activity{
	
public Person p;
	
/**
 * 
 * Coupon would used when person had wrapped in 
 * @param p had wrapped inside the coupon class
 */
	public Coupon(Person p) {
		this.p = p ;
	}
	
	/**
	 * 
	 * The price of coupon would changed and transmitted through getprice
	 * 
	 * @return the price of Person who got coupon
	 */
	
	public int getPrice() { // 50 % off for Coupon
		return  p.getPrice()/10*5;
	}
	
	
}