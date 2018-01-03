package com.example.bot.spring;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
//Billy imported two date for Feature3.1 
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	
	// tourist made in here
	public Tourist tmp = null;
	int eei = 0 ;
	// test case 2 : D.Chan
	//public Tourist tmp = new Tourist("D.Chan",0);
	
	/**
	 * 
	 * the function that would show the location about the trip.
	 * @param text is the trip name.
	 * @return location list
	 * @throws Exception when their have error
	 */
	public ArrayList<String> showLocation(String text) throws Exception {
		try {

			ArrayList<String> locationList = new ArrayList<String>();
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT location, latitude, longitude FROM tour where tourname = ?");
			stmt.setString(1, text);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				locationList.add(rs.getString(1));
				locationList.add(rs.getString(2));
				locationList.add(rs.getString(3));
			}
			
			return locationList;
			
		}catch(SQLException e) {
			System.out.println(e.toString());
		}
		return null;
	}
	//Feature 9.1
	/**
	 * Part of Feature 9.1: A function that get line ids of our clients from our database
	 * @return a list containing the string of the line id of all our clients
	 * @throws Exception
	 */
		public List<String> getClientList() throws Exception {
			try {
				List<String> clientList = new ArrayList<String>();
				
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("Select lid from client");
				ResultSet rs = null;
				rs = stmt.executeQuery();
				
				while(rs.next()) {
					clientList.add(rs.getString("lid"));
				}
				
				return clientList;
				
			}catch(Exception e) {
		}
			return null;
		}	

		/**
		 * A part of Feature 9.1: A function that composes the promotion message based on the result of database (if there is last sales tour, promote it. Otherwise, promote the cheapest tour to the clients)
		 * @return The Promotion Message that is supposed to be sent to our clients
		 */
		public String getPromotionMessage() {
			//promote tour with application deadline approaching with discount
			String result = null;
			
			try
			{
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT tourname, tourfee FROM tour WHERE application_deadline BETWEEN CURRENT_DATE AND (CURRENT_DATE + 5)");
				ResultSet rs = stmt.executeQuery();

				int minPrice = Integer.MAX_VALUE;

				while (result == null && rs.next()) {
				//get the cheapest tour among those result
					if (minPrice >rs.getInt(2)) {
						result = rs.getString(1);
						minPrice = rs.getInt(2);
						}
				 }
				 
				//if there isn't application deadline discount tour, simply suggest the cheapest tour to the user
				 if(result != null)
				 {
					 result += " is a tour approaching deadline! we will offer you a last order discount.";
				 }
				 else{
				 	stmt = connection.prepareStatement("SELECT tourname, tourfee FROM tour WHERE tourfee = (SELECT MIN(tourfee) from tour) LIMIT 1");
				 	rs = stmt.executeQuery();
				 	
				 	while (result == null && rs.next()) {
				 		result = rs.getString(1);
				 		minPrice = rs.getInt(2);
				 	}
				 	result += " is the cheapest tour. Come and join!";
				 }
				
			}catch(Exception e)
			{
				return result = "Please book tours by now!";
			}
			 return result;
		}
		
		/**
		 * Feautre 5: a function that used to update the client's name in the database the first time he books our tour
		 * @param userId is the line id of that client
		 * @param name is the name that we are going to update to the field'engname'
		 * @throws Exception
		 */
		
		public void updateClientName(String userId, String name) throws Exception {
			try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("UPDATE Client SET engname = ? WHERE lid = ?");
				stmt.setString(1, name);
				stmt.setString(2, userId);
				stmt.executeUpdate();
				connection.close();
				
			}catch(SQLException e) {
				System.out.println(e.toString());
			}
			return;
		}
	
		/**
		 * Feauture 5: get the conversation status of the client chatting with our bot. The status indicates the stage of the conversation between client and bot. Different values represent different expected response.
		 * @param userId is the line id of the client talking to our linechatbot
		 * @return the conversation status of the client from databse
		 * @throws Exception
		 */
		public int getConversationStatus(String userId) throws Exception {
			try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT converStatus FROM Client WHERE lid = ?");
				stmt.setString(1, userId);
				ResultSet rs = stmt.executeQuery();
				connection.close();
				
				if(rs.next()) {
					return rs.getInt(1);
				}
				
			}catch(SQLException e) {
				System.out.println(e.toString());
			}
			return 0;
		}
	
		/**
		 * Feature 5: a function that used to change the conversation status in our database
		 * @param userId is the line id of the the client
		 * @param status is the conversation status that should be updated to our database
		 * @throws Exception
		 */
		public void setConversationStatus(String userId, Integer status) throws Exception {
			try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("UPDATE Client SET converStatus = ? WHERE lid = ?");
				stmt.setInt(1, status);
				stmt.setString(2, userId);
				stmt.executeUpdate();
				connection.close();
				
			}catch(SQLException e) {
				System.out.println(e.toString());
			}
			return;
		}
	
		/**
		 * Feature 5: A function that get the name of clients using their line id
		 * @param userId is the line id of our client
		 * @return the name of the client
		 * @throws Exception
		 */
		public String getClientName(String userId) throws Exception {
			try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT engname from client WHERE lid = ?");
				stmt.setString(1, userId);
				ResultSet rs = stmt.executeQuery();
				connection.close();
				
				if(rs.next())
				{
					return rs.getString(1);
				}
				
			}catch(SQLException e) {
				
			}
			return null;
		}
		
		/**
		 * Feature 5: check if the client registered in our database
		 * @param userId is the string that the line id of the client
		 * @return the boolean if the client have registered in our db before
		 * @throws Exception
		 */
		public boolean checkClient(String userId) throws Exception {
			try {
				Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT * from client WHERE lid = ?");
				stmt.setString(1, userId);
				ResultSet rs = stmt.executeQuery();
				connection.close();
				
				if(rs.next()) {
					return true;
				}		

			}catch(SQLException e) {
				System.out.println(e.toString());
			}
			return false;
		}
		
	/**
	 * Feature 5: create a client entry to our database.
	 * @param userId is the line id of client
	 * @throws Exception
	 */
	public void addClient(String userId) throws Exception {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO client (lid, converStatus) VALUES (?, 0);");
			stmt.setString(1, userId);
			stmt.executeUpdate();
			connection.close();
			
		}catch(SQLException e) {
			System.out.println(e.toString());
		}
		return;
	}
	
	/** Mediator Design Pattern is used */
	/**
	* This method acts as a mediator to determine what method should be called to handle the request. 
	* @param text This is a parameter to take the complete sentence that client inputted. 
	* @return String This returns of the reply message.
	*/
//	Super Method to identify which of the following functions to be called.
	public String switchFunctions(String text,String name) throws Exception{
		
		try {
			String name1 = null;
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT name FROM Tourinf where id = ? order by name ASC;");
			stmt.setString(1,name);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
    			name1 = rs.getString("name");
    		    }
			//System.out.println("the name is " + name1);
			// udpate of tourist
			if(tmp == null)
			tmp = new Tourist(name1,0);
			else {
			tmp = new Tourist(name1,0);
			}
		}catch(Exception e) {
			
		}
		
		
		String output="";
		
		
		
		
		
		if( text.toLowerCase().contains("keyword")) {
			output = searchByKeywords(text);
		}else if( text.toLowerCase().contains("details of ") ){
			output = displayTour(text);
		}else if( text.toLowerCase().contains("search") ){
			output = search(text,"");
		}else if( text.toLowerCase().contains("survey")) {
			output = rating(text);
		}else if ( text.toLowerCase().contains("recommend") ){
			output = recommendation(text);
		}
		//else if (text.toLowerCase().contains("hi")||
		//		text.toLowerCase().contains("hey")||
	    //			text.toLowerCase().contains("hello")){
		//	output = dynamicGreeting();
		//}
	else if ( text.toLowerCase().contains("thank you"))
		{
			output = "Thank you.";
		}else {
			output = "Sorry, we don't have answer for this.";
		}
		return output;
	}
	

	/**
	 * Feature 1: Dynamic Greeting
	 * a dynamic greeting will be responsed if the user has booked a tour before
	 * 
	 * @param userId		the LINE user ID of the user who entered the keywords (greeting to the LINE chatbot)
	 * @return			a dynamic greeting will be returned if the user has booked a tour before, otherwise the normal one will be returned
	 */
	public String dynamicGreeting(String userId) {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String name = null;
		Date date = null;
		String location = null;
		try {
		connection = getConnection();
		stmt = connection.prepareStatement("SELECT engname FROM client WHERE lid = ?");
		stmt.setString(1, userId);
		rs = stmt.executeQuery();

		//unique lineId for a username
		if (rs.next())
			name = rs.getString(1);
		else if (name == null)
			return "Welcome. What can I help you?";
		
		stmt = connection.prepareStatement("SELECT max(date), location FROM tourinf WHERE id = ? group by id, location");
		stmt.setString(1, userId);
		rs = stmt.executeQuery();
		
		if (rs.next())
		{
			//date = new Date(rs.getDate(1));
			location = rs.getString(2);
		}
		
		stmt.close();
		connection.close();
		}catch(Exception e) {
			
			
		}
		return "How the last " + location + " trip was, " + name + "?  It is nice to see you again!";
		
//		while (rs.next()) {
//			Date dateTemp = new(rs.getDate(1));
//			if (date.compareTo(dateTemp) )
//		date  = rs.getString(1);
//		location = rs.getString(2); 
//		if (exactText.toLowerCase().equals(keyword.toLowerCase())) {
//			result = response;
//		}
	}
	
	/** Mediator Design Pattern is used */
	/**
	* this function create a discount record in the Database. 
	* @param couponID A coupon ID is required to create a discount record in the database. 
	* @param amount The number of discounts to give out. 
	* @param percentOff The discount to be offered (percentOff should be 50 if 50% discount is offered). 
	* @return boolean Return True if the discount record can be created successfully, False if not.
	*/
	public boolean createDiscount(String couponID, int amount, int percentOff) throws Exception {
		if (couponID.isEmpty() || amount<=0 || percentOff <= 0)
			return false;
		boolean flag = false;
		try {
			//CHECK IF EXISTING
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT Remaining FROM Coupon WHERE CouponID = ?");
			stmt.setString(1, couponID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return false;
			}
			//INSERT
			int rows=0;
//			Connection connection = getConnection();
			stmt = connection.prepareStatement("INSERT INTO Coupon (CouponID, Remaining, discount) VALUES (?, ?, ?);");
			stmt.setString(1, couponID);
			stmt.setInt(2, amount);
			stmt.setInt(3, percentOff);
			rows = stmt.executeUpdate();
			if (rows==0)
				flag = false;
			else
				flag = true;	
			stmt.close();
			connection.close();
		}catch(SQLException e) {
//			System.out.println(e.toString());
		}
		return flag;
	}
	public boolean deleteDiscount(String couponID) {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM Coupon WHERE CouponID = ?");
			stmt.setString(1, couponID);
			int rows=0;
			rows = stmt.executeUpdate();
			if (rows>0)
				return true;
			else
				return false;
		}catch(Exception e) {}
		return false;
	}
	
	/**
	* This function reduces the number of the coupon in the Database, then give a discount COUPON to a client by adding the coupon String in the client record. 
	* @param clientID A client ID is required to add the discount String in the client record. 
	* @param couponID A coupon ID is required to retrieve corresponding coupon in the database. 
	* @return boolean Return True if the discount record can be transferred successfully, False if not.
	*/
	public boolean getDiscount(String clientID, String couponID) throws Exception {
		if (clientID.isEmpty()||couponID.isEmpty())
			return false;
		boolean flag=false;
		try{
			Connection connection = null;
			PreparedStatement stmt = null;
			connection = getConnection();
			ResultSet rs = null;
			//Check remaining number of coupon
			stmt = connection.prepareStatement("SELECT Remaining FROM Coupon WHERE CouponID=?");
			stmt.setString(1, couponID);
			rs = stmt.executeQuery();
			int num=-1;
			while(rs.next()) {
				num = Integer.parseInt(rs.getString(1));
			}
			if (num>0) {
				//Check if the client has already owned the coupon
				stmt = connection.prepareStatement("SELECT Coupon FROM client WHERE CID=?");
				stmt.setString(1, clientID);
				rs = stmt.executeQuery();
				String ownedCoupon="";
				while(rs.next()) {
					ownedCoupon = rs.getString(1);
				}
				if (ownedCoupon.contains(couponID)==false) {
					int rows=0;
					//Retrieving info from DB
					stmt = connection.prepareStatement("UPDATE client SET Coupon=CONCAT(Coupon,?) WHERE CID=?");
					stmt.setString(1, couponID);
					stmt.setString(2, clientID);
					rows = stmt.executeUpdate();
					//Returning results
					if (rows==0) {
						flag = false;
					}else {
						stmt = connection.prepareStatement("UPDATE Coupon SET Remaining = Remaining - 1 WHERE CouponID=?");
						stmt.setString(1, couponID);
						rows = stmt.executeUpdate();
						if (rows == 0)
							flag=false;
						flag=true;
					}
				}
			}
			rs.close();
			stmt.close();
			connection.close();
		}catch(Exception e) {
//			log.info("Exception: ", e.toString());
//			output = e.toString();
		} finally { }
		return flag;
	}
	
	/**
	* Feature 0
	* this function displays the details of a tour, including Tour Fee, Date, Location, Deadline of application. 
	* @param text This is a parameter to take the complete sentence that client inputted. 
	* @return String This returns of the reply message.
	*/
	public String displayTour(String text) throws Exception {
		String wording="details of ";
		String output="";
		if (text.toLowerCase().contains(wording)==false){
			return "Sorry but your input format was invalid~";
		}
		try{
			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet rs = null; 
			connection = getConnection();
			//Get the keyword
			String target="";
			if (text.toLowerCase().contains(wording)) {
				int startInt = text.toLowerCase().indexOf(wording)+wording.length();
//				int endInt = text.toLowerCase().indexOf(" ", startInt);
//				if (endInt==-1) {
					target = text.substring(startInt);
//				}else {
//					target = text.substring(startInt,endInt);
//				}
			}
			if ( "?".equals( target.substring(target.length()-1,target.length())) || ",".equals( target.substring(target.length()-1,target.length())) || "!".equals( target.substring(target.length()-1,target.length())) )
				target = target.substring(0,target.length()-1);
			//Retrieving info from DB
			stmt = connection.prepareStatement("SELECT tourfee,date_start,date_end,location,application_deadline FROM tour WHERE tourname = ? ");
			stmt.setString(1, target);
			rs = stmt.executeQuery();
			//Returning results
			while(rs.next()) {
				output += "Details of tour "+target+": \n";
				output += "Tour Fee: " + rs.getString(1)+"\n";
				output += "Date: \t" + rs.getString(2) + " - " +rs.getString(3)+"\n";
				output += "Location: " + rs.getString(4)+"\n";
				output += "Application Deadline: " + rs.getString(5)+"\n"+"\n";
				output += "Book it now by typing in: \"book:[tourname]\"! " ;
			}
			if (output.equals("")) {
				output+="Oops, no related result found. \n\n";
				output+=recommendation(text);
			}
			rs.close();
			stmt.close();
			connection.close();
		}catch(Exception e) {
//			log.info("Exception: ", e.toString());
//			output = e.toString();
			return "Sorry but your input format was invalid~";
		} finally { }
		if (output.equals("")) {
			output+="Sorry but your input format was invalid~";
		}
		return output;
	}
	
	/**
	* Feature 9.2 
	* this function recommend the name of a trip that has the highest rating. 
	* @param text This is a parameter to take the complete sentence that client inputted. 
	* @return String This returns of the reply message.
	*/
	public String recommendation(String text) throws Exception {
		String output="";
		ArrayList<String> tripList = new ArrayList<String>();
		Connection connection = getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null; 
		stmt = connection.prepareStatement("SELECT trip, AVG(rate) AS avg_rate FROM survey GROUP BY trip ORDER BY trip");
		rs = stmt.executeQuery();
		while(rs.next()) {
			tripList.add( rs.getString(1) );
		}
		output = "Would you like to try "+tripList.get(0)+"? It has the highest rating from other tourists! ";
		return output;
	}
//	FEATURE 9.3
	public String rating(String text) throws Exception {
		try {
			String[] wd = text.split(":");
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO survey (trip, rate) VALUES (?, ?);");
			stmt.setString(1, wd[1]);
			stmt.setInt(2, Integer.parseInt(wd[2]));
			stmt.executeUpdate();
			return "Thank you for your opinion!";	
		}catch(SQLException e) {
			System.out.println(e.toString());
		}
		return "Fail";
	}
	
	
	void updatePrice(String bookingDetails[],String userId,int price) {
		
		try {
			   Connection connection = getConnection();
			   PreparedStatement stmt = connection.prepareStatement("UPDATE TourInf SET tourfee = ? WHERE name = ? and tripname = ? and adultnum = ? and childnum = ? and toodlernum = ? ");
			   stmt.setInt(1,price);
			   stmt.setString(2,userId);
			   stmt.setString(3,bookingDetails[1]);
			   stmt.setInt(4,Integer.parseInt(bookingDetails[3]));
			   stmt.setInt(5,Integer.parseInt(bookingDetails[4]));
			   stmt.setInt(6,Integer.parseInt(bookingDetails[5]));
			   stmt.executeUpdate();
			   stmt.close();
			   connection.close();
		}catch(Exception e) {
			
			
		}
		
	}
	
	
	
//	FEATURE 5
	/**
	 * This function is a part of feature 5 to book a tour with specified user line id and then add a booking entry to our database
	 * @param bookingDetails is a string array that store all the information of the booking(include tourname, clientname, num of adult, num of child, num of todd)
	 * @param userId is the line id of the client that are currently asking out linechatbot to book a tour
	 * @return The status of the booking
	 * @throws Exception
	 */
	String booking(String bookingDetails[],String userId) throws Exception {
		try { //
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("Select location,latitude,longitude,date_start,tid from tour where tourname = ?");
			stmt.setString(1,bookingDetails[1]);	
			ResultSet rs = null;
			rs = stmt.executeQuery();
			rs.next();
			connection.close();
			
			Connection connection2 = getConnection();
			PreparedStatement stmt2 = connection2.prepareStatement("INSERT INTO tourinf (name, adultnum, childnum, toodlernum, tripname, amount_paid,location,latitude,longitude,tid,tourfee, id, date,tripstatus) VALUES(?,?,?,?,?,0,?,?,?,?,1000,?,?,1)");
			stmt2.setString(1, bookingDetails[2]);
			stmt2.setInt(2, Integer.parseInt(bookingDetails[3]));
			stmt2.setInt(3, Integer.parseInt(bookingDetails[4]));
			stmt2.setInt(4, Integer.parseInt(bookingDetails[5]));
			stmt2.setString(5, bookingDetails[1]);
			stmt2.setString(6, rs.getString(1));
			stmt2.setDouble(7, rs.getDouble(2));
			stmt2.setDouble(8, rs.getDouble(3));
			stmt2.setString(9, rs.getString(5));
			stmt2.setString(10, userId);
			stmt2.setDate(11, rs.getDate(4));
			stmt2.executeUpdate();
			Date dateUpdated  = rs.getDate(4);
			connection2.close();
			
//			StatusName statusCaller = new StatusName();
//			statusCaller.notAll();
			Tourist tmp = new Tourist(bookingDetails[2],0); // Assume D.Chan joined the tripB and Trip B cost 1000 HKD for adult,800 for child.child got 80% on against because of Back school discount
			PriceCalculate pc = new PriceCalculate(tmp);
			int price = pc.PriceCalculateForTrip(bookingDetails[1]);
			StatusName status = new StatusName(bookingDetails[1]);  // status tmp update
			//for(int i = 0 ; i < 10; i++)
		    status.notAll(); 		
		    updatePrice(bookingDetails, bookingDetails[2],price);
			return "Tour " + bookingDetails[1] + " has been booked. Please be reminded to pay HKD "+price+" before the trip start.";
			
			
		}catch(SQLException e) {
			System.out.println(e.toString());
			return "Fail to book. Please contact our staff.";
		}
	}
	
	/**
	 * This is a part of feature 5
	 * @param tour is a string that contains the name of the tour that we want to check its existance in our database
	 * @return boolean indicating that whether there exists the tour as stated
	 * @throws Exception
	 */
	public boolean checkTour(String tour) throws Exception {
		try {
			Connection connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM tour WHERE tourname = ?");
			stmt.setString(1, tour);
			ResultSet rs= stmt.executeQuery();
			connection.close();
			
			if(rs.next()) {
				return true;
			}


		}catch(SQLException e) {
			System.out.println(e.toString());
		}
		return false;
	}
	
	/**
	* FEATURE 3
	* Searching shall be done in more accurate way by adding up budget, location and time as attribute. (e.g. keyword search a tour in Shanghai from now to 2018-11-11 for 40000)
	* @param text This is a parameter to take the complete sentence that client inputted. 
	* @return String This returns of the reply message.
	*/
	public String searchByKeywords(String input) throws Exception {
		String output="";
		if ((input.toLowerCase().contains("in ") || input.toLowerCase().contains("for ") || input.toLowerCase().contains("from "))==false){
			return "Sorry but your input format was invalid~";
		}
		try{
			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet rs = null; 
			connection = getConnection();
			String keywords="Keywords: ";
			boolean locationFlag = false, timeFlag = false, budgetFlag = false;
			ArrayList<String> tripList = new ArrayList<String>();
		//LOCATION searching
			ArrayList<String> placeList = new ArrayList<String>();
			if (input.toLowerCase().contains("in ")) {
				int startInt = input.toLowerCase().indexOf("in ")+3;
				int endInt = input.toLowerCase().indexOf(" ", startInt);
				String target="";
				if (endInt==-1) {
					target = input.toLowerCase().substring(startInt);
				}else {
					target = input.toLowerCase().substring(startInt,endInt);
				}	
				keywords += target.toLowerCase()+", ";
				//Get Different Location from DB
				ArrayList<String> Locations = new ArrayList<String>();
				stmt = connection.prepareStatement("SELECT DISTINCT location FROM tour");
				rs = stmt.executeQuery();
				while(rs.next()) {
					Locations.add( rs.getString(1) );
				}
				//Search the keyword and get related tripname
				for (int i=0;i<Locations.size();i++) {
					if (target.toLowerCase().contains(Locations.get(i).toLowerCase())) {
						//Get related tour from DB
						stmt = connection.prepareStatement("SELECT tid FROM tour WHERE location = ? ");
						stmt.setString(1, Locations.get(i));
						rs = stmt.executeQuery();
						while(rs.next()) {
							placeList.add( rs.getString(1) );
						}
						locationFlag = true;
					}
				}
			}
		//TIME searching 
			ArrayList<String> timeList = new ArrayList<String>();
			int fromInt=-1; int toInt=-1;
			fromInt = input.toLowerCase().indexOf("from ");
			toInt = input.toLowerCase().indexOf("to ");
			String fromDate="";
			if (fromInt!=-1) {
				fromInt += 5;
				if (input.substring(fromInt, fromInt+3).equals("now")) {
					fromDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				}else {
					fromDate = input.substring(fromInt);
					if (fromDate.length()>=10) {
						fromDate = fromDate.substring(0,10);
					}
					if (fromDate.contains(" ") || fromDate.length()<10) {
						int tempI = fromDate.indexOf("-")+1;
						if (fromDate.substring(tempI,tempI+2).contains("-")){
							String startFromDate = fromDate.substring(0,tempI);
							String endFromDate = null;
							if (fromDate.length()>=9)
								endFromDate= fromDate.substring(tempI,fromDate.length()-1);
							else 
								endFromDate= fromDate.substring(tempI,fromDate.length());
							fromDate = startFromDate +"0"+ endFromDate;
						}
						tempI = 8;
						if (fromDate.length()>=10) {
							if (fromDate.substring(tempI,tempI+2).contains(" ")){
								String startFromDate = fromDate.substring(0,tempI);
								String endFromDate = fromDate.substring(tempI,fromDate.length()-1);
								fromDate = startFromDate +"0"+ endFromDate;
							}
						}else{
							String startFromDate = fromDate.substring(0,tempI);
							String endFromDate = fromDate.substring(tempI);
							fromDate = startFromDate +"0"+ endFromDate;
						}
					}
					keywords += fromDate+", ";
				}
				if (toInt!=-1) {
					toInt += 3;
					String toDate = input.substring(toInt);
					if (toDate.length()>=10)
						toDate=toDate.substring(0,11);
					if (input.substring(toInt, toInt+3).equals("now")) {
						toDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					}else {
						toDate = input.substring(toInt);
						if (toDate.length()>=10) {
							toDate = toDate.substring(0,10);
						}
						if (toDate.contains(" ") || toDate.length()<10) {
							int tempI = toDate.indexOf("-")+1;
							if (toDate.substring(tempI,tempI+2).contains("-")){
								String starttoDate = toDate.substring(0,tempI);
								String endtoDate = null;
								if (toDate.length()>=9)
									endtoDate= toDate.substring(tempI,toDate.length()-1);
								else 
									endtoDate= toDate.substring(tempI,toDate.length());
								toDate = starttoDate +"0"+ endtoDate;
							}
							tempI = 8;
							if (toDate.length()>=10) {
								if (toDate.substring(tempI,tempI+2).contains(" ")){
									String starttoDate = toDate.substring(0,tempI);
									String endtoDate = toDate.substring(tempI,toDate.length()-1);
									toDate = starttoDate +"0"+ endtoDate;
								}
							}else{
								String starttoDate = toDate.substring(0,tempI);
								String endtoDate = toDate.substring(tempI);
								toDate = starttoDate +"0"+ endtoDate;
							}
						}
						keywords += toDate+", ";
					}
					String tempStmt = "SELECT tid FROM tour WHERE date_start >= date(?) AND date_start <= date(?) ";
					stmt = connection.prepareStatement(tempStmt);
					stmt.setString(1, fromDate.replace("-", "") );
					stmt.setString(2, toDate.replace("-", "") );
				}
				else {
					stmt = connection.prepareStatement("SELECT tid FROM tour WHERE date_start >= date(?) ");
					stmt.setString(1, fromDate.replace("-", "") );
				}
				rs = stmt.executeQuery();
				while(rs.next()) {
					timeList.add( rs.getString(1) );
				}
				timeFlag = true;
			}
		//BUDGET searching
			ArrayList<String> budgetList = new ArrayList<String>();
			if (input.toLowerCase().contains("for ")) {
				int startInt = input.toLowerCase().indexOf("for ")+4;
				int endInt = input.toLowerCase().indexOf(" ", startInt);
				if (endInt==-1) {
					stmt = connection.prepareStatement("SELECT tid FROM tour WHERE tourfee <= "+input.toLowerCase().substring(startInt));
					keywords += input.toLowerCase().substring(startInt)+", ";
				}else {
					stmt = connection.prepareStatement("SELECT tid FROM tour WHERE tourfee <= "+input.toLowerCase().substring(startInt,endInt));
					keywords += input.toLowerCase().substring(startInt,endInt)+", ";
				}
//				stmt.setString( 1, input.toLowerCase().substring(input.toLowerCase().indexOf("for")+4) );
				rs = stmt.executeQuery();
				while(rs.next()) {
					budgetList.add( rs.getString(1) );
				}
				budgetFlag = true;
			}
			//Generate result from the three types of keyword searching
			if (locationFlag) {
				if (timeFlag) {
					if(budgetFlag){timeList.retainAll(budgetList);placeList.retainAll(timeList);}
					else{placeList.retainAll(timeList);}
					tripList = placeList;
				}else {
					if(budgetFlag){placeList.retainAll(budgetList);}
					tripList = placeList;
				}
			}else {
				if (timeFlag) {
					if(budgetFlag){timeList.retainAll(budgetList);}
					tripList = timeList;
				}else {
					if(budgetFlag){tripList = budgetList;}
				}
			}
			ArrayList<String> outputList = new ArrayList<String>();
			for (int i=0;i<tripList.size();i++) {
				stmt = connection.prepareStatement("SELECT tourname FROM tour WHERE tid = ?");
				stmt.setString(1, tripList.get(i) );
				rs = stmt.executeQuery();
				while(rs.next()) {
					outputList.add( rs.getString(1) );
				}
			}
			output = keywords.substring(0, keywords.length()-2) +"\n\n";
			if (outputList.isEmpty()) {
				output+="Oops, no related result found. \n\n";
				output+=recommendation(input);
			}else if(outputList.size()==1){
				output+="This trip maybe good for you: \n";
				output += "1. " + outputList.get(0) + "\n";
			}else{
				output+="These trips maybe good for you: \n";
				for (int i=0;i<outputList.size();i++) {
					output += Integer.toString(i+1) +". " + outputList.get(i) + "\n";
				}
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
//			log.info("Exception: ", e.toString());
//			output = e.toString();
			return "Sorry but your input format was invalid~";
		} finally { 
		}
		return output;
	}

    
	/**
	 * search the status in difference case
	 * 
	 * @param text is the text that user input
	 * @param name is the name that user 's LINE ID
	 * @return the string output
	 * @throws Exception is the exception that we have
	 */
	
	
	@Override
	public String search(String text,String name) throws Exception {
		
		String result = null;
		Connection connection = null;
		PreparedStatement stmt = null;
		String keyword;
		String response;
		ResultSet rs = null;
		// don 't know why don 't go in test case. test testtest
		
		try {
			connection = getConnection();
		}
		catch (URISyntaxException e1) {
			log.info("URISyntaxException while closing file: {}", e1.toString());
		}
		catch (SQLException e2) {
			log.info("URISyntaxException while closing file: {}", e2.toString());
		}
		
		try {
			//FAQ Enquiry
			FAQ faq = FAQ.getFAQ(stmt, connection);
			result = faq.enquiry(text);
			
	          if(text.toLowerCase().contains("tourstatus")) {
	        	     // this is a Method for keep track user 's tours status
	        	     // and notAll() would used in the future since the once the user
	        	     // implements his datum and it would run update.
	        	     // but now it might not needed.
	        	     // and it only provided real time update when running.
	        	     // difference from the notice function that used by tour guide
	        	  
	          	//Tourist tmp = new Tourist("tschiuaa",0);  // the most impt,we are going to find tschiuaa 's tourstatus
	           	StatusName status = new StatusName("hotspringtrip"); // string inside constructor is uselss for this case , but very useful in implementation parts
	           	//status.notAll(); // this is not have its use complete until the implementation part finish
	           	result = "\n******Here are your tour Status****** \n\n";
	           	result += "@ Remark : Introduction and Review from tourists shall be shown once customer selected a tour (Fulfilled req 3.2.)\n";
	           	result += "@ Remark2: So the result is not finalised since some customers are not paid, but review just given them to have a look whether they hold the positions in trips.\n";
	           	for(int i = 0 ; i < status.getSizeOfTripNum(tmp) ;i++)
	            {
	            result += status.CheckStatus(tmp,i); // check status Method
	            }
	          }
	           	
		        if(text.toLowerCase().contains("balance"))
		        {
		        //	Tourist tmp = new Tourist("tschiuaa",0); 
		        	// this is part that keep track about pricing of each tours that users should paid
	 		    //tmp = new Tourist("tschiuaa",0); 
	 			PriceCalculate pc = new PriceCalculate(tmp);   
	 			result = "\n******Here are your tour record****** \n";
	 			result += "@Remark : The tour, traffic and any other fee, and the total price shall be echoed eventually.(Fulfilled req 4.) \n";
	 			for(int i = 0 ; i <  pc.getSizeOfCustomer() ; i++) {
	 				result += pc.getDescription(i);
	 			}
		        }
	        
		}
		catch (SQLException e3) {
			log.info("SQLException while closing file: {}", e3.toString());
		}
		
		finally {
			try {
				if(rs!=null)
					rs.close();
				if(stmt!=null)
					stmt.close();
				if(connection!=null)
					connection.close();
			}
			catch (SQLException e4) {
				log.info("SQLException while closing file: {}", e4.toString());
			}
		}
		
		if (result != null) {
			return result;
		}else {
			return "Sorry, we don't have answer for this.";
		}

//		throw new Exception("NOT FOUND");
	}
	
	/**
	 * 
	 * get connection 
	 * 
	 * @return connection status
	 * @throws URISyntaxException excpetion of url case
	 * @throws SQLException sql exception of the case
	 */
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI("postgres://itniqlvmjieeeq:1bf7632721043bf02f0f498974ece3939ed3e947b9c96435ca0ced9d06593f33@ec2-107-22-235-167.compute-1.amazonaws.com:5432/daj5i9364g5dfv");
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
//		URI dbUri = new URI(System.getenv("postgres://itniqlvmjieeeq:1bf7632721043bf02f0f498974ece3939ed3e947b9c96435ca0ced9d06593f33@ec2-107-22-235-167.compute-1.amazonaws.com:5432/daj5i9364g5dfv"));
//
//		String username = itniqlvmjieeeq;
//		String password = 1bf7632721043bf02f0f498974ece3939ed3e947b9c96435ca0ced9d06593f33;
//		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + '5432' + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
//
//		log.info("Username: {} Password: {}", username, password);
//		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
