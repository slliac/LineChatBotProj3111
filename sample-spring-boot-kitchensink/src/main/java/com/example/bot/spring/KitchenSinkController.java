/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import com.linecorp.bot.model.profile.UserProfileResponse;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.AudioMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@LineMessageHandler
public class KitchenSinkController {

	@Autowired
	public LineMessagingClient lineMessagingClient;
    public BotApiResponse apiResponse2;
    //FEATURE 5
    private String[] cacheForBooking;
    private boolean waitingForResponse = false;
    public static final int WAIT_FOR_NAME = 1;
    public static final int WAIT_FOR_NUMA = 2;
    public static final int WAIT_FOR_NUMC = 3;
    public static final int WAIT_FOR_NUMT = 4;
    
	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		log.info("This is your entry point:");
		log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		TextMessageContent message = event.getMessage();
		handleTextContent(event.getReplyToken(), event, message);
	}

	@EventMapping
	public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
		handleSticker(event.getReplyToken(), event.getMessage());
	}

	@EventMapping
	public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
		LocationMessageContent locationMessage = event.getMessage();
		reply(event.getReplyToken(), new LocationMessage(locationMessage.getTitle(), locationMessage.getAddress(),
				locationMessage.getLatitude(), locationMessage.getLongitude()));
	}

	@EventMapping
	public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
		final MessageContentResponse response;
		String replyToken = event.getReplyToken();
		String messageId = event.getMessage().getId();
		try {
			response = lineMessagingClient.getMessageContent(messageId).get();
		} catch (InterruptedException | ExecutionException e) {
			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
			throw new RuntimeException(e);
		}
		DownloadedContent jpg = saveContent("jpg", response);
		reply(((MessageEvent) event).getReplyToken(), new ImageMessage(jpg.getUri(), jpg.getUri()));

	}

	@EventMapping
	public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) throws IOException {
		final MessageContentResponse response;
		String replyToken = event.getReplyToken();
		String messageId = event.getMessage().getId();
		try {
			response = lineMessagingClient.getMessageContent(messageId).get();
		} catch (InterruptedException | ExecutionException e) {
			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
			throw new RuntimeException(e);
		}
		DownloadedContent mp4 = saveContent("mp4", response);
		reply(event.getReplyToken(), new AudioMessage(mp4.getUri(), 100));
	}

	@EventMapping
	public void handleUnfollowEvent(UnfollowEvent event) {
		log.info("unfollowed this bot: {}", event);
	}

	@EventMapping
	public void handleFollowEvent(FollowEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Got followed event");
	}

	/**
	 * when the user add the LINE chatbot into the group chatroom, 
	 * or other events that trigger the join event, the greeting message will be shown
	 * 
	 * @author Louis, spcheungaa
	 * @param event		a current join event instance triggered by the user
	 */
	@EventMapping
	public void handleJoinEvent(JoinEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Welcome to Trip ChatBot");
	}

	/**
	 * when the user press the "Get more information" after searching with "search assembly point", 
	 * or other events that trigger the postback event, the description of the picture will be shown
	 * 
	 * @author Louis, spcheungaa
	 * @param event		a current postback event instance triggered by the user
	 */
	@EventMapping
	public void handlePostbackEvent(PostbackEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, event.getPostbackContent().getData());
	}

	@EventMapping
	public void handleBeaconEvent(BeaconEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Got beacon message " + event.getBeacon().getHwid());
	}

	@EventMapping
	public void handleOtherEvent(Event event) {
		log.info("Received message(Ignored): {}", event);
	}

	private void reply(@NonNull String replyToken, @NonNull Message message) {
		reply(replyToken, Collections.singletonList(message));
	}

	public void post(@NonNull String to, @NonNull Message message) {
		postmessage(to, Collections.singletonList(message));
	}
	
	public void Mpost(@NonNull String [] to, @NonNull Message message) {
		Mpostmessage(to, Collections.singletonList(message));
	}
	
	private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
			log.info("Sent messages: {}", apiResponse);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	

	public void postmessage(@NonNull String to, @NonNull List<Message> messages) {
		try {
    		    apiResponse2 = lineMessagingClient.pushMessage(new PushMessage(to, messages)).get();
			log.info("Sent messages: {}", apiResponse2);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	

	public void Mpostmessage(@NonNull String [] to, @NonNull List<Message> messages) {
		try {
			Set<String> strSet = Arrays.stream(to).collect(Collectors.toSet());
    		    apiResponse2 = lineMessagingClient.multicast(new Multicast(strSet, messages)).get();
			log.info("Sent messages: {}", apiResponse2);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	

	public void postText(@NonNull String to,@NonNull String message) {
		if (message.length() > 1000) {
			message = message.substring(0, 1000 - 2) + "..";
		}
		this.post(to, new TextMessage(message));
	}
	
	public void MpostText(@NonNull String [] to,@NonNull String message) {
		if (message.length() > 1000) {
			message = message.substring(0, 1000 - 2) + "..";
		}
		this.Mpost(to, new TextMessage(message));
	}
	
	private void replyText(@NonNull String replyToken, @NonNull String message) {
		if (replyToken.isEmpty()) {
			throw new IllegalArgumentException("replyToken must not be empty");
		}
		if (message.length() > 10000) {
			message = message.substring(0, 10000 - 2) + "..";
		}
		this.reply(replyToken, new TextMessage(message));
	}


	private void handleSticker(String replyToken, StickerMessageContent content) {
		reply(replyToken, new StickerMessage(content.getPackageId(), content.getStickerId()));
	}

	public void handleTextContent(String replyToken, Event event, TextMessageContent content)
            throws Exception {
        String text = content.getText();

        log.info("Got text message from {}: {}", replyToken, text);
        
        
        if (waitingForResponse == true) {
    		
    		String userId = event.getSource().getUserId();
    		
    		switch (database.getConversationStatus(userId))
    		{
    		case WAIT_FOR_NAME: {
    			database.updateClientName(userId, text);
    			cacheForBooking[2] = text;
    			
				String askNumAdult = "How many adult?";
				this.replyText(replyToken, askNumAdult);
				waitingForResponse = true;
				database.setConversationStatus(userId, WAIT_FOR_NUMA);
    			break;
    		}
    		case WAIT_FOR_NUMA: {
    			cacheForBooking[3] = text;
    			
    			String askNumChildren = "How many children?";
    			this.replyText(replyToken, askNumChildren);
    			waitingForResponse = true;
    			database.setConversationStatus(userId, WAIT_FOR_NUMC);
    			break;
    		}
    		case WAIT_FOR_NUMC: {
    			cacheForBooking[4] = text;
    			
    			String askNumToddler = "How many toddler?";
    			this.replyText(replyToken, askNumToddler);
    			waitingForResponse = true;
    			database.setConversationStatus(userId, WAIT_FOR_NUMT);
    			break;
    		}
    		case WAIT_FOR_NUMT: {
    			cacheForBooking[5] = text;
    			
    			confirmBooking(replyToken);
    			waitingForResponse = false;
    			database.setConversationStatus(userId, 0);
    			break;
    		}
    		default:
        		waitingForResponse = false;
    			break;
    		}
    		
    		return;
    }
        switch (text) {
	        
//        	case "hi":
//        	case "hey":
//        	case "hello":
//        	{
//        		
//        		break;
//        	}
        
        	case "profile": {
                String userId = event.getSource().getUserId();
                if (userId != null) {
                    lineMessagingClient
                            .getProfile(userId)
                            .whenComplete(new ProfileGetter (this, replyToken));
                } else {
                    this.replyText(replyToken, "Bot can't use profile API without user ID");
                }
                break;
            }
            case "confirm": {
                ConfirmTemplate confirmTemplate = new ConfirmTemplate(
                        "Do it?",
                        new MessageAction("Yes", "Yes!"),
                        new MessageAction("No", "No!")
                );
                TemplateMessage templateMessage = new TemplateMessage("Confirm alt text", confirmTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "adminpromo": {
        		try {
            		List<String> clientList = database.getClientList();
            		
            		for(int i =0; i < clientList.size(); i++)
            		{
            			try {
            				this.postText(clientList.get(i), database.getPromotionMessage());
            			}catch(Exception e) {
            				continue;
            			}
            		}
            				
            		
            		this.replyText(replyToken, "promotion messages are successfully sent to all client!");
            
        		}catch(Exception e) {
        			this.replyText(replyToken, "Error, there isn't any clients in database. Please contact line chatbot developers");
        		}
        		break;

        }
            
            case "adminupdate":{
            	 StatusName status = new StatusName("tripB");
                 status.notAllConfirmed();
                 List<Tourist> touristlist = status.getConfirmedTourist();
                 List<String>  sizestring  = status.getStringSize();
                 List<String>  failstring  = status.getFailSize();
                 List<String>  nmc         = status.getNEConfirmed();
                 List<String>  nmcc         = status.getNEConfirmedN();
                 //enough number of tourmates
                 for(int i = 0; i < sizestring.size() ; i++)
                 this.postText(event.getSource().getUserId(), "Hi,"+sizestring.get(i)+",I am going to updated your trip status : " +"Success. Your team 's your guide is Mr. Sung. He 'll contact you at soon. Have a nice trip ^^ ");
                 for(int i = 0 ; i < failstring.size() ; i++) {
                 this.postText(event.getSource().getUserId(), "Hi,"+failstring.get(i)+",I am going to updated your trip status : \n" +"We 're fail to offer you a trip due to unsufficient trip fee you pay. \n" + " You have 2 choices for your trip.\n 1) Pay tour fee before today 6:00 pm \n 2) Cancel tour booking. \n Please contact us at soon.");
                 }
                 //failure number of tourmates
                 for(int i = 0 ; i < nmc.size() ; i++)
                	 this.postText(event.getSource().getUserId(), "Hi,"+nmc.get(i)+",I am going to updated your trip status : " +"Failure since not enough number of person which give full fee join the trip");
                 for(int i = 0 ; i < nmcc.size() ; i++)
                	 this.postText(event.getSource().getUserId(), "Hi,"+nmcc.get(i)+",I am going to updated your trip status : " +"Failure since not enough number of person which give full fee join the trip and also you didn 't give sufficient money for trip.");
                 
            	break;
            }
            case "carousel": {
                String imageUrl = createUri("/static/buttons/1040.jpg");
                CarouselTemplate carouselTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                        new URIAction("Go to line.me",
                                                      "https://line.me"),
                                        new PostbackAction("Say hello1",
                                                           "hello 嚙踝蕭��嚙踐����蕭��������蕭��嚙踝蕭謕蕭�摰�郭嚙踐�蕭�蝮�郭嚙踐�蕭��")
                                )),
                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                        new PostbackAction("嚙踐���頩蕭 hello2",
                                                           "hello 嚙踝蕭��嚙踐����蕭��������蕭��嚙踝蕭謕蕭�摰�郭嚙踐�蕭�蝮�郭嚙踐�蕭��",
                                                           "hello 嚙踝蕭��嚙踐����蕭��������蕭��嚙踝蕭謕蕭�摰�郭嚙踐�蕭�蝮�郭嚙踐�蕭��"),
                                        new MessageAction("Say message",
                                                          "Rice=嚙踝蕭賹��蕭")
                                ))
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            
            /*
             * Feature 2a: FAQ Section
             * - The gathering point shall be shown to the client, presented by a picture and description.
             */
            case "search assembly point": {
                String imageUrl = createUri("/static/gather.jpg");
                CarouselTemplate carouselTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrl, "Assembly Point", " ", Arrays.asList(
                                        new PostbackAction("Get more information", database.search(text,"twchiuaa"))
                                ))
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "command: confirmed": {
            		if(cacheForBooking != null)
            		{
            			String userId = event.getSource().getUserId();
                		String responseFromDB = database.booking(cacheForBooking, userId);
                		//responseFromDB += "You should paid your price before 3 days the trip established.";
                		cacheForBooking = null;
                		this.replyText(replyToken, responseFromDB);
            		}
            		else
            		{
            			this.replyText(replyToken, "You have already booked that tour!");
            		}

            		break;
            }
            case "command: cancelled": {
            		if(cacheForBooking != null)
            		{
            			cacheForBooking = null;
                		this.replyText(replyToken, "Booking Cancelled");
            		}
            		else
            		{
            			this.replyText(replyToken, "You have already cancelled the booking!");
            		}

            		break;
            }

            default:
            	
            	//Run API function
           	 String[] parts = text.split(":");
           	 if(parts[0].equals("adminupdate") ) {
           	 StatusName status = new StatusName(parts[1]);
                status.notAllConfirmed();
                List<Tourist> touristlist = status.getConfirmedTourist();
                List<String>  sizestring  = status.getStringSize();
                List<String>  failstring  = status.getFailSize();
                List<String>  nmc         = status.getNEConfirmed();
                List<String>  nmcc        = status.getNEConfirmedN();
                List<String>  ccid        = status.getccid();
                List<String>  testid      = status.getestid();
                List<String>  testid2      = status.getestid2();
                List<String>  ccidn       = status.getccidn();
                List<String>  nccid       = status.getnccid();
                List<String>  nccidn      = status.getnccidn();
                //enough number of tourmates
                //"U53250c5aaa0ebc147a426e384e5d751c"
                
                String[] idget = null;
                String [] idget2  = null;
                String [] idget3  = null;
                String [] idget4 = null;
                idget = testid.toArray(new String[testid.size()]);
                idget2  =  testid2.toArray(new String[testid2.size()]);
                idget3  =  nccid.toArray(new String[nccid.size()]);
                idget4  =  nccid.toArray(new String[nccidn.size()]);
                
                this.MpostText(idget, "Hi,I am going to updated your trip status : " +"Success. Your team 's your guide is Mr. Sung. He 'll contact you at soon. Have a nice trip ^^ ");
                this.MpostText(idget2, "Hi, I am going to updated your trip statusI am going to updated your trip status : We 're fail to offer you a trip due to unsufficient trip fee you pay. \n You have 2 choices for your trip.\n 1) Pay tour fee before today 6:00 pm \n 2) Cancel tour booking. \n Please contact us at soon.");
                this.MpostText(idget3, "Hi,I am going to updated your trip status : Failure since not enough number of person which give full fee join the trip");
                this.MpostText(idget4, "Hi,I am goign to updated your trip status : ailure since not enough number of person which give full fee join the trip and also you didn 't give sufficient money for trip.");

                
                //for(int i = 0; i < sizestring.size() ; i++)
                //this.postText(ccid.get(i), "Hi,"+sizestring.get(i)+",I am going to updated your trip status : " +"Success. Your team 's your guide is Mr. Sung. He 'll contact you at soon. Have a nice trip ^^ ");
                //for(int i = 0 ; i < failstring.size() ; i++) {
                //this.postText(ccid.get(i), "Hi,"+failstring.get(i)+",I am going to updated your trip status : \n" +"We 're fail to offer you a trip due to unsufficient trip fee you pay. \n" + " You have 2 choices for your trip.\n 1) Pay tour fee before today 6:00 pm \n 2) Cancel tour booking. \n Please contact us at soon.");
                //}
                //failure number of tourmates
                //for(int i = 0 ; i < nmc.size() ; i++)
                //this.postText(ccid.get(i), "Hi,"+nmc.get(i)+",I am going to updated your trip status : " +"Failure since not enough number of person which give full fee join the trip");
                //for(int i = 0 ; i < nmcc.size() ; i++)
               //this.postText(ccid.get(i), "Hi,"+nmcc.get(i)+",I am going to updated your trip status : " +"Failure since not enough number of person which give full fee join the trip and also you didn 't give sufficient money for trip.");
           	 } else {
            	String reply = null;       	
            	try {
            		
            		
            		//Feature 5: collect user info when booking tour
            		if(text.contains("book"))
            		{
            			cacheForBooking = new String[6];
            			String[] bookingInfo = text.split(":");
            			cacheForBooking[1] = bookingInfo[1];
            			
            			String userId = event.getSource().getUserId();
            			
            			if(database.checkClient(userId)) {
            				cacheForBooking[2] = database.getClientName(userId);
            				
            				String askNumAdult = "How many adult?";
            				this.replyText(replyToken, askNumAdult);
            				waitingForResponse = true;
            				database.setConversationStatus(userId, WAIT_FOR_NUMA);
            			}
            			else
            			{
            				database.addClient(userId);
            				
            				String newCustomerGreeting = "Hello, new customer. What should I call you?";
            				this.replyText(replyToken, newCustomerGreeting);
            				waitingForResponse = true;
            				database.setConversationStatus(userId, WAIT_FOR_NAME);
            			}
            		}
            		if(text.contains("location")) {
            			String[] locationInfo = text.split(":");
            			
            			ArrayList<String> list = database.showLocation(locationInfo[1]);
            			
            			//replyText(replyToken, list.get(0));
            			
            			reply(replyToken, new LocationMessage("Location of travel: " + locationInfo[1], "Please click on this message to see the location: " + list.get(0), Double.parseDouble(list.get(1)), Double.parseDouble(list.get(2))));          			
            		}

            		
            		reply = database.switchFunctions(text,event.getSource().getUserId());
//            		reply = database.searchByKeywords(text);
//            		reply = database.search(text,event.getSource().getUserId());
            		
            		//Feature 1: Dynamic Greeting
            		if ((reply.equals("Sorry, we don't have answer for this."))&&
                			(text.toLowerCase().contains("hi")||text.toLowerCase().contains("hey")||text.toLowerCase().contains("hello")))
                		{
                			String userId = event.getSource().getUserId();
                			reply = database.dynamicGreeting(userId);
                		}
            		
            	} catch (Exception e) {
            		reply = text;
            	}
                log.info("Returns echo message {}: {}", replyToken, reply);
                this.replyText(
                        replyToken,
//                        itscLOGIN + " says " + reply
                		reply
                );
                // PUSH Message API and use
           	 }
                break;
                
        }
    }
	
	/**
	 * Feature 5: This is a function that use a confirmation message to ask for the confirmation from clients
	 * @param replyToken is a string token that is used to identify the session of the reply functions referring to
	 * @return a string containing the status of booking, whether it is success or not and it will be used to inform the client for the result
	 * @throws Exception
	 */
	private String confirmBooking(String replyToken) throws Exception {
		String result = null;
		
		try {
			String displayString = "";
			
			if(cacheForBooking.length != 6)
			{
				result = "Error Occur: Booking Information not correct. Please retry or contact our staff";
				this.replyText(replyToken, result);
			}
		
			if(database.checkTour(cacheForBooking[1]))
			{
				displayString += "Please confirm the following booking: \n";
				displayString += "Tour Name: " + cacheForBooking[1] + "\n";
				displayString += "Customer Name: " + cacheForBooking[2] + "\n";
				displayString += "Number Of Adult: " + cacheForBooking[3] + "\n";
				displayString += "Number Of Child: " + cacheForBooking[4] + "\n";
				displayString += "Number Of Toodler: " + cacheForBooking[5] + "\n";
			
				ConfirmTemplate confirmTemplate = new ConfirmTemplate(
						displayString,
						new MessageAction("Yes", "command: confirmed"),
						new MessageAction("No", "command: cancelled")
						);
				TemplateMessage templateMessage = new TemplateMessage("Confirm alt text", confirmTemplate);
				
				this.reply(replyToken, templateMessage);
			}
			else
			{
				result = "There is no tour named: " + cacheForBooking[1];
				this.replyText(replyToken, result);
			}

        //result = templateMessage.toString();
        
			return result;
		}
		catch (Exception e) {
			return result = "can not recognize the command";
     	}
	}

	private Connection getConnection() throws URISyntaxException, SQLException {
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
	
	
	static String createUri(String path) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUriString();
	}

	private void system(String... args) {
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		try {
			Process start = processBuilder.start();
			int i = start.waitFor();
			log.info("result: {} =>  {}", Arrays.toString(args), i);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (InterruptedException e) {
			log.info("Interrupted", e);
			Thread.currentThread().interrupt();
		}
	}

	public void hehe() {
		System.out.print(123);
	}
	
	private static DownloadedContent saveContent(String ext, MessageContentResponse responseBody) {
		log.info("Got content-type: {}", responseBody);

		DownloadedContent tempFile = createTempFile(ext);
		try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
			ByteStreams.copy(responseBody.getStream(), outputStream);
			log.info("Saved {}: {}", ext, tempFile);
			return tempFile;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static DownloadedContent createTempFile(String ext) {
		String fileName = LocalDateTime.now().toString() + '-' + UUID.randomUUID().toString() + '.' + ext;
		Path tempFile = KitchenSinkApplication.downloadedContentDir.resolve(fileName);
		tempFile.toFile().deleteOnExit();
		return new DownloadedContent(tempFile, createUri("/downloaded/" + tempFile.getFileName()));
	}


	public KitchenSinkController() {
		database = new SQLDatabaseEngine();
//		database = new SQLDatabaseEngine();

		itscLOGIN = System.getenv("ITSC_LOGIN");
	}

	private SQLDatabaseEngine database;
	private String itscLOGIN;
	

	//The annontation @Value is from the package lombok.Value
	//Basically what it does is to generate constructor and getter for the class below
	//See https://projectlombok.org/features/Value
	@Value
	public static class DownloadedContent {
		Path path;
		String uri;
	}


	//an inner class that gets the user profile and status message
	class ProfileGetter implements BiConsumer<UserProfileResponse, Throwable> {
		private KitchenSinkController ksc;
		private String replyToken;
		
		public ProfileGetter(KitchenSinkController ksc, String replyToken) {
			this.ksc = ksc;
			this.replyToken = replyToken;
		}
		@Override
    	public void accept(UserProfileResponse profile, Throwable throwable) {
    		if (throwable != null) {
            	ksc.replyText(replyToken, throwable.getMessage());
            	return;
        	}
        	ksc.reply(
                	replyToken,
                	Arrays.asList(new TextMessage(
                		"Display name: " + profile.getDisplayName()),
                              	new TextMessage("Status message: "
                            		  + profile.getStatusMessage()))
        	);
    	}
		

		
		
    }
	
	

}
