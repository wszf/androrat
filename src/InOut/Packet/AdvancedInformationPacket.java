package Packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class AdvancedInformationPacket implements Packet, Serializable{

	private static final long serialVersionUID = 44346671562310318L;
	String phoneNumber;
	String IMEI;
	String softwareVersion;
	String countryCode;
	String operatorCode;
	String operatorName;
	String simOperatorCode;
	String simOperatorName;
	String simCountryCode;
	String simSerial;
	
	boolean wifiAvailable;
	boolean wifiConnectedOrConnecting;
	String wifiExtraInfos;
	String wifiReason;
	
	String mobileNetworkName;
	boolean mobileNetworkAvailable;
	boolean mobileNetworkConnectedOrConnecting;
	String mobileNetworkExtraInfos;
	String mobileNetworkReason;
	
	String androidVersion;
	int androidSdk;
	
	ArrayList<String> sensors;
	
	int batteryHealth;
	int batteryLevel;
	int batteryPlugged;
	boolean batteryPresent;
	int batteryScale;
	int batteryStatus;
	String batteryTechnology;
	int batteryTemperature;
	int batteryVoltage;
	
	public byte[] build() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(this);
			return bos.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

	public void parse(byte[] packet) {
		ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(bis);
			AdvancedInformationPacket adv = (AdvancedInformationPacket) in.readObject();
			setPhoneNumber(adv.getPhoneNumber());
			setIMEI(adv.getIMEI());
			setSoftwareVersion(adv.getSoftwareVersion());
			setCountryCode(adv.getCountryCode());
			setOperatorCode(adv.getOperatorCode());
			setOperatorName(adv.getOperatorName());
			setSimOperatorCode(adv.getSimOperatorCode());
			setSimOperatorName(adv.getSimOperatorName());
			setSimCountryCode(adv.getSimCountryCode());
			setSimSerial(adv.getSimSerial());
			setWifiAvailable(adv.isWifiAvailable());
			setWifiConnectedOrConnecting(adv.isWifiConnectedOrConnecting());
			setWifiExtraInfos(adv.getWifiExtraInfos());
			setWifiReason(adv.getWifiReason());
			setMobileNetworkName(adv.getMobileNetworkName());
			setMobileNetworkAvailable(adv.isMobileNetworkAvailable());
			setMobileNetworkConnectedOrConnecting(adv.isMobileNetworkConnectedOrConnecting());
			setMobileNetworkExtraInfos(adv.getMobileNetworkExtraInfos());
			setMobileNetworkReason(adv.getMobileNetworkReason());
			setAndroidVersion(adv.getAndroidVersion());
			setAndroidSdk(adv.getAndroidSdk());
			setSensors(adv.getSensors());
			setBatteryHealth(adv.getBatteryHealth());
			setBatteryLevel(adv.getBatteryLevel());
			setBatteryPlugged(adv.getBatteryPlugged());
			setBatteryPresent(adv.isBatteryPresent());
			setBatteryScale(adv.getBatteryScale());
			setBatteryStatus(adv.getBatteryStatus());
			setBatteryTechnology(adv.getBatteryTechnology());
			setBatteryTemperature(adv.getBatteryTemperature());
			setBatteryVoltage(adv.getBatteryVoltage());
		} catch (Exception e) {
		}
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getSimCountryCode() {
		return simCountryCode;
	}
	
	public void setSimCountryCode(String code) {
		this.simCountryCode = code;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getSimOperatorCode() {
		return simOperatorCode;
	}

	public void setSimOperatorCode(String simOperatorCode) {
		this.simOperatorCode = simOperatorCode;
	}

	public String getSimOperatorName() {
		return simOperatorName;
	}

	public void setSimOperatorName(String simOperatorName) {
		this.simOperatorName = simOperatorName;
	}

	public String getSimSerial() {
		return simSerial;
	}

	public void setSimSerial(String simSerial) {
		this.simSerial = simSerial;
	}

	public boolean isWifiAvailable() {
		return wifiAvailable;
	}

	public void setWifiAvailable(boolean wifiAvailable) {
		this.wifiAvailable = wifiAvailable;
	}

	public boolean isWifiConnectedOrConnecting() {
		return wifiConnectedOrConnecting;
	}

	public void setWifiConnectedOrConnecting(boolean wifiConnectedOrConnecting) {
		this.wifiConnectedOrConnecting = wifiConnectedOrConnecting;
	}

	public String getWifiExtraInfos() {
		return wifiExtraInfos;
	}

	public void setWifiExtraInfos(String wifiExtraInfos) {
		this.wifiExtraInfos = wifiExtraInfos;
	}

	public String getWifiReason() {
		return wifiReason;
	}

	public void setWifiReason(String wifiReason) {
		this.wifiReason = wifiReason;
	}

	public String getMobileNetworkName() {
		return mobileNetworkName;
	}

	public void setMobileNetworkName(String mobileNetworkName) {
		this.mobileNetworkName = mobileNetworkName;
	}

	public boolean isMobileNetworkAvailable() {
		return mobileNetworkAvailable;
	}

	public void setMobileNetworkAvailable(boolean mobileNetworkAvailable) {
		this.mobileNetworkAvailable = mobileNetworkAvailable;
	}

	public boolean isMobileNetworkConnectedOrConnecting() {
		return mobileNetworkConnectedOrConnecting;
	}

	public void setMobileNetworkConnectedOrConnecting(
			boolean mobileNetworkConnectedOrConnecting) {
		this.mobileNetworkConnectedOrConnecting = mobileNetworkConnectedOrConnecting;
	}

	public String getMobileNetworkExtraInfos() {
		return mobileNetworkExtraInfos;
	}

	public void setMobileNetworkExtraInfos(String mobileNetworkExtraInfos) {
		this.mobileNetworkExtraInfos = mobileNetworkExtraInfos;
	}

	public String getMobileNetworkReason() {
		return mobileNetworkReason;
	}

	public void setMobileNetworkReason(String mobileNetworkReason) {
		this.mobileNetworkReason = mobileNetworkReason;
	}

	public String getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

	public int getAndroidSdk() {
		return androidSdk;
	}

	public void setAndroidSdk(int androidSdk) {
		this.androidSdk = androidSdk;
	}

	public ArrayList<String> getSensors() {
		return sensors;
	}

	public void setSensors(ArrayList<String> sensors) {
		this.sensors = sensors;
	}

	public int getBatteryHealth() {
		return batteryHealth;
	}

	public void setBatteryHealth(int batteryHealth) {
		this.batteryHealth = batteryHealth;
	}

	public int getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public int getBatteryPlugged() {
		return batteryPlugged;
	}

	public void setBatteryPlugged(int batteryPlugged) {
		this.batteryPlugged = batteryPlugged;
	}

	public boolean isBatteryPresent() {
		return batteryPresent;
	}

	public void setBatteryPresent(boolean batteryPresent) {
		this.batteryPresent = batteryPresent;
	}

	public int getBatteryScale() {
		return batteryScale;
	}

	public void setBatteryScale(int batteryScale) {
		this.batteryScale = batteryScale;
	}

	public int getBatteryStatus() {
		return batteryStatus;
	}

	public void setBatteryStatus(int batteryStatus) {
		this.batteryStatus = batteryStatus;
	}

	public String getBatteryTechnology() {
		return batteryTechnology;
	}

	public void setBatteryTechnology(String batteryTechnology) {
		this.batteryTechnology = batteryTechnology;
	}

	public int getBatteryTemperature() {
		return batteryTemperature;
	}

	public void setBatteryTemperature(int batteryTemperature) {
		this.batteryTemperature = batteryTemperature;
	}

	public int getBatteryVoltage() {
		return batteryVoltage;
	}

	public void setBatteryVoltage(int batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

}
