package com.genexus.android.core.externalobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class Contacts {
	private final Context mContext;

	public Contacts(Context context) {
		mContext = context.getApplicationContext();
	}

	private static final String TYPE_CONTACT = "GeneXus.SD.ContactInfo";

	private static final String PROP_CONTACT_DISPLAY_NAME = "DisplayName";
	private static final String PROP_CONTACT_FIRST_NAME = "FirstName";
	private static final String PROP_CONTACT_LAST_NAME = "LastName";
	private static final String PROP_CONTACT_EMAIL = "EMail";
	private static final String PROP_CONTACT_PHONE = "Phone";
	private static final String PROP_CONTACT_COMPANY_NAME = "CompanyName";
	private static final String PROP_CONTACT_PHOTO = "Photo";
	private static final String PROP_CONTACT_NOTES = "Notes";

	public List<Entity> getAllContacts() {
		// We need all the contacts and some of their data (phone, email, organization, &c).
		// There are two basic ways to accomplish this:
		// 1) Query the contacts table and then the DATA table for each piece of information.
		// 2) Query the DATA table and do a control break for each contact.
		// We implement the second option, since it should be more efficient.
		ArrayList<Entity> contacts = new ArrayList<>();

		// Get the list of groups against which we will check membership.  
		List<Integer> desiredGroups = getDefaultGroups();

		String[] sortOrder = new String[]{"UPPER(" + ContactsContract.Data.DISPLAY_NAME_PRIMARY + ")", ContactsContract.Data.CONTACT_ID, ContactsContract.Data.MIMETYPE,
			ContactsContract.Data.IS_SUPER_PRIMARY + " DESC", ContactsContract.Data.IS_PRIMARY + " DESC"};

		Cursor cursor = mContext.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, Services.Strings.join(Arrays.asList(sortOrder), ", "));
		if (cursor != null) // Fix for a bug in Android M, see https://code.google.com/p/android-developer-preview/issues/detail?id=2342
		{
			try {
				int currentId = -1;
				Entity current = null;
				boolean isDesired = false;

				int colContactId = cursor.getColumnIndexOrThrow(ContactsContract.Data.CONTACT_ID);
				int colDisplayName = cursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME_PRIMARY);
				int colDataMimeType = cursor.getColumnIndexOrThrow(ContactsContract.Data.MIMETYPE);

				int colInDefaultDirectory = cursor.getColumnIndexOrThrow(ContactsContract.Data.IN_DEFAULT_DIRECTORY);
				int colGroupVisible = cursor.getColumnIndexOrThrow(ContactsContract.Data.IN_VISIBLE_GROUP);

				while (cursor.moveToNext()) {
					int id = cursor.getInt(colContactId);
					if (id != currentId) {
						// Switched to New contact.
						if (current != null && isDesired) {
							//Services.Log.debug(" current add " + current.toString());
							contacts.add(current);
						} else if (current != null) {
							Services.Log.debug(" CURRENT DISCARD " + current.toString());

						}
						current = newAddressBookContact();
						String displayName = cursor.getString(colDisplayName);
						setPropertyIfNeeded(current, PROP_CONTACT_DISPLAY_NAME, displayName);

						currentId = id;

						// If we have no special groups, then all contacts are "desired".
						isDesired = (desiredGroups.size() == 0);
					}

					// Read one row of contact data.
					String dataMimeType = cursor.getString(colDataMimeType);
					processDataRow(cursor, dataMimeType, current);


					// Check if this contact belongs to a desired group.
					if (!isDesired && desiredGroups.size() != 0 && dataMimeType.equalsIgnoreCase(CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)) {
						int groupId = getInt(cursor, CommonDataKinds.GroupMembership.GROUP_ROW_ID);
						if (desiredGroups.contains(groupId)) {
							isDesired = true;
						} else {
							Services.Log.debug(" no matching desiredGroups ");
							int defaultDirectory = cursor.getInt(colInDefaultDirectory);
							int groupVisible = cursor.getInt(colGroupVisible);
							Services.Log.debug(" default Directory " + defaultDirectory + " group Visible " + groupVisible);
							if (defaultDirectory == 1 || groupVisible == 1) {
								isDesired = true;
							} else {
								//no matching
								Services.Log.debug(" current no matching directory or visible" + current.toString());
							}
						}
					} else if (!isDesired)   // samsung and maybe other not have groupmembership info.
					{
						Services.Log.debug(" no matching desiredGroups mimetype");
						int defaultDirectory = cursor.getInt(colInDefaultDirectory);
						int groupVisible = cursor.getInt(colGroupVisible);
						Services.Log.debug(" default Directory " + defaultDirectory + " group Visible " + groupVisible);
						if (defaultDirectory == 1 || groupVisible == 1) {
							isDesired = true;
						} else {
							//no matching
							Services.Log.debug(" current no matching mimetype directory or visible" + current.toString());
						}
					}

				}

				// Add last processed row.
				if (current != null && isDesired) {
					//Services.Log.debug(" current add " + current.toString());
					contacts.add(current);
				} else if (current != null) {
					Services.Log.debug(" CURRENT DISCARD " + current.toString());

				}
			} finally {
				cursor.close();
			}
		}

		return contacts;
	}

	public boolean removeContact(String phone, String fullName) {
		Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
		Cursor cursor = mContext.getContentResolver().query(contactUri, null, null, null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				if (cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(fullName)) {
					String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
					Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
					mContext.getContentResolver().delete(uri, null, null);
					return true;
				}
			}
		}

		return false;
	}

	private void processDataRow(Cursor cursor, String dataMimeType, Entity contact) {
		// Check whether the data for the current row is "interesting" or not.
		// Since there may be multiple raw contacts associated to this contact, 
		// take data from the first one that has it.
		if (dataMimeType.equalsIgnoreCase(CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
			String firstName = getString(cursor, CommonDataKinds.StructuredName.GIVEN_NAME);
			String lastName = getString(cursor, CommonDataKinds.StructuredName.FAMILY_NAME);

			setPropertyIfNeeded(contact, PROP_CONTACT_FIRST_NAME, firstName);
			setPropertyIfNeeded(contact, PROP_CONTACT_LAST_NAME, lastName);
		} else if (dataMimeType.equalsIgnoreCase(CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
			String email = getString(cursor, CommonDataKinds.Email.ADDRESS);
			setPropertyIfNeeded(contact, PROP_CONTACT_EMAIL, email);
		} else if (dataMimeType.equalsIgnoreCase(CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
			String phone = getString(cursor, CommonDataKinds.Phone.NUMBER);
			setPropertyIfNeeded(contact, PROP_CONTACT_PHONE, phone);
		} else if (dataMimeType.equalsIgnoreCase(CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
			String companyName = getString(cursor, CommonDataKinds.Organization.COMPANY);
			setPropertyIfNeeded(contact, PROP_CONTACT_COMPANY_NAME, companyName);
		} else if (dataMimeType.equalsIgnoreCase(CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
			// Read photo and move it into a file local to the app.
			String photoUri = getString(cursor, CommonDataKinds.Photo.PHOTO_URI);
			setPropertyIfNeeded(contact, PROP_CONTACT_PHOTO, photoUri);
		} else if (dataMimeType.equalsIgnoreCase(CommonDataKinds.Note.CONTENT_ITEM_TYPE)) {
			String notes = getString(cursor, CommonDataKinds.Note.NOTE);
			setPropertyIfNeeded(contact, PROP_CONTACT_NOTES, notes);
		}
	}


	private List<Integer> getDefaultGroups() {
		ArrayList<Integer> groupIds = new ArrayList<>();
		Cursor cursor = mContext.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, null, null, ContactsContract.Groups._ID);
		if (cursor != null) {
			try {
				while (cursor.moveToNext()) {
					int groupId = getInt(cursor, ContactsContract.Groups._ID);

					// Use a heuristic for default contact groups:
					// AUTO_ADD (i.e. "My Contacts") & Favorites.
					if (getInt(cursor, ContactsContract.Groups.AUTO_ADD) != 0 || getInt(cursor, ContactsContract.Groups.FAVORITES) != 0)
						groupIds.add(groupId);
				}
			} finally {
				cursor.close();
			}
		}

		return groupIds;
	}

	private static Entity newAddressBookContact() {
		Entity entitySdt = EntityFactory.newSdt(TYPE_CONTACT);
		entitySdt.initialize();
		return entitySdt;
	}

	private static boolean setPropertyIfNeeded(Entity item, String property, String value) {
		if (!Strings.hasValue(value))
			return false; // No new value provided.

		String oldValue = item.optStringProperty(property);
		if (Strings.hasValue(oldValue))
			return false; // Already had an old value.

		item.setProperty(property, value);
		return true;
	}

	private static String getString(Cursor cursor, String column) {
		return cursor.getString(cursor.getColumnIndexOrThrow(column));
	}

	private static int getInt(Cursor cursor, String column) {
		return cursor.getInt(cursor.getColumnIndexOrThrow(column));
	}


	public Entity getContactFromUri(Uri contactUri) {

		// Read this contact data .
		Cursor cursor = mContext.getContentResolver().query(contactUri, null, null, null, null);
		Entity current = null;

		String lookupKey = null;

		// If the cursor returned is valid, get the phone number
		if (cursor != null && cursor.moveToFirst()) {

			int colDisplayName = cursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME_PRIMARY);
			int colLookupKey = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY);

			lookupKey = cursor.getString(colLookupKey);

			// Create the contact to return
			current = newAddressBookContact();

			String displayName = cursor.getString(colDisplayName);
			setPropertyIfNeeded(current, PROP_CONTACT_DISPLAY_NAME, displayName);

			// Read one row of contact data. with phone info
			processDataRowPhone(cursor, current);
		}

		// use another cursor to get the rest of the contact data.
		String selectionCondition = ContactsContract.Data.LOOKUP_KEY + " = ?";
		String[] selectionArgs = {lookupKey};

		Cursor cursorAll = mContext.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, selectionCondition, selectionArgs, null);

		if (cursorAll != null && current != null) {
			int colDataMimeType = cursorAll.getColumnIndexOrThrow(ContactsContract.Data.MIMETYPE);

			while (cursorAll.moveToNext()) {

				// Read one row of contact data. Read all the rest of the contact data.
				String dataMimeType = cursorAll.getString(colDataMimeType);
				processDataRow(cursorAll, dataMimeType, current);
			}
		}
		return current;
	}

	private void processDataRowPhone(Cursor cursor, Entity contact) {
		// Check the data for the current row, phone data only is present

		String phone = getString(cursor, CommonDataKinds.Phone.NUMBER);
		setPropertyIfNeeded(contact, PROP_CONTACT_PHONE, phone);

		// Read photo and move it into a file local to the app.
		String photoUri = getString(cursor, CommonDataKinds.Phone.PHOTO_URI);
		setPropertyIfNeeded(contact, PROP_CONTACT_PHOTO, photoUri);

	}

	public List<Entity> getContactsFromUri(Uri contactUri) {
		ArrayList<Entity> contacts = new ArrayList<>();

		Entity current = getContactFromUri(contactUri);
		if (current != null)
			contacts.add(current);

		return contacts;
	}

}
