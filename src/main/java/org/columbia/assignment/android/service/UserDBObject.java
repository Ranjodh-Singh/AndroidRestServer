package org.columbia.assignment.android.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class UserDBObject implements Serializable {

	
	private static final long serialVersionUID = 4011988L;
	private String operationType;
	
	@JsonIgnore
	private List <UserDBObject> friendList;
	 
	private String phoneNumber;
	private boolean isError;
	private String errorDesc;
	
	
	public UserDBObject(){
		friendList = new  ArrayList<UserDBObject>();
	}
	
	
	
	


	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public List<UserDBObject> getFriendList() {
		return friendList;
	}
	public void setFriendList(List<UserDBObject> friendList) {
		this.friendList = friendList;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOauthprovider() {
		return oauthprovider;
	}
	public void setOauthprovider(String oauthprovider) {
		this.oauthprovider = oauthprovider;
	}
	public String getLocation_long() {
		return location_long;
	}
	public void setLocation_long(String location_long) {
		this.location_long = location_long;
	}
	public String getLocation_lat() {
		return location_lat;
	}
	public void setLocation_lat(String location_lat) {
		this.location_lat = location_lat;
	}






	/**
	 * this class will work as the place holder for the User object.
	 * An extra variable is added to store answer given by user.
	 */
	@JsonProperty("_id")
	 String _id;

	
       
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	
	private String username;
	private String oauthprovider;
	private String location_long;
	private String location_lat;	
	

	/*
	static class Id implements Serializable{
		@JsonCreator
		public Id(String id){
			set$id(id);
		}
		String $id;

		public String get$id() {
			return $id;
		}

		public void set$id(String $id) {
			this.$id = $id;
		}
		
	}*/


	@Override
	public String toString() {
		return "UserDBObject [operationType=" + operationType + ", friendList="
				+ friendList + ", phoneNumber=" + phoneNumber + ", isError="
				+ isError + ", errorDesc=" + errorDesc + ", _id=" + _id
				+ ", username=" + username + ", oauthprovider=" + oauthprovider
				+ ", location_long=" + location_long + ", location_lat="
				+ location_lat + "]";
	}

	
	
	
	
}

