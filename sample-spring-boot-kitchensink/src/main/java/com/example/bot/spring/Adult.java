package com.example.bot.spring;

/**
 * 
 * Adule class
 * 
 * A class inherited from Person
 * 
 * which would return the class of Adult
 * 
 * 
 * 
 * @author Gordon
 *
 */



public class Adult implements Person{
	/**
	 * 
	 * price of trip is the class from the default adult.
	 * 
	 * @param priceOfTrip is price of trip
	 */
	public Adult(int priceOfTrip) {
		price  = priceOfTrip;
	}
	public int price = 0 ;
	public void discount() {
	}
	
	/**
	 * 
	 * 
	 * @return price which is the price for adult.
	 */
	public int getPrice() {return price;}
}
