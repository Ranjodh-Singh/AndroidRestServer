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

import com.mongodb.MongoClient;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;




@Path("/rdsobjectupdate")
public class RDSEntityOperationsService {
	private Connection connect = null;
	private Statement statement = null;
	MongoClient mongoClient;
	RDSEntityOperationsService(){
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
				getUserDetails(userDBObject);
			}else if (userDBObject.getOperationType().equals("UPDATE")){
				updateUserDetails(userDBObject);
			}
		 return Response.status(200).entity(userDBObject).build();
	 }

	
	private void insertUserRegistrationDetails(UserDBObject userDBObject) {
		
		 	
		
	}


	private void updateUserDetails(UserDBObject userDBObject) {
		// TODO Auto-generated method stub
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
			preparedStatement.setString(3, userDBObject.getUserName());
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
		// TODO Auto-generated method stub
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
			preparedStatement.setString(2, userDBObject.getUserName());
			preparedStatement.setString(3, userDBObject.getOauthProvider());					
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void getUserDetails(UserDBObject userDBObject) {
		// TODO Auto-generated method stub
		try {
			System.out.println("getUserDetails");
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://mydbinstance.c1b4bf3mouew.us-east-1.rds.amazonaws.com:3306/mydb";
			connect = (Connection) DriverManager.getConnection(url,
					"myuser", "mypassword");
			
			PreparedStatement stmnt = (PreparedStatement) connect.prepareStatement("Select * from users where id = ? ");
			stmnt.setString(1, userDBObject.getUserName());
			ResultSet rst = stmnt.executeQuery();
			while(rst.next()){}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			
		} finally {
			try {
				connect.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
