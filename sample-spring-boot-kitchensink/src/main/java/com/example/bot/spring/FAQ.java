package com.example.bot.spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** 
 * FAQ Enquiry: 
 * A class to allow the Line Chatbot to search by keywords and show responses retrieved from the "faq" database.
 * 
 * Thread-safe Singleton Design Pattern + modified Strategy Design Pattern
 * - Singleton pattern is used as only 1 FAQ object can be instantiated
 * - Strategy pattern is used as 2 types of FAQ querying methods are implemented:
 * 	- 1. Direct Search: searching the database once only
 * 	- 2. Double Search: the first search of the database retrieves the response template from the database "faq",
 * 						the second search of the database retrieves the actual values stored from the other database "companyinfo"
 * 	- 3. Not Available Search: search the database and return same batch of "not support" response
 * ***Remark: setting the query behaviour (qBehaviour) inside all the newly added new method!!!***
 * 
 * @author Louis, spcheungaa
*/
public class FAQ {
	
	// The only KEYWORDS that can be searched by the user
	private static final String[] DIRECT_KEYWORDS = {
			"assembly point", "transportation", "contact", 
			"visa", "swimming suit", "vegetarian", "late departure date"
	};
	private static final String[] DOUBLE_KEYWORDS = {
			"apply", "tour cancelled", "additional charge", 
			"insurance", "hotel bed", "tour fee", 

			//extraFAQ
			"deadline", "tour size"
	};
	private static final String[] NOT_AVAILABLE_KEYWORDS = {
			//these Keywords searched will return the same result, i.e. not available:
			//air ticket and trip||waiting list||language support
			"air ticket and trip", "waiting list", "language support"
	};
	
	//unique instance
	private static FAQ faq = new FAQ();
	
	//instance variable
	private static PreparedStatement stmt;
	private static Connection connection;
	private QueryBehaviour qBehaviour;
	private String result = null;
	
	/**
	 * private constructor
	 */
	private FAQ() {;}
	
	/**
	 * 
	 * @param p	get the PreparedStatement instance from the caller
	 * @param c	get the Connection instance from the caller
	 * @return faq	unique FAQ object
	 */
	public static FAQ getFAQ (PreparedStatement p, Connection c)
	{
		stmt = p;
		connection = c;
		return faq;
	}
	
	/**
	 * get the Keywords set inside the FAQ class
	 * 
	 * @param notAvailable
	 * 		-	true: 	ONLY search the NOT_AVAILABLE_KEYWORDS keywords
	 * 		-	false: 	DIRECT_KEYWORDS / DOUBLE_KEYWORDS
	 * @param direct
	 * 		-	true: 	ONLY search the DIRECT_KEYWORDS keywords (pre-condition: notAvailabe == false)
	 * 		- 	false: 	ONLY search the DOUBLE_KEYWORDS keywords (pre-condition: notAvailabe == false)
	 * @return temp		a String of the selected KEYWORDS which are separated with a space
	 */
	public String getKeywords(boolean notAvailable, boolean direct)
	{
		String[] tempKeywords = null;
		String temp = "";
		int i = 0;
		if (notAvailable)
			tempKeywords = NOT_AVAILABLE_KEYWORDS;
		else if (direct)
			tempKeywords = DIRECT_KEYWORDS;
		else
			tempKeywords = DOUBLE_KEYWORDS;
			
		for (String k: tempKeywords)
		{
			temp += k;
			++i;
			if (i < tempKeywords.length)
				temp += " ";
		}
		return temp;
	}
	
	/**
	 * set the query behaviour of a FAQ instance
	 * 
	 * @param qb		the parameter contains a QueryBehaviour instance to be assigned into a FAQ instance
	 */
	public void setQuery(QueryBehaviour qb)
	{
		qBehaviour = qb;
	}
	
	/**
	 * a method that search the database through the keyword "text" provided
	 * 
	 * @param text	keyword that to be searched
	 * @return		return the searched result if it was found in the database
	 * @throws SQLException	throw the exception if SQL query has problem
	 */
	public String enquiry(String text) throws SQLException
	{
		boolean direct = false;
		boolean found = false;
		
		for (String k: DIRECT_KEYWORDS)
		{
			if (text.toLowerCase().contains(k))
			{
				qBehaviour = new FAQQueryWithDirectSearch();
				result = qBehaviour.query(stmt, connection, text, result);
				direct = true;
				found = true;
				break;
			}
		}
		
		if (!direct)
			for (String k: DOUBLE_KEYWORDS)
				if (text.toLowerCase().contains(k))
				{
					qBehaviour = new FAQQueryWithDirectSearch();
					result = qBehaviour.query(stmt, connection, text, result);
					qBehaviour = new FAQQueryWithDoubleSearch();
					result = qBehaviour.query(stmt, connection, text, result);
					found = true;
					break;
				}
		
		for (String k: NOT_AVAILABLE_KEYWORDS)
		{
			if (text.toLowerCase().contains(k))
			{
				qBehaviour = new FAQQueryNotAvailable();
				result = qBehaviour.query(stmt, connection, text, result);
				found = true;
				break;
			}
		}

		if (found)
			return result;
		else
			return "Sorry, we don't have answer for this.";
	}
}
