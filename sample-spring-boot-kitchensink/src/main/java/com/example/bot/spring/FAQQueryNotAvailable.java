package com.example.bot.spring;

import java.sql.*;

/**
 * a query "algorithm" that response the "service not available" result
 * 
 * @author Louis, spcheungaa
 */
public class FAQQueryNotAvailable implements QueryBehaviour {
	private PreparedStatement stmt;
	private Connection connection;
	private ResultSet rs;

	/**
	 * query the database of the searching batch: air ticket and trip||waiting list||language support
	 * and return the correponding result
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

		String exactText = "not available";
		
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
