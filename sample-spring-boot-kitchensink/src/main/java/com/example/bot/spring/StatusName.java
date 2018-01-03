package com.example.bot.spring;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Slf4j
/**
 * 
 * 
 * Status name 
 * 
 * is the status for the search the status of users  
 * 
 * @author Gordon
 * 
 * 
 *
 */

public class StatusName{
    public KitchenSinkController controller = null;
	public List<Tourist> tourist = new ArrayList<Tourist>(); // Object for Tourist
	public List<String>  tourName = new ArrayList<String>(); // Tourist name
	public List<String>  tripName = new ArrayList<String>(); // your trip Name
	public List<String>  tripStatus = new ArrayList<String>(); // your trip Status
	public List<Integer> numOfChildList = new ArrayList<Integer>(); // keep track about the number of child per customer
	public List<Integer> numOfAdultList = new ArrayList<Integer>(); // keep track about the number of child per customer
	public List<Integer> numOftoodlerist = new ArrayList<Integer>(); 
	public List<Integer> CnumOfChildList = new ArrayList<Integer>(); // keep track about the number of child per customer
	public List<Integer> CnumOfAdultList = new ArrayList<Integer>(); // keep track about the number of child per customer
	public List<Integer> CnumOftoodlerist = new ArrayList<Integer>(); 
	public List<Integer> tripNumList = new ArrayList<Integer>(); 
	public List<String> trip2;
    public List<Integer> tripPrice2  = new ArrayList<Integer>();
    public List<Integer> paidPrice2  = new ArrayList<Integer>();
    
    public List<String>  ccID  = new ArrayList<String>(); 
    public List<String>  ccIDN  = new ArrayList<String>(); 
    public List<String>  NccID  = new ArrayList<String>(); 
    public List<String>  NccIDN  = new ArrayList<String>(); 
    public List<String>  idtest  = new ArrayList<String>(); 
    public List<String>  idtest2  = new ArrayList<String>(); 
    public List<String>  ConfirmedCustomer = new ArrayList<String>(); 
    public List<String>  ConfirmedCustomerNE = new ArrayList<String>(); 
    public List<String>  NConfirmedCustomerNE = new ArrayList<String>(); 
    public List<String>  NConfirmedCustomer = new ArrayList<String>(); 
    public List<Tourist> confirmedto = new ArrayList<Tourist>();
    public List<Tourist> unpaidto = new ArrayList<Tourist>();
	//public String tripName = null;
	public Tourist tmp ;
	public String [] tmpString;
	public String tn; 
	public List<Integer> tripStatus2 = new ArrayList<Integer>();
	public StatusName() {
		
	}
	
	/**
	 * StatusName is searched the status by the trip
	 * 
	 * @param trip is the string that we used.
	 */
	
    public StatusName(String trip) {
	    tn = trip;
	    trip2 = new ArrayList<String>();
	try {
	Connection connection = getConnection2();
	PreparedStatement stmt = connection.prepareStatement("SELECT name,tripstatus,childnum,adultnum,toodlernum from TourInf WHERE tripname like '%'||?||'%'");
	stmt.setString(1,tn);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()){
    	tourName.add(rs.getString("name"));
    	tripStatus.add(Integer.toString(rs.getInt("tripstatus")));
    	numOfChildList.add(rs.getInt("childnum"));
    	numOfAdultList.add(rs.getInt("adultnum"));
    	numOftoodlerist.add(rs.getInt("toodlernum"));
    }
    rs.close();
    stmt.close();
    
	PreparedStatement stmt2 = connection.prepareStatement("SELECT name,tripstatus,childnum,adultnum,toodlernum from TourInf WHERE tripname like '%'||?||'%' and tourfee - amount_paid <= 0  ");
	stmt2.setString(1,tn);
    ResultSet rs2 = stmt2.executeQuery();
    while (rs2.next()){
    	CnumOfChildList.add(rs2.getInt("childnum"));
    	CnumOfAdultList.add(rs2.getInt("adultnum"));
    CnumOftoodlerist.add(rs2.getInt("toodlernum"));
    }
    rs2.close();
    stmt2.close();
    connection.close();
	

	}
	
	catch (Exception e) 
	{
     System.out.println(e);
    }

	
    for(int i = 0 ; i < tourName.size()  ; i++) {
    	tmp = new Tourist(tourName.get(i),Integer.parseInt(tripStatus.get(i))); //Integer.parseInt(tripStatus.get(i))
    	  	PriceCalculate pc = new PriceCalculate(tmp);		    //	PriceCalculate pc = new PriceCalculate(tmp);
    	     addTourist(tmp);
    }
    
	
}
    
    /**
     * People Count for count of the peoplecount for tours
     * 
     * @return the number of people count
     */
    public int PeopleCount2() {
    	// Method refactored
       	int peopleCount = 0;
    	       	   	
	    for(int i : CnumOfChildList) {
 	    	peopleCount += i;
 	    }
 	    
 	    for(int i : CnumOfAdultList) {
 	    	peopleCount += i;
 	    }
 	    
 	    for(int i : CnumOftoodlerist) {
 	    	peopleCount += i;
 	    }
    	
 	
 	    return peopleCount;
    }
    
    
	/**
	 * 
	 * tripStatus in which status 
	 * @return the status of trip
	 */
    public boolean tripStatus() {
    	try {
    		Connection connection = getConnection2();
    		PreparedStatement stmt = connection.prepareStatement("SELECT name,tripstatus from TourInf WHERE tripname like '%'||?||'%' ");
    		stmt.setString(1,tn);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
            	tripNumList.add(rs.getInt("tripstatus"));
            }
            rs.close();
            stmt.close();
            connection.close();
            
            for(int i : tripNumList) {
            	     if(i == 0 ){
            	    	 return false;
            	     }else
            	    	 return true;
            }
            
    		}
    		
    		catch (Exception e) 
    		{
        
        }
    return false;
    }
    
    /**
     * 
     * remove the tourist from the tourist list
     * 
     * @param t is object inside the tourist list
     */
    public void RemoveTourist(Tourist t) {
    	tourist.remove(t);
    }
    
    
    /**
     * addTourist 
     * 
     * add the tourist inside the tourist list
     * 
     * @param t is the object inside the list
     */
    public void addTourist(Tourist t) {
    	tourist.add(t);
    }
    public int getSizeOfTripNum(Tourist tourist) {
    	try {
    		int number = 0 ; 
    		Connection connection = getConnection2();
    		PreparedStatement stmt = connection.prepareStatement("SELECT tripname,tripstatus FROM TourInf where name = ? ");
    		stmt.setString(1,tourist.getName());
    		ResultSet rs = stmt.executeQuery();
    		while(rs.next()) {
    			number++;
    		}
            rs.close();
            stmt.close();
            connection.close();
         return number;
    		}catch(Exception e) {
    		
    		}
    		return 0;
    }
   
    
    /**
     * 
     * Check status of the tourist.
     * 
     * @param tourist is object of tourist
     * @param index of tourist
     * @return the string that we wants.
     */
    public String CheckStatus(Tourist tourist,int index) {
		try {
		Connection connection = getConnection2();
		PreparedStatement stmt = connection.prepareStatement("SELECT tourfee,amount_paid,tripname,tripstatus FROM TourInf where name = ? ");
		stmt.setString(1,tourist.getName());
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			trip2.add(rs.getString("tripname"));
			tripStatus2.add(rs.getInt("tripstatus"));
           	tripPrice2.add(rs.getInt("tourfee"));
           	paidPrice2.add(rs.getInt("amount_paid"));
		}
        rs.close();
        stmt.close();
        connection.close();
        
        String []description = new String[trip2.size()];
        //this.notAllConfirmed();
        //int num = PeopleCount2();
        // it 's always assume the price show out should
        // ve difference from priceCalculate
        // because  didn 't implmented input system.
        // noted by Gordon 
        // No error !!!!
	    for(int i = 0; i < trip2.size() ;i++) {
    	    description[i] = Integer.toString(i)+ ". "+ trip2.get(i)+"\n" ;
    	    if(tripStatus2.get(i) == 0 && (tripPrice2.get(i) - paidPrice2.get(i) == 0)) 
    	    	description[i] += "Status of trip : Not yet establish the trip due to smaller than 20 person and you paid. \n\n"; 
        if(tripStatus2.get(i) == 1 && (tripPrice2.get(i) - paidPrice2.get(i) > 0 )) 
	    	description[i] += "Status of trip : Trip Established but you still didn 't paid for this trip.\n\n ";
    	    if(tripStatus2.get(i) == 2 && (tripPrice2.get(i) - paidPrice2.get(i) > 0 )) 
    	    	description[i] += "Status of trip : Trip Established and full but you still need to paid for this trip.\n\n"; 
    	    if(tripStatus2.get(i) == 1 && (tripPrice2.get(i) - paidPrice2.get(i) == 0 )) 
	    	description[i] += "Status of trip : Trip Established and you paid.\n\n"; 
    	    if(tripStatus2.get(i) == 2 && (tripPrice2.get(i) - paidPrice2.get(i) == 0 )) 
    	    	description[i] += "Status of trip : Trip Established and full also you paid. \n\n"; 
    	    if(tripStatus2.get(i) == 2 && (tripPrice2.get(i) - paidPrice2.get(i) < 0 )) 
    	    	description[i] += "Status of trip : Trip Established and full also you paid, we 'll refund to you. \n\n"; 
    		if(tripStatus2.get(i) == 0 && (tripPrice2.get(i) - paidPrice2.get(i) < 0)) 
        description[i] += "Status of trip : Not yet establish the trip due to smaller than 20 person and refund will return to you. \n\n"; 
    		if(tripStatus2.get(i) == 0 && (tripPrice2.get(i) - paidPrice2.get(i) > 0)) 
        	description[i] += "Status of trip : Not yet establish the trip due to smaller than 20 person and you still need to paid for this trip.\n\n"; 
        
    	    
	    }
	    
		return description[index];
		}catch(Exception e) {
		
		}
		return "hoho";
    }
    
	/**
	 * People Count of the number of price
	 * @return the number of people
	 */
    public int PeopleCount() {
    	// Method refactored
       	int peopleCount = 0;
    	       	   	
	    for(int i : numOfChildList) {
 	    	peopleCount += i;
 	    }
 	    
 	    for(int i : numOfAdultList) {
 	    	peopleCount += i;
 	    }
 	    
 	    for(int i : numOftoodlerist) {
 	    	peopleCount += i;
 	    }
    		    
 	    return peopleCount;
    }


    /**
     * 
     * it confirmed all the tourist if the number of tourist > 20 and < 50 for tourist.
     * 
     * 
     * 
     */
    public void notAllConfirmed() {
    	try {
        Connection connection = getConnection2();
		PreparedStatement stmt = connection.prepareStatement("SELECT name,id FROM TourInf where tripname = ? AND (tourfee - amount_paid) <= 0 ");
		stmt.setString(1,tn);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			if(PeopleCount2() >= 20) {
			ConfirmedCustomer.add(rs.getString("name"));
			idtest.add(rs.getString("id"));
			}
			else if(PeopleCount2() < 20) {
			ConfirmedCustomerNE.add(rs.getString("name"));
			NccID.add(rs.getString("id"));
			}
			ccID.add(rs.getString("id"));
		}
        rs.close();
        stmt.close();
        connection.close();
     // SET CONFIRMED CUSTOMER
     for(int i = 0; i < ConfirmedCustomer.size(); i++) {
    	      confirmedto.add(new Tourist(ConfirmedCustomer.get(i) , 0 ) );
     }
     // SET NOT CONFIRMED CUSTOMER
     
    	}catch(Exception e) {
    		
    	}
    	
    	
    	
    	try {
    		Connection connection = getConnection2();
    		PreparedStatement stmt = connection.prepareStatement("SELECT name,id FROM TourInf where tripname = ? AND (tourfee - amount_paid) > 0 ");
    		stmt.setString(1,tn);
    		ResultSet rs = stmt.executeQuery();
    		while(rs.next()) {
    		    if(PeopleCount2() >= 20) {
    			NConfirmedCustomer.add(rs.getString("name"));
    			idtest2.add(rs.getString("id"));
    		    }
    		    else if(PeopleCount2() < 20) {
    		    	NConfirmedCustomerNE.add(rs.getString("name"));
    		    	NccIDN.add(rs.getString("id"));
    		    }
    		    ccID.add(rs.getString("id"));
    		}
            rs.close();
            stmt.close();
            connection.close();
            for(int i = 0; i < NConfirmedCustomer.size(); i++) {
      	      unpaidto.add(new Tourist(NConfirmedCustomer.get(i) , 0 ) );
       }      
    	}catch(Exception e) {
    		
    		
    	}
    	// if the situation for paid customer
    	/*
    for(Tourist t:confirmedto) {
    	if(PeopleCount2() >= 50 && t.getTourName(t).equals(tn) ) {
   		 //k.postText(event.getSource().getUserId(),"Hi,Customer,I am going to updated your trip status : Success");
   		// if success, then notify success and contact the guide ,controller.posttext(id,text)
   	    // if failure, cancel the tour
   		}else if(PeopleCount2() >= 20 && PeopleCount2() < 50 && t.getTourName(t).equals(tn)) {     
   	    //k.postText(event.getSource().getUserId(),"Hi,Customer,I am going to updated your trip status : Success ");  
   	    // if success, then notify success and contact the guide, then notify success and contact the guide ,controller.posttext(id,text)
       }else if(PeopleCount2() < 20 && t.getTourName(t).equals(tn)) {
       //k.postText(event.getSource().getUserId(),"Hi,Customer,I am going to updated your trip status : Failure ");
   	    // if failure, cancel the tour
       }
    }
    	
    for(Tourist t:unpaidto) {
    	if(PeopleCount2() >= 50 && t.getTourName(t).equals(tn) ) {
    		//controller.postText("U53250c5aaa0ebc147a426e384e5d751c","Hi,Customer,I am going to updated your trip status : failure ");
   		// if success, then notify success and contact the guide ,controller.posttext(id,text)
   	    // if failure, cancel the tour
   		}else if(PeopleCount2() >= 20 && PeopleCount2() < 50 && t.getTourName(t).equals(tn)) {     
   	    //k.postText(event.getSource().getUserId(),"Hi,Customer,I am going to updated your trip status : Success ");  
   	    // if success, then notify success and contact the guide, then notify success and contact the guide ,controller.posttext(id,text)
       }else if(PeopleCount2() < 20 && t.getTourName(t).equals(tn)) {
       //k.postText(event.getSource().getUserId(),"Hi,Customer,I am going to updated your trip status : Failure ");
   	    // if failure, cancel the tour
       }
    }
    	*/
    	
    }
    /**
     * not All for temp status of tourist.
     * 
     */
	public void notAll() {   
		//controller.hehe();
		//tmp called only
 	    //Observer Pattern used, solved bug and each notification only takes using the tripName instead of Tourist name.
		for(Tourist t : tourist) 
        {
            if(PeopleCount() >= 50 && t.getTourName(t).equals(tn) ) {
            	t.update(2,tn);
            }
            else if(PeopleCount() >= 20 && PeopleCount() < 50 && t.getTourName(t).equals(tn)) {     
            t.update(1,tn);
            }
            else if(PeopleCount() < 20 && t.getTourName(t).equals(tn)){  
            t.update(0,tn);
            }
        }
	}
	
	/**
	 * 
	 * getconnection 
	 * @return the connection 
	 * @throws URISyntaxException is the url exception 
	 * @throws SQLException is the sql exception 
	 */
	
	private Connection getConnection2() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI("postgres://itniqlvmjieeeq:1bf7632721043bf02f0f498974ece3939ed3e947b9c96435ca0ced9d06593f33@ec2-107-22-235-167.compute-1.amazonaws.com:5432/daj5i9364g5dfv");
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}
	
	/**
	 * 
	 * return the number of confirmed tourist
	 * @return the confirm number of toruist
	 */
	public List<Tourist> getConfirmedTourist(){
		return confirmedto;
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * @return the number of size of confirmed customer
	 */
	public List<String> getStringSize() {
		return ConfirmedCustomer;
	}
	
	/**
	 * 
	 * 
	 * @return non confirmed customer
	 */
	public List<String> getFailSize(){
	   return NConfirmedCustomer;
	}
	
	/**
	 * 
	 * 
	 * @return the confirmed customer with non established
	 */
	public List<String> getNEConfirmed(){
		return ConfirmedCustomerNE;
	}
	
	/**
	 * 
	 * 
	 * @return the non confirmed customer with non established
	 */
	public List<String> getNEConfirmedN(){
		return NConfirmedCustomerNE;
	}
	
	
	/**
	 * 
	 * 
	 * @return ccid
	 */
	public List<String> getccid(){
		return ccID;
	}

	
	/**
	 * 
	 * 
	 * 
	 * @return ccidn
	 */
	public List<String> getccidn(){
		return ccIDN;
	}
	
	/**
	 * 
	 * 
	 * 
	 * @return nccid
	 */
	public List<String> getnccid(){
		return NccID;
	}
	/**
	 * 
	 * 
	 * 
	 * @return nccidn
	 */
	public List<String> getnccidn(){
		return NccIDN;
	}
	
	/**
	 * 
	 * 
	 * @return idtest
	 */
	public List<String> getestid(){
		return idtest;
	}
	
	/**
	 * 
	 * 
	 * 
	 * @return idtest2
	 */
	public List<String> getestid2(){
		return idtest2;
	}
	
}



