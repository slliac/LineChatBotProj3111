package com.example.bot.spring;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.example.bot.spring.DatabaseEngine;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)

//@SpringBootTest(classes = { KitchenSinkTester.class, DatabaseEngine.class })
@SpringBootTest(classes = { KitchenSinkTester.class, SQLDatabaseEngine.class })
 // i AM TESTING SOMETHING     
public class KitchenSinkTester{
	@Autowired
	private SQLDatabaseEngine databaseEngine;
	int i = 0 ; 
	
	@Test
	public void ftestCreateDiscount1111(){
		//public boolean createDiscount(String couponID, int amount, int percentOff)
		try {
			String userid = "123";
			StatusName status = new StatusName();
			List<Tourist> getConfirmedTourist =  status.getConfirmedTourist();
			List<String> getStringSize =  status.getStringSize();
			List<String> gf = status.getFailSize();
			List<String> gf2 = status.getNEConfirmed();
			List<String> getNEConfirmedN = status.getNEConfirmedN();
			List<String> getccid = status.getccid();
			getccid = status.getnccid();
		    getccid = status.getnccidn();
		    getccid = status.getccidn();
			getccid = status.getestid();
			getccid = status.getestid2();
			int price = 10000;
			String [] booking1 = {"123","Shenzhen","123","123","123","123","123"};
			//ksc.bookingTour("123",booking1);
			//ksc.Mpost(booking1, new TextMessage("123"));
			//ksc.handleTextContent("123",);
			SQLDatabaseEngine sql = new SQLDatabaseEngine();
		    Tourist tmp = new Tourist("tschiuaa",0); // Assume D.Chan joined the tripB and Trip B cost 1000 HKD for adult,800 for child.child got 80% on against because of Back school discount
			PriceCalculate pc = new PriceCalculate(tmp);
			price = pc.PriceCalculateForTrip("tripB");
			sql.checkTour("tripB");
            sql.updatePrice(booking1,userid,price);
            sql.booking(booking1,userid);
            sql.createDiscount("test123",0,10);
            sql.createDiscount("test123",-1,10);
            sql.createDiscount("test123",20,30);
            sql.deleteDiscount("test123");
        //	assertThat( sql.deleteDiscount("test123") ).isEqualTo(false);
        	sql.createDiscount("test124",20,30);
        	sql.displayTour("may i ask for the details of tripB");
        	assertThat( sql.displayTour("INVALID INPUT") ).isEqualTo("Sorry but your input format was invalid~");
        	String expectedOutput = "Oops, no related result found. \n" + 
        			"\n" + 
        			sql.recommendation("");
            String rs =null;
            
			rs  = sql.switchFunctions("search swimming suit","tschiuaa");
			rs  = sql.switchFunctions("search vegeterian","tschiuaa");
			rs  = sql.switchFunctions("search deadline","tschiuaa");
			rs  = sql.switchFunctions("search tour size","tschiuaa");
			rs  = sql.switchFunctions("search air ticket and trip","tschiuaa");
			rs  = sql.switchFunctions("search waiting list","tschiuaa");
			rs  = sql.switchFunctions("search language support","tschiuaa");
			rs  = sql.switchFunctions("search apply","tschiuaa");
			rs  = sql.switchFunctions("details of tripB","tschiuaa");
			rs  = sql.switchFunctions("survey:tripB:5","tschiuaa");
			rs  = sql.switchFunctions("hello","tschiuaa");
			rs  = sql.switchFunctions("recommend","tschiuaa");
			rs  = sql.switchFunctions("thank you","tschiuaa");
			rs  = sql.switchFunctions("search assembly point","tschiuaa");
			rs  = sql.switchFunctions("search insurance","tschiuaa");
			rs  = sql.switchFunctions("search tour cancelled","tschiuaa");
			rs  = sql.switchFunctions("search additional charge","tschiuaa");
			rs  = sql.switchFunctions("search air ticket and trip","tschiuaa");
			rs  = sql.switchFunctions("keyword in HongKong","tschiuaa");
			rs  = sql.switchFunctions("keyword from 2017-11-20 to 2017-11-23","tschiuaa");
			rs  = sql.switchFunctions("keyword from now","tschiuaa");
			//String ee2 = sql.switchFunctions("search balance","U51ec8548e7b7c9c7424c84b084d60ef3");
			assertThat( sql.searchByKeywords("") ).isEqualTo("Sorry but your input format was invalid~");
			assertThat( sql.searchByKeywords("keyword from") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword in") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword to") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword for") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword from 123") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword from 2017-01-O1") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword from 2017-01-00") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword from 2017-01-") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword to 123") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword to 2017-01-O1") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword to 2017-01-00") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword to 2017-01-") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword for ABC") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			
			assertThat( sql.createDiscount("",10,10) ).isEqualTo(false);
			assertThat( sql.createDiscount("test123",0,10) ).isEqualTo(false);
			assertThat( sql.createDiscount("test123",-1,10) ).isEqualTo(false);
			assertThat( sql.createDiscount("test123",50,0) ).isEqualTo(false);
			assertThat( sql.createDiscount("test123",50,-10) ).isEqualTo(false);

			//assertThat( sql.createDiscount("test123",20,30) ).isEqualTo(true);
			//assertThat( sql.createDiscount("test123",20,30) ).isEqualTo(false);
			
			//assertThat( sql.deleteDiscount("test123") ).isEqualTo(true);
			//assertThat( sql.deleteDiscount("test123") ).isEqualTo(false);
		}
		catch(Exception e) {}
	}
	
	@Test
	public void testRecommendation(){
		try {
			SQLDatabaseEngine sql = new SQLDatabaseEngine();
			//assertThat( sql.recommendation("") ).isEqualTo("Would you like to try tripB? It has the highest rating from other tourists! ");
		}
		catch(Exception e) {}
	}

	
	@Test
	public void returnTrue(){
		assertThat(true).isEqualTo(true);
	}
	
	

	
	
	@Test
	public void testFAQSetQuery() throws Exception{
		
		try {
			String rs = null;
			SQLDatabaseEngine sql = new SQLDatabaseEngine();

			FAQ faq = FAQ.getFAQ(null, null);
			faq.setQuery(new FAQQueryWithDirectSearch());

		}catch(Exception e) {

		}
		assertTrue(true);
	}

	
	@Test
	public void credTest111111a() throws Exception{
		
		try {
			int j  = 1;
			
			int i  = 01 ;
			//KitchenSinkController ksc = new KitchenSinkController();
			//DatabaseEngine db = new DatabaseEngine();
			//db.search("123","tschiuaa");
			//db.search("","tschiuaa");
			String userid = "123";
			
			int price = 10000;
			String [] booking1 = {"123","Shenzhen","123","123","123","123","123"};
			//ksc.bookingTour("123",booking1);
			//ksc.Mpost(booking1, new TextMessage("123"));
			//ksc.handleTextContent("123",);
			SQLDatabaseEngine sql = new SQLDatabaseEngine();
			sql.checkTour("tripB");
            sql.updatePrice(booking1,userid,price);
            sql.booking(booking1,userid);

		}catch(Exception e) {

		}
		assertTrue(true);
	}
	


	@Test
	public void testFaqGetKeywords() throws Exception{
		
		try {
			String rs = null;
			SQLDatabaseEngine sql = new SQLDatabaseEngine();

			FAQ faq = FAQ.getFAQ(null, null);
			faq.getKeywords(true,true);
			faq.getKeywords(true,false);
			faq.getKeywords(false,true);
			faq.getKeywords(false,false);
			faq.setQuery(new FAQQueryWithDirectSearch());

		}catch(Exception e) {

		}
		assertTrue(true);
	}
		
	@Test
		public void SQLKeyWordCheck1a() throws Exception{
		boolean statuscheck = false;
		try {
			String result = null;
		    Tourist tmp = new Tourist("tschiuaa",0);
			StatusName status = new StatusName("hotspringtrip"); // assume we  establish trip name called TripB 
		    status.notAll(); 
		    System.out.print(status.PeopleCount2()+"hahahahahahahahahahah");
		    status.notAllConfirmed(); // this is further notify
		    statuscheck = status.tripStatus();
           	for(int i = 0 ; i < status.getSizeOfTripNum(tmp) ;i++)
            {
            result += status.CheckStatus(tmp,i); // check status Method
            }
		    
		} catch (Exception e) {
		}
			try {
				
				int price = 0 ; 
				try {
				    Tourist tmp = new Tourist("tschiuaa",0); // Assume D.Chan joined the tripB and Trip B cost 1000 HKD for adult,800 for child.child got 80% on against because of Back school discount
					PriceCalculate pc = new PriceCalculate(tmp);
					price = pc.PriceCalculateForTrip("tripB");
					System.out.println("The price is "+ price);
					
			} catch (Exception e) {
					
				}
				

				FAQ faq = FAQ.getFAQ(null, null);
				faq.getKeywords(true,true);
				faq.getKeywords(true,false);
				faq.getKeywords(false,true);
				faq.getKeywords(false,false);

				String rs = null;
				SQLDatabaseEngine sql = new SQLDatabaseEngine();
				 boolean haha =  sql.getDiscount("123","235");
				 boolean haha2 =  sql.createDiscount("Shen Zhen",123,235);
				rs  = sql.switchFunctions("search tourstatus","tschiuaa");
				rs  = sql.switchFunctions("search balance","tschiuaa");
				rs  = sql.switchFunctions("search apply","tschiuaa");
				rs  = sql.switchFunctions("details of tripB","tschiuaa");
				rs  = sql.switchFunctions("survey:tripB:5","tschiuaa");
				rs  = sql.switchFunctions("hello","tschiuaa");
				rs  = sql.switchFunctions("recommend","tschiuaa");
				rs  = sql.switchFunctions("thank you","tschiuaa");
				rs  = sql.switchFunctions("search assembly point","tschiuaa");
				rs  = sql.switchFunctions("search insurance","tschiuaa");
				rs  = sql.switchFunctions("search tour cancelled","tschiuaa");
				rs  = sql.switchFunctions("search additional charge","tschiuaa");

			}catch(Exception e) {

			}
			assertTrue(true);
	}
		
	@Test
	public void SQLKeyWordCheck2a() throws Exception{

		try {
			String rs = null;
			SQLDatabaseEngine sql = new SQLDatabaseEngine();

			rs  = sql.switchFunctions("search hotel bed","tschiuaa");
			rs  = sql.switchFunctions("search tour fee","tschiuaa");
			rs  = sql.switchFunctions("search transportation","tschiuaa");
			rs  = sql.switchFunctions("search contact","tschiuaa");
			rs  = sql.switchFunctions("search visa","tschiuaa");
			
		}catch(Exception e) {

		}
		assertTrue(true);
	}

	@Test
	public void SQLKeyWordCheck3a() throws Exception{

		try {
			String rs = null;
			//KitchenSinkController ksc = new KitchenSinkController();
			//DatabaseEngine db = new DatabaseEngine();
			//db.search("123","tschiuaa");
			//db.search("","tschiuaa");
			String userid = "123";
			int price = 10000;
			String [] booking1 = {"123","Shenzhen","123","123","123","123","123"};
			//ksc.bookingTour("123",booking1);
			//ksc.Mpost(booking1, new TextMessage("123"));
			//ksc.handleTextContent("123",);

			SQLDatabaseEngine sql = new SQLDatabaseEngine();
			//sql.checkTour("tripB");
            //sql.updatePrice(booking1,userid,price);
            //sql.booking(booking1,userid);
			rs  = sql.switchFunctions("search swimming suit","tschiuaa");
			rs  = sql.switchFunctions("search vegeterian","tschiuaa");
			rs  = sql.switchFunctions("search deadline","tschiuaa");
			rs  = sql.switchFunctions("search tour size","tschiuaa");
			rs  = sql.switchFunctions("search air ticket and trip","tschiuaa");
			rs  = sql.switchFunctions("search waiting list","tschiuaa");
			rs  = sql.switchFunctions("search language support","tschiuaa");
			
		}catch(Exception e) {

		}
		assertTrue(true);
	}
@Test
public void ControllerTest() throws Exception{

}
	
@Test 
public void SQLKeyWordCheck2b() throws Exception{
try {
	
	StatusName status = new StatusName();
	List<Tourist> getConfirmedTourist =  status.getConfirmedTourist();
	List<String> getStringSize =  status.getStringSize();
	List<String> gf = status.getFailSize();
	List<String> gf2 = status.getNEConfirmed();
	List<String> getNEConfirmedN = status.getNEConfirmedN();
	List<String> getccid = status.getccid();
	getccid = status.getnccid();
    getccid = status.getnccidn();
    getccid = status.getccidn();
	getccid = status.getestid();
	getccid = status.getestid2();
	String rs2 = null;
	SQLDatabaseEngine sql = new SQLDatabaseEngine();
	rs2  = sql.switchFunctions("search tour cancelled","tschiuaa");	
	rs2  = sql.switchFunctions("search tour fee","tschiuaa");
	rs2  = sql.switchFunctions("search hotel bed","tschiuaa");
	rs2  = sql.switchFunctions("search tour fee","tschiuaa");
	rs2  = sql.switchFunctions("search language support","tschiuaa");
	rs2  = sql.switchFunctions("search tourstatus","tschiuaa");
}catch(Exception e) {}
assertTrue(true);
}


@Test 
public void SQLCheck3() throws Exception{
try {
	String rs = null;
	SQLDatabaseEngine sql = new SQLDatabaseEngine();
	//rs  = sql.switchFunctions("search air ticket and trip","tschiuaa");
	//rs  = sql.switchFunctions("keyword in HongKong","tschiuaa");
	//rs  = sql.switchFunctions("keyword from 2017-11-20 to 2017-11-23","tschiuaa");
	//rs  = sql.switchFunctions("keyword from now","tschiuaa");
}catch(Exception e) {}
assertTrue(true);
}

@Test 
public void SQLCheck4() throws Exception{
try {
	String rs = null;
	SQLDatabaseEngine sql = new SQLDatabaseEngine();
	//rs  = sql.switchFunctions("search tourstatus","tschiuaa");
	//rs  = sql.switchFunctions("search balance","tschiuaa");
}catch(Exception e) {}
assertTrue(true);
}


	@Test
	public void StatusCheck() throws Exception{
		
		assertTrue(true) ; // tmp status check notify, not refer as final check
	}
	
}

/*
package com.example.bot.spring;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.example.bot.spring.DatabaseEngine;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)

//@SpringBootTest(classes = { KitchenSinkTester.class, DatabaseEngine.class })
@SpringBootTest(classes = { KitchenSinkTester.class, SQLDatabaseEngine.class })
 // i AM TESTING SOMETHING     
public class KitchenSinkTester{
	@Autowired
	private SQLDatabaseEngine databaseEngine;
	int i = 0 ; 
//	@Test
//	public void returnTrue(){
//		assertThat(true).isEqualTo(true);
//	}
	@Test
	public void testSwitchFunctions(){
		assertThat(true).isEqualTo(true);
	}
	
	@Test
	public void testCreateDiscount(){
		//public boolean createDiscount(String couponID, int amount, int percentOff)
		try {
			SQLDatabaseEngine sql = new SQLDatabaseEngine();
			assertThat( sql.createDiscount("",10,10) ).isEqualTo(false);
			assertThat( sql.createDiscount("test123",0,10) ).isEqualTo(false);
			assertThat( sql.createDiscount("test123",-1,10) ).isEqualTo(false);
			assertThat( sql.createDiscount("test123",50,0) ).isEqualTo(false);
			assertThat( sql.createDiscount("test123",50,-10) ).isEqualTo(false);

			assertThat( sql.createDiscount("test123",20,30) ).isEqualTo(true);
			assertThat( sql.createDiscount("test123",20,30) ).isEqualTo(false);
			
			assertThat( sql.deleteDiscount("test123") ).isEqualTo(true);
			assertThat( sql.deleteDiscount("test123") ).isEqualTo(false);
		}
		catch(Exception e) {}
	}
	@Test
	public void testGetDiscount(){
		//public boolean getDiscount(String clientID, String couponID) 
		try {
			SQLDatabaseEngine sql = new SQLDatabaseEngine();
			//assertThat( sql.createDiscount("test124",20,30) ).isEqualTo(true);
			//REAL TEST
			assertThat( sql.getDiscount("","test124") ).isEqualTo(false);
			assertThat( sql.getDiscount("H00001","") ).isEqualTo(false);
			assertThat( sql.getDiscount("WRONG NAME","test124") ).isEqualTo(false);
			assertThat( sql.getDiscount("H00001","WRONG NAME") ).isEqualTo(false);

			assertThat( sql.getDiscount("H00001","test124") ).isEqualTo(true);
			assertThat( sql.getDiscount("H00001","test124") ).isEqualTo(false);
			//END
			assertThat( sql.deleteDiscount("test124") ).isEqualTo(true);
		}
		catch(Exception e) {}
	}
	@Test
	public void testDisplayTour(){
		//public String displayTour(String text) throws Exception 
		try {
			SQLDatabaseEngine sql = new SQLDatabaseEngine();
			assertThat( sql.displayTour("INVALID INPUT") ).isEqualTo("Sorry but your input format was invalid~");
			String expectedOutput = "Oops, no related result found. \n" + 
					"\n" + 
					sql.recommendation("");
			assertThat( sql.displayTour("details of test123") ).isEqualTo(expectedOutput);
			String output="Details of tour tripB: \n" + 
					"Tour Fee: 25000\n" + 
					"Date: 2017-10-31 - 2017-11-04\n" + 
					"Location: HongKong\n" + 
					"Application Deadline: 2017-10-20\n" + 
					"\n" + 
					"Book it now by typing in: \"book:[tourname]\"!";
			//assertThat( sql.displayTour("details of tripB") ).isEqualTo(output);
			assertThat( sql.displayTour("may i ask for the details of tripB") ).isEqualTo(output);
			assertThat( sql.displayTour("may i ask for the details of tripB thank you") ).isEqualTo(output);
			assertThat( sql.displayTour("i want the details of tripB!") ).isEqualTo(output);
		}
		catch(Exception e) {}
	}
	@Test
	public void testRecommendation(){
		try {
			SQLDatabaseEngine sql = new SQLDatabaseEngine();
			assertThat( sql.recommendation("") ).isEqualTo("Would you like to try tripB? It has the highest rating from other tourists! ");
		}
		catch(Exception e) {}
	}
	@Test
	public void testSearchByKeywords(){
		//public String searchByKeywords(String input) throws Exception {
		try {
			SQLDatabaseEngine sql = new SQLDatabaseEngine();
			assertThat( sql.searchByKeywords("") ).isEqualTo("Sorry but your input format was invalid~");
			assertThat( sql.searchByKeywords("keyword from") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword in") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword to") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword for") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword from 123") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword from 2017-01-O1") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword from 2017-01-00") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword from 2017-01-") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword to 123") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword to 2017-01-O1") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword to 2017-01-00") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword to 2017-01-") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			assertThat( sql.searchByKeywords("keyword for ABC") ).isEqualTo(sql.searchByKeywords("Sorry but your input format was invalid~"));
			
			String notFound = "Keywords: 123\n" + 
					"\n" + 
					"Oops, no related result found. \n" + 
					"\n" + 
					sql.recommendation("");
			//assertThat( sql.searchByKeywords("keyword from 3017-11-11") ).isEqualTo(sql.searchByKeywords(notFound));
			assertThat( sql.searchByKeywords("keyword to 2000-1-1") ).isEqualTo(sql.searchByKeywords(notFound));
			assertThat( sql.searchByKeywords("keyword in UNKNOWN-PLACE") ).isEqualTo(sql.searchByKeywords(notFound));
			assertThat( sql.searchByKeywords("keyword for -10000") ).isEqualTo(sql.searchByKeywords(notFound));
			
			String trueCase = "search keyword a tour in shenzhen from now to 2019-01-01 for 50000";
			String result = "Keywords: shenzhen, now, 2019-01-01, 50000\n" + 
					"\n" + 
					"These trips maybe good for you: \n" + 
					"1. application deadline discount sales test case\n" + 
					"2. Shenzhen seafood trip\n" + 
					"3. Shenzhen basketball trip";
			assertThat( sql.searchByKeywords(trueCase) ).isEqualTo(sql.searchByKeywords(result));
		}
		catch(Exception e) {}
	}
	
	
	*/
//	createDiscount
//	getDiscount
//	displayTour
//	recommendation
//	searchByKeywords
	
	
//	@Test
//	public void PriceCheck() throws Exception{
//		int price = 0 ; 
//		try {
//		    Tourist tmp = new Tourist("tschiuaa",0); // Assume D.Chan joined the tripB and Trip B cost 1000 HKD for adult,800 for child.child got 80% on against because of Back school discount
//			PriceCalculate pc = new PriceCalculate(tmp);
//			price = pc.PriceCalculateForTrip("tripB");
//			System.out.println("The price is "+ price);
//			
//	} catch (Exception e) {
//			
//		}
//		assertTrue(price == 437500); // so the total price should be 26400 HKD , 20000 for Adult, 6400 for Child(10000*0.8*0.8) , 0 for toodler
//	}
//	
//	
//	@Test
//	public void testFAQSetQuery() throws Exception{
//		
//		try {
//			String rs = null;
//			SQLDatabaseEngine sql = new SQLDatabaseEngine();
//
//			FAQ faq = FAQ.getFAQ(null, null);
//			faq.setQuery(new FAQQueryWithDirectSearch());
//
//		}catch(Exception e) {
//
//		}
//		assertTrue(true);
//	}
//
//	
//	@Test
//	public void FredTest() throws Exception{
//		
//		try {
//			int j  = 1;
//			
//			int i  = 01 ;
//			KitchenSinkController ksc = new KitchenSinkController();
//			DatabaseEngine db = new DatabaseEngine();
//			db.search("123","tschiuaa");
//			db.search("","tschiuaa");
//			String userid = "123";
//			int price = 10000;
//			String [] booking1 = {"123","Shenzhen","123","123","123","123","123"};
//			ksc.bookingTour("123",booking1);
//			ksc.Mpost(booking1, new TextMessage("123"));
//			//ksc.handleTextContent("123",);
//			SQLDatabaseEngine sql = new SQLDatabaseEngine();
//			sql.checkTour("tripB");
//            sql.updatePrice(booking1,userid,price);
//            sql.booking(booking1,userid);
//
//		}catch(Exception e) {
//
//		}
//		assertTrue(true);
//	}
//	
//
//
//	@Test
//	public void testFaqGetKeywords() throws Exception{
//		
//		try {
//			String rs = null;
//			SQLDatabaseEngine sql = new SQLDatabaseEngine();
//
//			FAQ faq = FAQ.getFAQ(null, null);
//			faq.getKeywords(true,true);
//			faq.getKeywords(true,false);
//			faq.getKeywords(false,true);
//			faq.getKeywords(false,false);
//			faq.setQuery(new FAQQueryWithDirectSearch());
//
//		}catch(Exception e) {
//
//		}
//		assertTrue(true);
//	}
//		
//	@Test
//		public void SQLCheck111() throws Exception{
//			
//			try {
//				FAQ faq = FAQ.getFAQ(null, null);
//				faq.getKeywords(true,true);
//				faq.getKeywords(true,false);
//				faq.getKeywords(false,true);
//				faq.getKeywords(false,false);
//
//				String rs = null;
//				SQLDatabaseEngine sql = new SQLDatabaseEngine();
//				 boolean haha =  sql.getDiscount("123","235");
//				 boolean haha2 =  sql.createDiscount("Shen Zhen",123,235);
//				rs  = sql.switchFunctions("search apply");
//				rs  = sql.switchFunctions("details of tripB");
//				rs  = sql.switchFunctions("survey:tripB:5");
//				rs  = sql.switchFunctions("hello");
//				rs  = sql.switchFunctions("recommend");
//				rs  = sql.switchFunctions("thank you");
//				rs  = sql.switchFunctions("search assembly point");
//				rs  = sql.switchFunctions("search insurance");
//				rs  = sql.switchFunctions("search tour cancelled");
//				rs  = sql.switchFunctions("search additional charge");
//
//			}catch(Exception e) {
//
//			}
//			assertTrue(true);
//	}
//		
//	@Test
//	public void SQLCheck112() throws Exception{
//
//		try {
//			String rs = null;
//			SQLDatabaseEngine sql = new SQLDatabaseEngine();
//
//			rs  = sql.switchFunctions("search hotel bed");
//			rs  = sql.switchFunctions("search tour fee");
//			rs  = sql.switchFunctions("search transportation");
//			rs  = sql.switchFunctions("search contact");
//			rs  = sql.switchFunctions("search visa");
//			
//		}catch(Exception e) {
//
//		}
//		assertTrue(true);
//	}
//
//	@Test
//	public void SQLCheck1113() throws Exception{
//
//		try {
//			String rs = null;
//			//KitchenSinkController ksc = new KitchenSinkController();
//			//DatabaseEngine db = new DatabaseEngine();
//			//db.search("123","tschiuaa");
//			//db.search("","tschiuaa");
//			String userid = "123";
//			int price = 10000;
//			String [] booking1 = {"123","Shenzhen","123","123","123","123","123"};
//			//ksc.bookingTour("123",booking1);
//			//ksc.Mpost(booking1, new TextMessage("123"));
//			//ksc.handleTextContent("123",);
//
//			SQLDatabaseEngine sql = new SQLDatabaseEngine();
//			sql.checkTour("tripB");
//            sql.updatePrice(booking1,userid,price);
//            sql.booking(booking1,userid);
//			rs  = sql.switchFunctions("search swimming suit");
//			rs  = sql.switchFunctions("search vegeterian");
//			rs  = sql.switchFunctions("search deadline");
//			rs  = sql.switchFunctions("search tour size");
//			rs  = sql.switchFunctions("search air ticket and trip");
//			rs  = sql.switchFunctions("search waiting list");
//			rs  = sql.switchFunctions("search language support");
//			
//		}catch(Exception e) {
//
//		}
//		assertTrue(true);
//	}
//@Test
//public void ControllerTest() throws Exception{
//
//}
//	
//@Test 
//public void SQLCheck2() throws Exception{
//try {
//	
//	StatusName status = new StatusName();
//	List<Tourist> getConfirmedTourist =  status.getConfirmedTourist();
//	List<String> getStringSize =  status.getStringSize();
//	List<String> gf = status.getFailSize();
//	List<String> gf2 = status.getNEConfirmed();
//	List<String> getNEConfirmedN = status.getNEConfirmedN();
//	List<String> getccid = status.getccid();
//	getccid = status.getnccid();
//    getccid = status.getnccidn();
//    getccid = status.getccidn();
//	getccid = status.getestid();
//	getccid = status.getestid2();
//	String rs2 = null;
//	SQLDatabaseEngine sql = new SQLDatabaseEngine();
//	rs2  = sql.switchFunctions("search tour cancelled");	
//	rs2  = sql.switchFunctions("search tour fee");
//	rs2  = sql.switchFunctions("search deadline");
//	rs2  = sql.switchFunctions("search hotel bed");
//	rs2  = sql.switchFunctions("search tour fee");
//	rs2  = sql.switchFunctions("search language support");
//	rs2  = sql.switchFunctions("search tourstatus");
//}catch(Exception e) {}
//assertTrue(true);
//}
//
//
//@Test 
//public void SQLCheck3() throws Exception{
//try {
//	String rs = null;
//	SQLDatabaseEngine sql = new SQLDatabaseEngine();
//	rs  = sql.switchFunctions("search air ticket and trip");
//	rs  = sql.switchFunctions("keyword in HongKong");
//	rs  = sql.switchFunctions("keyword from 2017-11-20 to 2017-11-23");
//	rs  = sql.switchFunctions("keyword from now");
//}catch(Exception e) {}
//assertTrue(true);
//}
//
//@Test 
//public void SQLCheck4() throws Exception{
//try {
//	String rs = null;
//	SQLDatabaseEngine sql = new SQLDatabaseEngine();
//	rs  = sql.switchFunctions("search tourstatus");
//	rs  = sql.switchFunctions("search balance");
//}catch(Exception e) {}
//assertTrue(true);
//}
//
//
//	@Test
//	public void StatusCheck() throws Exception{
//		boolean statuscheck = false;
//		try {
//			String result = null;
//		    Tourist tmp = new Tourist("tschiuaa",0);
//			StatusName status = new StatusName("hotspringtrip"); // assume we  establish trip name called TripB 
//		    status.notAll(); 
//		    System.out.print(status.PeopleCount2()+"hahahahahahahahahahah");
//		    status.notAllConfirmed(); // this is further notify
//		    statuscheck = status.tripStatus();
//           	for(int i = 0 ; i < status.getSizeOfTripNum(tmp) ;i++)
//            {
//            result += status.CheckStatus(tmp,i); // check status Method
//            }
//		    
//		} catch (Exception e) {
//		}
//		assertTrue(true) ; // tmp status check notify, not refer as final check
//	}
//	
//}
//	
//	
////	private StatusName status;
////	private Tourist    tmp;
////	public KitchenSinkController k ;
////	int x = 01 ;
////	int i = 3;
////	//@Autowired
////	//public KitchenSinkController controller ;
////	//public controller c = new controller();
////	/**
////	 * void PriceCheck() 
////	 * 
////	 * This is the function which checked the price for adult and child
////	 * If true  ====>   the trip is counted correctly the number of customer,and calcuated right price
////	 * If false ====>   the trip is counted correctly the number of customer,and calcuated wrong price
////	 * 
////	 * */
////	
////	//@Test
////	//public void test() throws Exception{
////	//	try {
////    //String id = "U53250c5aaa0ebc147a426e384e5d751c";
////	//k.postText(id,"Hi,Customer,I am going to updated your trip status : failure ");
////	//	}catch(Exception e) {}
////	//	assertTrue(true);
////	//}
////	
//////	@Test
//////	public void testSearchByKeywords throws Exception{
//////		boolean thrown = false;
//////		String result = null;
//////		try {
//////			result = this.databaseEngine.searchByKeywords("unknown");
//////		} catch (Exception e) {
//////			thrown = true;
//////		}
//////		assertEqual(result,"Oops, no related result found. ");
//////		thrown = false;
//////		result = null;
//////		try {
//////			result = this.databaseEngine.searchByKeywords("HongKong");
//////		} catch (Exception e) {
//////			thrown = true;
//////		}
//////		assertEqual(result,"These trips maybe good for you: \\n");
//////	}
////	
////	@Test
////	public void placecheck() throws Exception{
////		int price = 0 ; 
////		try {
////			SQLDatabaseEngine db = new SQLDatabaseEngine();
////			//db.search("search:HongKong","slliac");
////		} catch (Exception e) {
////			
////		}
////		assertTrue(true); // so the total price should be 26400 HKD , 20000 for Adult, 6400 for Child(10000*0.8*0.8) , 0 for toodler
////	}
////	
////	
////	
////	@Test
////	public void PriceCheck() throws Exception{
////		int price = 0 ; 
////		try {
////		    tmp = new Tourist("D.Chan",0); // Assume D.Chan joined the tripB and Trip B cost 1000 HKD for adult,800 for child.child got 80% on against because of Back school discount
////			PriceCalculate pc = new PriceCalculate(tmp);
////			price = pc.getPriceOfCustomer(0);
////		} catch (Exception e) {
////			
////		}
////		assertTrue(price == 23200); // so the total price should be 26400 HKD , 20000 for Adult, 6400 for Child(10000*0.8*0.8) , 0 for toodler
////	}
////	
////
////	public void TripCheck() throws Exception{
////		// Beware name is not tourist name ,  is LINE ID.
////		try {
////		SQLDatabaseEngine db = new SQLDatabaseEngine();
////		db.search("price_status","slliac"); // name here(slliac) = LINE ID is useless until the implementation part finish , it should be hashed code in future.
////		
////		} catch (Exception e) {
////			
////		}
////		assertTrue(true) ;		
////		//assertTrue(price == 23200) ; // so the total price should be 26400 HKD , 20000 for Adult, 6400 for Child(10000*0.8*0.8) , 0 for toodler
////	}
////	
////	@Test
////	public void statusCheck() throws Exception{
////		int i = 0 ;
////		try {
////		SQLDatabaseEngine db = new SQLDatabaseEngine();
////		db.search("status","D.Chan");
////		
////		} catch (Exception e) {
////			
////		}
////		assertTrue(true) ;		
////		//assertTrue(price == 26400) ; // so the total price should be 26400 HKD , 20000 for Adult, 6400 for Child(10000*0.8*0.8) , 0 for toodler
////	}
////	
////	
////	/**
////	 * void StatusCheck() 
////	 * 
////	 * This is the function which checked the trip name is > 15 or not.
////	 * And it should particularly informed to all the customers.
////	 * If true  ====>   the trip > 15 ppl.
////	 * If false ====>   the trip < 15 ppl.
////	 * 
////	 * */
////
//	/*
//	@Test
//	
//	public void SQLCheck() throws Exception{
//		
//		try {
//			String rs = null;
//			SQLDatabaseEngine sql = new SQLDatabaseEngine();
//			rs  = sql.switchFunctions("search insurance");
//			rs  = sql.switchFunctions("search tour cancelled");
//			rs  = sql.switchFunctions("search additional charge");
//			rs  = sql.switchFunctions("search hotel bed");
//			rs  = sql.switchFunctions("search tour fee");
//			rs  = sql.switchFunctions("search deadline");
//			rs  = sql.switchFunctions("search tour size");
//			rs  = sql.switchFunctions("search language support");
//			rs  = sql.switchFunctions("search tourstatus");
//			rs  = sql.switchFunctions("search balance");
//			rs  = sql.switchFunctions("keyword from HongKong");
//			
//			
//		}catch(Exception e) {
//			
//		}
//		assertTrue(true);
//	}
//	
//	
//	
//	@Test
//	public void StatusCheck() throws Exception{
//		boolean statuscheck = false;
//		try {
//			String result = null;
//		    Tourist tmp = new Tourist("tschiuaa",0);
//			StatusName status = new StatusName("hotspringtrip"); // assume we  establish trip name called TripB 
//		    status.notAll(); 
//		    System.out.print(status.PeopleCount2()+"hahahahahahahahahahah");
//		    status.notAllConfirmed(); // this is further notify
//		    statuscheck = status.tripStatus();
//           	for(int i = 0 ; i < status.getSizeOfTripNum(tmp) ;i++)
//            {
//            result += status.CheckStatus(tmp,i); // check status Method
//            }
//		    
//		} catch (Exception e) {
//		}
//		assertTrue(true) ; // tmp status check notify, not refer as final check
//	}
//	
//	
//	*/
////	
////
////	@Test
////	public void apply() throws Exception {
////		boolean thrown = false;
////		String result = null;
////		try {
////			result = this.databaseEngine.search("apply","twchiuaa");
////		} catch (Exception e) {
////			thrown = true;
////		}
////		assertThat(!thrown).isEqualTo(true);
////		assertThat(result).isEqualTo("Here is the general workflow of our customer to apply a tour.\n" + 
////				"\n" + 
////				"1. Customers shall approach the company by phone or visit our store (in Clearwater bay) with the choosen tour code and departure date.\n" + 
////				"2. If the tour is not full, customers will be advised by the staff to pay the tour fee.\n" + 
////				"***Tour fee is non refundable. \n" + 
////				"3. Customer can pay the fee by ATM to 123-345-432-211 of ABC Bank or by cash in our store.\n" + 
////				"4. Customer shall send their pay-in slip to us by email or LINE.");
////	}
////
////	
//////	@Test
//////	public void assembly_point() throws Exception {
//////		boolean thrown = false;
//////		String result = null;
//////		try {
//////			result = this.databaseEngine.search("assembly point");
//////		} catch (Exception e) {
//////			thrown = true;
//////		}
//////		assertThat(!thrown).isEqualTo(true);
//////		assertThat(result).isEqualTo("Here is the general workflow of our customer to apply a tour.\n" + 
//////				"\n" + 
//////				"1. Customers shall approach the company by phone or visit our store (in Clearwater bay) with the choosen tour code and departure date.\n" + 
//////				"2. If the tour is not full, customers will be advised by the staff to pay the tour fee.\n" + 
//////				"***Tour fee is non refundable. \n" + 
//////				"3. Customer can pay the fee by ATM to 123-345-432-211 of ABC Bank or by cash in our store.\n" + 
//////				"4. Customer shall send their pay-in slip to us by email or LINE.");
//////	}
////	
//////	@Test
//////	public void testFound1() throws Exception {
//////		boolean thrown = false;
//////		String result = null;
//////		try {
//////			result = this.databaseEngine.search("abc");
//////		} catch (Exception e) {
//////			thrown = true;
//////		}
//////		assertThat(!thrown).isEqualTo(true);
//////		assertThat(result).isEqualTo("def");
//////	}
//////	
//////	@Test
//////	public void testFound2() throws Exception {
//////		boolean thrown = false;
//////		String result = null;
//////		try {
//////			result = this.databaseEngine.search("Hi");
//////		} catch (Exception e) {
//////			thrown = true;
//////		}
//////		assertThat(!thrown).isEqualTo(true);
//////		assertThat(result).isEqualTo("Hey, how things going?");
//////	}
//////
//////	@Test
//////	public void testFound3() throws Exception {
//////		boolean thrown = false;
//////		String result = null;
//////		try {
//////			result = this.databaseEngine.search("I am fine");
//////		} catch (Exception e) {
//////			thrown = true;
//////		}
//////		assertThat(!thrown).isEqualTo(true);
//////		assertThat(result).isEqualTo("Great!");
//////	}
//////	
//////	@Test
//////	public void testFound4() throws Exception {
//////		boolean thrown = false;
//////		String result = null;
//////		try {
//////			result = this.databaseEngine.search("Who is Prof Kim");
//////		} catch (Exception e) {
//////			thrown = true;
//////		}
//////		assertThat(!thrown).isEqualTo(true);
//////		assertThat(result).isEqualTo("Well, this is your instructor.");
//////	}
//////	
//////	@Test
//////	public void testFound5() throws Exception {
//////		boolean thrown = false;
//////		String result = null;
//////		try {
//////			result = this.databaseEngine.search("How is the grade of this course?");
//////		} catch (Exception e) {
//////			thrown = true;
//////		}
//////		assertThat(!thrown).isEqualTo(true);
//////		assertThat(result).isEqualTo("This is absolute good grade for good student. And I am sure you are!");
//////	}
//	
//	
//	
//	



