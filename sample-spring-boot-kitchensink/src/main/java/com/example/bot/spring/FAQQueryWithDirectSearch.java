package com.example.bot.spring;

import java.sql.*;
import java.sql.SQLException;

/**
 * a query "algorithm" that search the database once only and return the corrsonding response
 * 
 * @author Louis, spcheungaa
 */
public class FAQQueryWithDirectSearch implements QueryBehaviour {
	private PreparedStatement stmt;
	private Connection connection;
	private ResultSet rs;
	
	/**
	 * query the database once only and return the correponding result
	 * 
	 * @param stmt			contains the PreparedStatment instance to be used in query
	 * @param connection		contains the Connection instance to be used in query
	 * @param text			contains the keyword that to be query
	 * @param dummyResult	contains the a dummy parameter (will be used in the FAQQueryWithDoubleSearch)
	 * @return				return the searched result
	 * @throws SQLException	throw the exception if there is any SQL problems
	 */
	public String query (PreparedStatement stmt, Connection connection, String text, String dummyResult) throws SQLException
	{
		String result = null, keyword, response;
		
		String exactText = text.substring(7).toLowerCase();
				
		//First batch of serach: template of answering
		stmt = connection.prepareStatement("SELECT keyword, response FROM faq WHERE keyword like concat ('%', ?, '%')");
		stmt.setString(1, exactText);
		rs = stmt.executeQuery();

		while (result == null && rs.next()) {
			keyword  = rs.getString(1);
			response = rs.getString(2); 
			if (exactText.toLowerCase().equals(keyword.toLowerCase())) {
				result = response;
			}
		}

		return result;
	}
}
