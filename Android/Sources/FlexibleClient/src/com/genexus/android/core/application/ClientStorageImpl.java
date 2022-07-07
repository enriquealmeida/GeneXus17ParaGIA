package com.genexus.android.core.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.genexus.android.core.base.services.ClientStorage;
import com.genexus.android.core.base.services.Services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Storage for key-value pairs with the option to encrypt strings.
 * Uses {@link SharedPreferences} as the underlying storage.
 */
class ClientStorageImpl implements ClientStorage
{
	private final Context mContext;
	private final SharedPreferences mPreferences;
	private CryptoHelper mCryptoHelper;

	private static final String LOG_TAG = "ClientStorage";

	private static final String KEY_ENCRYPTION_DATA = "gx::encryption::encryption_data";
	private static final String KEY_PREFIX_IS_ENCRYPTED = "gx::encryption::is_encrypted>>";

	public ClientStorageImpl(@NonNull Context context, @NonNull SharedPreferences sharedPreferences)
	{
		mContext = context;
		mPreferences = sharedPreferences;

		// initialize have a penalty, so do it only when necessary
		//initializeCryptoHelper();
	}

	private void initializeCryptoHelper()
	{
		try
		{
			if (mCryptoHelper==null)
			{
				Services.Log.info("Create CryptoHelper for secure storage");

				// Initialize from previous encryption data, if any.
				String encryptionConfig = mPreferences.getString(KEY_ENCRYPTION_DATA, null);
				mCryptoHelper = new CryptoHelper(mContext, encryptionConfig);

				mPreferences.edit()
						.putString(KEY_ENCRYPTION_DATA, mCryptoHelper.getEncryptionData())
						.apply();
			}
		}
		catch (CryptoHelper.CryptoException e)
		{
			Services.Log.warning(LOG_TAG, "Exception initializing ClientStorage encryption support", e);
			mPreferences.edit().remove(KEY_ENCRYPTION_DATA).apply();
		}
	}

	@Override
	public void putString(@NonNull String key, @Nullable String value)
	{
		mPreferences.edit()
				.putString(key, value)
				.remove(KEY_PREFIX_IS_ENCRYPTED + key)
				.apply();
	}

	@Override
	public void putStringSecure(@NonNull String key, @Nullable String value)
	{
		boolean successfullyEncrypted = false;
		// initialize have a penalty, so do it only when necessary
		initializeCryptoHelper();
		if (mCryptoHelper != null)
		{
			try
			{
				value = mCryptoHelper.encrypt(value);
				successfullyEncrypted = true;
			}
			catch (CryptoHelper.CryptoException e)
			{
				Services.Log.warning(LOG_TAG, "Exception encrypting value", e);
			}
		}

		if (successfullyEncrypted)
		{
			mPreferences.edit()
					.putString(key, value)
					.putBoolean(KEY_PREFIX_IS_ENCRYPTED + key, true)
					.apply();
		}
		else
			putString(key, value);
	}

	@Override
	public String getString(@NonNull String key, String defValue)
	{
		String value = mPreferences.getString(key, null);
		if (value == null)
			return defValue;

		boolean isEncrypted = mPreferences.getBoolean(KEY_PREFIX_IS_ENCRYPTED + key, false);
		if (isEncrypted)
		{
			boolean successfullyDecrypted = false;
			// initialize have a penalty, so do it only when necessary
			initializeCryptoHelper();
			if (mCryptoHelper != null)
			{
				try
				{
					value = mCryptoHelper.decrypt(value);
					successfullyDecrypted = true;
				}
				catch (CryptoHelper.CryptoException e)
				{
					Services.Log.warning(LOG_TAG, "Exception decrypting value", e);
				}
			}

			if (!successfullyDecrypted)
			{
				// Was encrypted but we have no means to decrypt it? Better to lose it altogether.
				value = defValue;
				remove(key);
			}
		}

		return value;
	}

	@Override
	public void putBoolean(@NonNull String key, boolean value)
	{
		mPreferences.edit()
				.putBoolean(key, value)
				.apply();

	}

	@Override
	public boolean getBoolean(@NonNull String key, boolean defValue)
	{
		return mPreferences.getBoolean(key, defValue);
	}

	@Override
	public void remove(@NonNull String key)
	{
		mPreferences.edit()
				.remove(key)
				.remove(KEY_PREFIX_IS_ENCRYPTED + key)
				.apply();
	}

	@Override
	public void clear()
	{
		// Clear everything and generate a new encryption key.
		mPreferences.edit().clear().apply();
		mCryptoHelper = null;
		initializeCryptoHelper();
	}
}
