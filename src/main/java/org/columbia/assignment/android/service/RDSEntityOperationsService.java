package org.columbia.assignment.android.service;

import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.BSON;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;




@Path("/rdsobjectupdate")
public class RDSEntityOperationsService {
	private Connection connect = null;
	private Statement statement = null;
	MongoClient mongoClient;
	public RDSEntityOperationsService(){
		try {
			mongoClient	 = new MongoClient( "ds059898.mongolab.com" , 59898 );
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("--------Unable to connect to mongoDB Server-----------");
		}
	}
	
	private int max = 0;
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response getEntityRequest(UserDBObject userDBObject){
				
		System.out.println("Hello post call");
			if(userDBObject.getOperationType().equals("INSERT")){
				//insertUserRegDetails(userDBObject);
				insertUserRegistrationDetails(userDBObject);
			}else if (userDBObject.getOperationType().equals("SELECT")){
				//getUserDetails(userDBObject);
				getUserDetailsFromDB(userDBObject);
			}else if (userDBObject.getOperationType().equals("UPDATE")){
				//updateUserDetails(userDBObject);
				updateUserDetailsFromDB(userDBObject);
			}
		 return Response.status(200).entity(userDBObject).build();
	 }

	
	private void updateUserDetailsFromDB(UserDBObject userDBObject) {
		System.out.println("Inside the updateUserDetailsFromDB function. ");
	 	DB dataBase = mongoClient.getDB("android_connect");
	 	boolean auth = dataBase.authenticate("na389", "na12345".toCharArray());
	 	DBCollection  dBCollection = dataBase.getCollection("users");
	 	BasicDBObject basicOldDBObject = new BasicDBObject();
	 	basicOldDBObject.put("_id", userDBObject.get_id());
	 	basicOldDBObject.put("username", userDBObject.getUsername());
	 	DBCursor cursor = dBCollection.find(basicOldDBObject);

	 	try {
	 	   while(cursor.hasNext()) {
	 		  basicOldDBObject = (BasicDBObject) cursor.next();
	 	       System.out.println(basicOldDBObject.toString());
	 	   }
	 	} finally {
	 	   cursor.close();
	 	}
	 	BasicDBObject basicDBObject = new BasicDBObject();
	 	basicDBObject.put("_id", userDBObject.get_id());
	 	basicDBObject.put("username", userDBObject.getUsername());
	 	basicDBObject.put("oauthprovider",userDBObject.getOauthprovider());
	 	basicDBObject.put("location_long", userDBObject.getLocation_long());
	 	basicDBObject.put("location_lat", userDBObject.getLocation_lat());
	 	basicDBObject.put("phoneNumber", userDBObject.getPhoneNumber());
	 	
	 	dBCollection.update(basicOldDBObject,basicDBObject);
		
	}


	private void getUserDetailsFromDB(UserDBObject userDBObject) {
		System.out.println("Inside the getUserDetailsFromDB function. ");
	 	DB dataBase = mongoClient.getDB("android_connect");
	 	boolean auth = dataBase.authenticate("na389", "na12345".toCharArray());
	 	DBCollection  dBCollection = dataBase.getCollection("users");
	 	BasicDBObject basicDBObject = new BasicDBObject();
	 	basicDBObject.put("_id", userDBObject.get_id());
	 	basicDBObject.put("username", userDBObject.getUsername());
	 	 DBCursor cursor = dBCollection.find(basicDBObject);

	 	try {
	 	   while(cursor.hasNext()) {
	 		  basicDBObject = (BasicDBObject) cursor.next();
	 	       System.out.println(basicDBObject.toString());
	 	   }
	 	} finally {
	 	   cursor.close();
	 	}
	 	/*basicDBObject.put("oauthprovider",userDBObject.getOauthprovider());
	 	basicDBObject.put("location_long", userDBObject.getLocation_long());
	 	basicDBObject.put("location_lat", userDBObject.getLocation_lat());
	 	basicDBObject.put("phoneNumber", userDBObject.getPhoneNumber());*/
	 	if(basicDBObject.getString("location_lat") != null)
	 	userDBObject.setLocation_lat(basicDBObject.getString("location_lat"));
	 	if(basicDBObject.getString("oauthprovider") != null)
	 	userDBObject.setOauthprovider(basicDBObject.getString("oauthprovider"));
	 	if(basicDBObject.getString("location_long") != null)
	 	userDBObject.setLocation_long(basicDBObject.getString("location_long"));
	 	if(basicDBObject.getString("phoneNumber") != null)
	 	userDBObject.setPhoneNumber(basicDBObject.getString("phoneNumber"));
	 	/*dBCollection.insert(basicDBObject);*/
		
	}


	private void insertUserRegistrationDetails(UserDBObject userDBObject) {
		
		 	System.out.println("Inside the insertUserRegistrationDetails function. ");
		 	DB dataBase = mongoClient.getDB("android_connect");
		 	boolean auth = dataBase.authenticate("na389", "na12345".toCharArray());
		 	DBCollection  dBCollection = dataBase.getCollection("users");
		 	BasicDBObject basicDBObject = new BasicDBObject();
		 	basicDBObject.put("_id", userDBObject.get_id());
		 	basicDBObject.put("username", userDBObject.getUsername());
		 	basicDBObject.put("oauthprovider",userDBObject.getOauthprovider());
		 	basicDBObject.put("location_long", userDBObject.getLocation_long());
		 	basicDBObject.put("location_lat", userDBObject.getLocation_lat());
		 	basicDBObject.put("phoneNumber", userDBObject.getPhoneNumber());
		 	
		 	dBCollection.insert(basicDBObject);
		 	//dBCollection.save(basicDBObject);
		
	}


	private void updateUserDetails(UserDBObject userDBObject) {
		
		try {

			System.out.println("updateUserDetails");
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://mydbinstance.c1b4bf3mouew.us-east-1.rds.amazonaws.com:3306/mydb";
			connect = (Connection) DriverManager.getConnection(url,
					"myuser", "mypassword");
			PreparedStatement preparedStatement = (PreparedStatement) connect
					.prepareStatement("UPDATE users SET location_long = ?, location_lat = ? WHERE username = ?");			
			
			preparedStatement.setString(1, userDBObject.getLocation_long());
			preparedStatement.setString(2, userDBObject.getLocation_lat());
			preparedStatement.setString(3, userDBObject.getUsername());
			int status = preparedStatement.executeUpdate();
			System.out.println("Status::"+status);
		} catch (SQLException e) {
			e.printStackTrace();
			userDBObject.setError(true);
			userDBObject.setErrorDesc("Error");
		} catch(Exception e){
			e.printStackTrace();
			userDBObject.setError(true);
			userDBObject.setErrorDesc("Something Crashed");
		} finally {
			try {
				connect.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
	}

	private void insertUserRegDetails(UserDBObject userDBObject) {
		
		try {

			System.out.println("insertUserDetails");
			Class.forName("com.mysql.jdbc.Driver");
			//String url = "jdbc:mysql://localhost:3306/android_connect";
			String url = "jdbc:mysql://mydbinstance.c1b4bf3mouew.us-east-1.rds.amazonaws.com:3306/mydb";
			connect = (Connection) DriverManager.getConnection(url,
					"myuser", "mypassword");
			if (max == 0) {
				statement = (Statement) connect.createStatement();
				ResultSet rst = statement
						.executeQuery("SELECT max(id) from users");
				while (rst.next()) {
					max = rst.getInt("max(id)");
				}
				System.out.println("Max ID:" + max);
			}else {
				max++;
			}
			
			PreparedStatement preparedStatement = (PreparedStatement) connect
					.prepareStatement("INSERT INTO  users(id,username, oauthprovider, phonenumber) VALUES (?,?,?,?)");			
			
			preparedStatement.setInt(1, max+1);
			preparedStatement.setString(2, userDBObject.getUsername());
			preparedStatement.setString(3, userDBObject.getOauthprovider());					
			preparedStatement.setString(4, userDBObject.getPhoneNumber());
			int status = preparedStatement.executeUpdate();
			System.out.println("Status::"+status);
		} catch (SQLException e) {
			e.printStackTrace();
			userDBObject.setError(true);
			userDBObject.setErrorDesc("Duplicate Entry");
		} catch(Exception e){
			e.printStackTrace();
			userDBObject.setError(true);
			userDBObject.setErrorDesc("Something Crashed");
		}finally {
			try {
				connect.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	}

	private void getUserDetails(UserDBObject userDBObject) {
		
		try {
			System.out.println("getUserDetails");
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://mydbinstance.c1b4bf3mouew.us-east-1.rds.amazonaws.com:3306/mydb";
			connect = (Connection) DriverManager.getConnection(url,
					"myuser", "mypassword");
			
			PreparedStatement stmnt = (PreparedStatement) connect.prepareStatement("Select * from users where id = ? ");
			stmnt.setString(1, userDBObject.getUsername());
			ResultSet rst = stmnt.executeQuery();
			while(rst.next()){}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			
		} finally {
			try {
				connect.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	}
}
