package com.genexus.android.core.externalobjects;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

import java.util.List;

public class ContactsAPI extends ExternalApi {

	public static final String OBJECT_NAME = "GeneXus.SD.Contacts";

	private static final String METHOD_ADD_CONTACT = "AddContact";
	private static final String METHOD_REMOVE_CONTACT = "RemoveContact";
	private static final String METHOD_VIEW_CONTACT = "ViewContact";
	private static final String METHOD_GET_ALL_CONTACTS = "GetAllContacts";
	private static final String METHOD_PICK_CONTACT = "PickContact";
	private static final String METHOD_PICK_CONTACTS = "PickContacts";

	private static final String[] PERMISSIONS = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};

	static final int REQUEST_SELECT_CONTACT = 451;
	static final int REQUEST_SELECT_CONTACTS = 452;

	public ContactsAPI(ApiAction action) {
		super(action);
		addMethodHandler(METHOD_ADD_CONTACT, 7, mAddContactHandler);
		addMethodHandlerRequestingPermissions(METHOD_VIEW_CONTACT, 4, PERMISSIONS, mViewContactHandler);
		addMethodHandlerRequestingPermissions(METHOD_PICK_CONTACT, 0, PERMISSIONS, mPickContactHandler);
		addMethodHandlerRequestingPermissions(METHOD_PICK_CONTACTS, 0, PERMISSIONS, mPickContactsHandler);
		addMethodHandlerRequestingPermissions(METHOD_REMOVE_CONTACT, 5, PERMISSIONS, mRemoveContactHandler);
		addMethodHandlerRequestingPermissions(METHOD_GET_ALL_CONTACTS, 0, PERMISSIONS, mGetAllContactHandler);
	}

	private final IMethodInvoker mGetAllContactHandler = parameters -> {
		Contacts helper = new Contacts(getContext());
		return ExternalApiResult.success(helper.getAllContacts());
	};

	private final IMethodInvokerWithActivityResult mAddContactHandler = new IMethodInvokerWithActivityResult() {

		@NonNull
		@Override
		public ExternalApiResult invoke(List<Object> parameters) {
			if (!SDActionsHelper.addContactFromParameters(getActivity(), ExternalApi.toString(parameters))) {
				return ExternalApiResult.failure(R.string.GXM_NoApplicationAvailable);
			}
			return ExternalApiResult.SUCCESS_WAIT;
		}

		@NonNull
		@Override
		public ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
			return ExternalApiResult.success(resultCode == Activity.RESULT_OK);
		}
	};

	private final IMethodInvoker mRemoveContactHandler = parameters -> {
		String firstName = (String) parameters.get(0);
		String lastName = (String) parameters.get(1);
		String fullName = firstName + " " + lastName;
		String phone = (String) parameters.get(3);
		Contacts contactsHelper = new Contacts(getContext());
		return ExternalApiResult.success(contactsHelper.removeContact(phone, fullName));
	};

	private final IMethodInvoker mViewContactHandler = new IMethodInvokerWithActivityResult() {

		@NonNull
		@Override
		public ExternalApiResult invoke(List<Object> parameters) {
			try {
				if (SDActionsHelper.viewContactFromParameters(getActivity(), ExternalApi.toString(parameters)))
					return ExternalApiResult.SUCCESS_WAIT;
				else {
					Services.Log.info("Contact not found in method ViewContact");
					return ExternalApiResult.SUCCESS_CONTINUE;
				}
				// IllegalCatch is ignored to allow catching NullPointerException which is
				// thrown when no Application can handle an Intent with an incorrect Contact Id
			} catch (@SuppressWarnings("checkstyle:IllegalCatch") NullPointerException exception) {
				return ExternalApiResult.SUCCESS_CONTINUE;
			}
		}

		@NonNull
		@Override
		public ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
			return ExternalApiResult.SUCCESS_CONTINUE;
		}

	};

	private final IMethodInvokerWithActivityResult mPickContactHandler = new IMethodInvokerWithActivityResult() {

		@NonNull
		@Override
		public ExternalApiResult invoke(List<Object> parameters) {
			// Pick contact with phones. https://developer.android.com/guide/components/intents-common#PickContactDat
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			// select only contacts with phones.
			intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
			//intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
				startActivityForResult(intent, REQUEST_SELECT_CONTACT);
				return ExternalApiResult.SUCCESS_WAIT;
			}
			return ExternalApiResult.FAILURE;
		}

		@NonNull
		@Override
		public ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
			if (requestCode == REQUEST_SELECT_CONTACT && resultCode == Activity.RESULT_OK) {
				Uri contactUri = result.getData();
				// Get contact data from selected contact at contactUri
				Contacts helper = new Contacts(getContext());
				Entity current = helper.getContactFromUri(contactUri);
				if (current != null)
					return ExternalApiResult.success(current);
			}
			return ExternalApiResult.FAILURE;
		}

	};

	private final IMethodInvokerWithActivityResult mPickContactsHandler = new IMethodInvokerWithActivityResult() {

		@NonNull
		@Override
		public ExternalApiResult invoke(List<Object> parameters) {
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			// select only contacts with phones.
			intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
			// multiple contacts not supported
			//intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); not supported
			if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
				startActivityForResult(intent, REQUEST_SELECT_CONTACTS);
				return ExternalApiResult.SUCCESS_WAIT;
			}
			return ExternalApiResult.FAILURE;
		}

		@NonNull
		@Override
		public ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
			if (requestCode == REQUEST_SELECT_CONTACTS && resultCode == Activity.RESULT_OK) {
				Uri contactUri = result.getData();
				// Get contact data from selected contact at contactUri
				Contacts helper = new Contacts(getContext());
				List<Entity> contactsData = helper.getContactsFromUri(contactUri);
				return ExternalApiResult.success(contactsData);
			}
			return ExternalApiResult.FAILURE;
		}

	};
}
