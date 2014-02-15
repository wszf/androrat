package utils;

import java.io.Serializable;
import java.util.ArrayList;

public class Contact implements Serializable {

	private static final long serialVersionUID = -744071613945933264L;
	long id;
	int times_contacted;
	long last_time_contacted;
	String display_name;
	int starred;
	ArrayList<String> phones;
	ArrayList<String> emails;
	ArrayList<String> notes;
	String street;
	String city;
	String region;
	String postalcode;
	String country;
	int type_addr;
	ArrayList<String> messaging;
	String OrganisationName;
	String OrganisationStatus; //manager ..
	byte[] photo;
	
	public Contact() {
		
	}

	public String getRegion() {
		return region;
	}
	
	public void setRegion(String reg) {
		region = reg;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTimes_contacted() {
		return times_contacted;
	}

	public void setTimes_contacted(int times_contacted) {
		this.times_contacted = times_contacted;
	}

	public long getLast_time_contacted() {
		return last_time_contacted;
	}

	public void setLast_time_contacted(long last_time_contacted) {
		this.last_time_contacted = last_time_contacted;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public int getStarred() {
		return starred;
	}

	public void setStarred(int starred) {
		this.starred = starred;
	}

	public ArrayList<String> getPhones() {
		return phones;
	}

	public void setPhones(ArrayList<String> phones) {
		this.phones = phones;
	}

	public ArrayList<String> getEmails() {
		return emails;
	}

	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
	}

	public ArrayList<String> getNotes() {
		return notes;
	}

	public void setNotes(ArrayList<String> notes) {
		this.notes = notes;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getType_addr() {
		return type_addr;
	}

	public void setType_addr(int type_addr) {
		this.type_addr = type_addr;
	}

	public ArrayList<String> getMessaging() {
		return messaging;
	}

	public void setMessaging(ArrayList<String> messaging) {
		this.messaging = messaging;
	}

	public String getOrganisationName() {
		return OrganisationName;
	}

	public void setOrganisationName(String organisationName) {
		OrganisationName = organisationName;
	}

	public String getOrganisationStatus() {
		return OrganisationStatus;
	}

	public void setOrganisationStatus(String organisationStatus) {
		OrganisationStatus = organisationStatus;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
}
