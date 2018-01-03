package com.example.bot.spring;

import java.sql.*;

/**
 * a query "algorithm" that search the database at second time and return the corrsonding response
 * 
 * @author Louis, spcheungaa
 */
public class FAQQueryWithDoubleSearch implements QueryBehaviour {
	private PreparedStatement stmt;
	private Connection connection;
	private ResultSet rs;
	
	/**
	 * query the database twice and return the correponding result
	 * remarks: the first search is called in the FAQ class via the FAQQueryWithDirectSearch
	 * 
	 * @param stmt			contains the PreparedStatment instance to be used in query
	 * @param connection		contains the Connection instance to be used in query
	 * @param text			contains the keyword that to be query
	 * @param result			contains the result searched from the first query
	 * @return				return the second searched result to the first searched result
	 * @throws SQLException	throw the exception if there is any SQL problems
	 */
	public String query (PreparedStatement stmt, Connection connection, String text, String result) throws SQLException
	{
		String keyword, response;

		String exactText = text.substring(7).toLowerCase();

		//Second batch of search: variable which store the current information
		switch(exactText)
		{
		//Modifiable FAQ through variables
		case "apply":
			stmt = connection.prepareStatement("SELECT keyword, info FROM companyinfo WHERE keyword like concat ('%', ?, '%')");
			stmt.setString(1, "<application method>");
			rs = stmt.executeQuery();
			rs.next();
			result = result.replaceFirst("<application method>", rs.getString(2));

			stmt = connection.prepareStatement("SELECT keyword, info FROM companyinfo WHERE keyword like concat ('%', ?, '%')");
			stmt.setString(1, "<payment method>");
			rs = stmt.executeQuery();
			rs.next();
			result = result.replaceFirst("<payment method>", rs.getString(2));

			stmt = connection.prepareStatement("SELECT keyword, info FROM companyinfo WHERE keyword like concat ('%', ?, '%')");
			stmt.setString(1, "<evidence>");
			rs = stmt.executeQuery();
			rs.next();
			result = result.replaceFirst("<evidence>", rs.getString(2));

			break;

		case "insurance":
			stmt = connection.prepareStatement("SELECT keyword, info FROM companyinfo WHERE keyword like concat ('%', ?, '%')");
			stmt.setString(1, "<insurance>");
			rs = stmt.executeQuery();
			rs.next();
			result = result.replaceFirst("<insurance>", rs.getString(2));

			break;

		case "tour cancelled":
			stmt = connection.prepareStatement("SELECT keyword, info FROM companyinfo WHERE keyword like concat ('%', ?, '%')");
			stmt.setString(1, "<dayTourCancelledInformed>");
			rs = stmt.executeQuery();
			rs.next();
			result = result.replaceFirst("<dayTourCancelledInformed>", rs.getString(2));

			break;

		case "additional charge":
			stmt = connection.prepareStatement("SELECT keyword, info FROM companyinfo WHERE keyword like concat ('%', ?, '%')");
			stmt.setString(1, "<additionalServiceCharge>");
			rs = stmt.executeQuery();
			rs.next();
			result = result.replaceFirst("<additionalServiceCharge>", rs.getString(2));

			break;

		case "hotel bed":
			stmt = connection.prepareStatement("SELECT keyword, info FROM companyinfo WHERE keyword like concat ('%', ?, '%')");
			stmt.setString(1, "<additionalBedCharge>");
			rs = stmt.executeQuery();
			rs.next();
			result = result.replaceFirst("<additionalBedCharge>", rs.getString(2));

			break;

		case "tour fee":
			stmt = connection.prepareStatement("SELECT keyword, info FROM companyinfo WHERE keyword like concat ('%', ?, '%')");
			stmt.setString(1, "<age3fee>");
			rs = stmt.executeQuery();
			rs.next();
			result = result.replaceFirst("<age3fee>", rs.getString(2));

			stmt = connection.prepareStatement("SELECT keyword, info FROM companyinfo WHERE keyword like concat ('%', ?, '%')");
			stmt.setString(1, "<age4to11fee>");
			rs = stmt.executeQuery();
			rs.next();
			result = result.replaceFirst("<age4to11fee>", rs.getString(2));

			break;

		case "deadline":
			stmt = connection.prepareStatement("SELECT keyword, info FROM companyinfo WHERE keyword like concat ('%', ?, '%')");
			stmt.setString(1, "<deadlineOfTrip>");
			rs = stmt.executeQuery();
			rs.next();
			result = result.replaceFirst("<deadlineOfTrip>", rs.getString(2));

			break;

		case "tour size":
			stmt = connection.prepareStatement("SELECT keyword, info FROM companyinfo WHERE keyword like concat ('%', ?, '%')");
			stmt.setString(1, "<sizeOfTour>");
			rs = stmt.executeQuery();
			rs.next();
			result = result.replaceFirst("<sizeOfTour>", rs.getString(2));

			break;
		}
		return result;
	}
}
