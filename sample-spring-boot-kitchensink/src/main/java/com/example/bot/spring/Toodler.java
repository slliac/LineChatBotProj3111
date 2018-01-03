
package com.example.bot.spring;

/**
 * 
 * 
 * Write toodler class inherited from Perosn
 * @author gordon
 *
 */
public class Toodler implements Person{
	public int price = 0 ;
	public void discount() {
		
	}
	/**
	 * 
	 * 
	 * 
	 * @param priceOfTrip is the price of trip
	 */
	public Toodler(int priceOfTrip) {
		price  = priceOfTrip;
	}
	/**
	 * 
	 * 
	 * 
	 * @return price of toodler is free
	 */
	public int getPrice() {return 0;}
}
