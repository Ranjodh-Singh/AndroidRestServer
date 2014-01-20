package org.columbia.assignment.android.service;

import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;



@Path("/rdsobjectretrieve")
public class RDSEntityFriendsListService {
	MongoClient mongoClient;
	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String _ID = "_id";
	public static final String USERNAME = "username";
	public static final String OAUTHPROVIDER = "oauthprovider";
	public static final String LOCATION_LONG = "location_long";
	public static final String LOCATION_LAT = "location_lat";
	
	public RDSEntityFriendsListService(){

		try {
			mongoClient	 = new MongoClient( "ds059898.mongolab.com" , 59898 );
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("--------Unable to connect to mongoDB Server-----------");
		}
	
	}
//this method will take the list of phone numbers and find the corresponding registered users in the db. 
//and return that created list of USerDBObject to our application.
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFriends(PhoneNumbersEntity phoneNumbersEntity){
		System.out.println("phone number entity size :::"+phoneNumbersEntity.getPhoneNumbers().size());
		FriendListEntity friendListEntity = mapPhoneNumbersWithRegUsersFromDB(phoneNumbersEntity.getPhoneNumbers());		
		System.out.println("RDSEntityFriendsListService:"+friendListEntity.toString());
		
		return Response.status(200).entity(friendListEntity).build();
	}

	private FriendListEntity mapPhoneNumbersWithRegUsersFromDB(
		List<String> phoneNumbers) {
	
	FriendListEntity friendListEntity = new FriendListEntity();
	System.out.println("Inside the mapPhoneNumbersWithRegUsersFromDB function. ");
 	DB dataBase = mongoClient.getDB("android_connect");
 	boolean auth = dataBase.authenticate("na389", "na12345".toCharArray());
 	if(!auth){
 		System.out.println("Unable to authenticate MongoDB user name and password, please check your credentials. ");
 	}
 	DBCollection  dBCollection = dataBase.getCollection("users");
 	QueryBuilder query = new QueryBuilder();
 	query.put(PHONE_NUMBER);
 	query.in(phoneNumbers);
 	DBObject dBObject = query.get();
 	DBCursor cursor = dBCollection.find(dBObject);
 	BasicDBObject basicOldDBObject = new BasicDBObject();
 	
 	try {
	 	   while(cursor.hasNext()) {
	 		  basicOldDBObject = (BasicDBObject) cursor.next();
	 		 UserDBObject userdbObj = new UserDBObject();
	 		   basicDBObjectTOUserDBObjectConverter(basicOldDBObject,userdbObj);
	 		  friendListEntity.friendList.add(userdbObj);
	 	       System.out.println("user added to the friend list based on the phone number : "+userdbObj.toString());
	 	   }
	 	} finally {
	 	   cursor.close();
	 	}
 	
 	//DBCursor cursor = dBCollection.;
 //	DBObject basicOldDBObject = new DBObject(query);
 	//['phoneNumber':['$in':['e','b']]]
	
	return friendListEntity;
}
/**
 * this function can be used as utility to convert BasicDBOject into UserDBObject.
 * @param basicOldDBObject
 * @param userdbObj
 */
	public static void  basicDBObjectTOUserDBObjectConverter(
			BasicDBObject basicOldDBObject, UserDBObject userdbObj) {
		userdbObj.set_id(basicOldDBObject.getString(_ID));
		userdbObj.setUsername(basicOldDBObject.getString(USERNAME));
		userdbObj.setPhoneNumber(basicOldDBObject.getString(PHONE_NUMBER));
		userdbObj.setOauthprovider(basicOldDBObject.getString(OAUTHPROVIDER));
		userdbObj.setLocation_long(basicOldDBObject.getString(LOCATION_LONG));
		userdbObj.setLocation_lat(basicOldDBObject.getString(LOCATION_LAT));
		
	}
	private FriendListEntity mapPhoneNumbersWithRegUsers(List<String> phoneNumbers) {
		Connection connect = null;
		FriendListEntity friendListEntity = new FriendListEntity();
		try {
			System.out.println("mapPhoneNumbersWithRegUsers");
			
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://mydbinstance.c1b4bf3mouew.us-east-1.rds.amazonaws.com:3306/mydb";
			connect = (Connection) DriverManager.getConnection(url,"myuser", "mypassword");
			Statement stmnt = (Statement) connect.createStatement();
			ResultSet rst = stmnt.executeQuery("SELECT username,phonenumber,location_lat,location_long FROM users");
			while(rst.next()){
				System.out.println("RSTTTTTTTTTTTTTT:"+rst.getString("username"));
				if(phoneNumbers.contains(rst.getString("phonenumber"))){
					System.out.println("Matched phone number:"+rst.getString("phonenumber"));
					UserDBObject userdbObj = new UserDBObject();
					userdbObj.setUsername(rst.getString("username"));
					userdbObj.setPhoneNumber(rst.getString("phonenumber"));
					userdbObj.setLocation_lat(rst.getString("location_lat"));
					userdbObj.setLocation_long(rst.getString("location_long"));					
					friendListEntity.friendList.add(userdbObj);
				}
			}
			
			return friendListEntity;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			
		} finally {
			try {
				if(connect != null)
					connect.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
}
