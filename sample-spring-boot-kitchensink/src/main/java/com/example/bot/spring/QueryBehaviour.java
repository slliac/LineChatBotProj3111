package com.example.bot.spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * an interface of all the various QueryBehaviour
 * e.g. FAQQueryNotAvailable, FAQQueryWithDirectSearch, FAQQueryWithDoubleSearch
 * 
 * @author Louis, spcheungaa
 *
 */
public interface QueryBehaviour {
	/**
	 * an abstract method query which ensure all the implemented classes have query method
	 * 
	 * @param stmt			contains the PreparedStatment instance to be used in query (in the implemented classes) 
	 * @param connection	contains the Connection instance to be used in query (in the implemented classes) 
	 * @param text			contains the keyword that to be query (in the implemented classes) 
	 * @param result		contains the result searched from the corresponding query (in the implemented classes) 
	 * @return				return the correponding searched result (in the implemented classes) 
	 * @throws SQLException	throw the exception if there is any SQL problems (in the implemented classes) 
	 */
	public abstract String query(PreparedStatement stmt, Connection connection, String text, String result) throws SQLException;
}
