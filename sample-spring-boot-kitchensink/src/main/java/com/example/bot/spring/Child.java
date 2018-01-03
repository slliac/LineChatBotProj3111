package com.example.bot.spring;



/**
 * 
 * 
 * Class Child extends from Person class.
 * 
 * @author Gordon
 *
 */
public class Child implements Person{
	
	/**
	 * Child Constructor
	 * @param priceOfTrip is the price of the trip 
	 */
	public Child(int priceOfTrip) {
		price  = priceOfTrip;
	}

	public int price = 0 ;
	public void discount() {	
	}
	
/**
 * 
 * Child 's getprice function 
 * @return price of the child
 */
	public int getPrice() {
	   return price/10*8;
	}
	
	

}
