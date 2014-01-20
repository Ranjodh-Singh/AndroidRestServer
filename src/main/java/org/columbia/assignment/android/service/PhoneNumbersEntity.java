package org.columbia.assignment.android.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PhoneNumbersEntity implements Serializable {
	public PhoneNumbersEntity() {
		super();this.phoneNumbers = new ArrayList<String>();
		
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -1197541481573050033L;
	@JsonIgnore
	List<String> phoneNumbers;
	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

}
