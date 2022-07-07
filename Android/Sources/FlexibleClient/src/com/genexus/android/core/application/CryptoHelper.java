package com.genexus.android.core.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.*;
import androidx.annotation.NonNull;
import android.util.Base64;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.HexEncoder;

/**
 * CryptoHelper for secure storage.
 *
 * Uses a secret key for symmetric encryption. The secret key itself is protected by a
 * public-private key pair stored in the AndroidKeyStore, which means it cannot be extracted
 * event if the device is rooted.
 */
@SuppressWarnings("deprecation")
class CryptoHelper
{
	private static final String ENCRYPTION_TYPE_API18 = "API18";
	private static final String ENCRYPTION_TYPE_LEGACY = "Legacy";

	private static final String SECRET_KEY_ALGORITHM = "AES";
	private static final int SECRET_KEY_LENGTH = 256; // bits

	private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final int IV_LENGTH = 16;
	private static final String STRING_ENCODING = "UTF-8";

	private final SecretKey mKey;
	private final String mEncryptionData;

	public CryptoHelper(Context context, String encryptionData) throws CryptoException
	{
		try
		{
			final SecretKeyWrapper keyWrapper;
			if (encryptionData != null)
			{
				int separatorPos = encryptionData.indexOf(';');
				String encryptionType = encryptionData.substring(0, separatorPos);
				String encryptionKey = encryptionData.substring(separatorPos + 1);

				// Keep using the same encryption type we had. We cannot switch now.
				if (ENCRYPTION_TYPE_API18.equals(encryptionType))
					keyWrapper = new SecretKeyWrapperApi18(context);
				else if (ENCRYPTION_TYPE_LEGACY.equals(encryptionType))
					keyWrapper = new SecretKeyWrapperLegacy();
				else
					throw new IllegalArgumentException("Unknown encryptionType: " + encryptionType);

				// Unwrap the secret key (using KeyStore on API 18).
				byte[] keyBlob = Base64.decode(encryptionKey, Base64.DEFAULT);
				mKey = keyWrapper.unwrap(keyBlob);

				mEncryptionData = encryptionData;
			}
			else
			{
				if (Services.Application.get() == null ||
						!Services.Application.get().getUseLegacyClientStorage())
				{
					Services.Log.info("Create SecretKey Wrapper Api18 ");
					keyWrapper = new SecretKeyWrapperApi18(context);
				}
				else
				{
					Services.Log.info("Create SecretKey Wrapper Legacy ");
					keyWrapper = new SecretKeyWrapperLegacy();
				}

				// Create a new secret key.
				SecureRandom secureRandom = new SecureRandom();
				KeyGenerator keyGenerator = KeyGenerator.getInstance(SECRET_KEY_ALGORITHM);
				keyGenerator.init(SECRET_KEY_LENGTH, secureRandom);
				mKey = keyGenerator.generateKey();

				byte[] keyBlob = keyWrapper.wrap(mKey);
				mEncryptionData = keyWrapper.getType() + ';' + Base64.encodeToString(keyBlob, Base64.DEFAULT);
			}
		}
		catch (GeneralSecurityException | IOException e)
		{
			throw new CryptoException("Error initializing CryptoHelper", e);
		}
	}

	public String getEncryptionData()
	{
		return mEncryptionData;
	}

	public String encrypt(String clearText) throws CryptoException
	{
		try
		{
			byte[] iv = generateIv();
			IvParameterSpec ivSpec = new IvParameterSpec(iv);

			// Encrypt using the key and IV.
			Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);
			encryptionCipher.init(Cipher.ENCRYPT_MODE, mKey, ivSpec);
			byte[] encryptedBytes = encryptionCipher.doFinal(clearText.getBytes(STRING_ENCODING));

			String ivHex = HexEncoder.toHex(iv);
			String encryptedHex = HexEncoder.toHex(encryptedBytes);

			// Return both the IV and the encrypted text, to reverse it later.
			return ivHex + encryptedHex;
		}
		catch (NoSuchAlgorithmException | BadPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | NoSuchPaddingException
				| IllegalBlockSizeException | UnsupportedEncodingException e)
		{
			throw new CryptoException("Unable to encrypt", e);
		}
	}

	public String decrypt(String cipherText) throws CryptoException
	{
		try
		{
			// Separate the algorithm's IV from the encrypted text proper.
			String ivHex = cipherText.substring(0, IV_LENGTH * 2);
			String encryptedHex = cipherText.substring(IV_LENGTH * 2);

			// Decrypt using the key and the IV.
			Cipher decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);
			IvParameterSpec ivSpec = new IvParameterSpec(HexEncoder.toByte(ivHex));
			decryptionCipher.init(Cipher.DECRYPT_MODE, mKey, ivSpec);

			byte[] decryptedBytes = decryptionCipher.doFinal(HexEncoder.toByte(encryptedHex));
			return new String(decryptedBytes, STRING_ENCODING);

		}
		catch (NoSuchAlgorithmException | BadPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | NoSuchPaddingException
				| IllegalBlockSizeException | UnsupportedEncodingException e)
		{
			throw new CryptoException("Unable to decrypt", e);
		}
	}

	private byte[] generateIv()
	{
		SecureRandom random = new SecureRandom();
		byte[] iv = new byte[IV_LENGTH];
		random.nextBytes(iv);
		return iv;
	}

	private interface SecretKeyWrapper
	{
		@NonNull String getType();
		@NonNull byte[] wrap(@NonNull SecretKey key) throws GeneralSecurityException;
		@NonNull SecretKey unwrap(@NonNull byte[] blob) throws GeneralSecurityException;
	}

	/**
	 * Secret key wrapper for pre-API18 devices.
	 *
	 * Since we cannot have a certificate from the KeyStore to encrypt/decrypt securely,
	 * just obfuscate it somewhat.
	 */
	private static class SecretKeyWrapperLegacy implements SecretKeyWrapper
	{
		private static final byte[] DATA;

		static
		{
			DATA = new byte[SECRET_KEY_LENGTH / 8];
			for (int i = 0; i < DATA.length; i++)
				DATA[i] = (byte)(i * 853 + 42);
		}

		@Override
		public @NonNull String getType()
		{
			return ENCRYPTION_TYPE_LEGACY;
		}

		@Override
		public @NonNull byte[] wrap(@NonNull SecretKey key) {
			return scrambleBytes(key.getEncoded());
		}

		@Override
		public @NonNull SecretKey unwrap(@NonNull byte[] blob) {
			return new SecretKeySpec(scrambleBytes(blob), 0, blob.length, SECRET_KEY_ALGORITHM);
		}

		private static @NonNull byte[] scrambleBytes(@NonNull byte[] blob)
		{
			if (blob.length != DATA.length)
				throw new IllegalArgumentException(String.format("Invalid blob length (%s)", blob.length));

			byte[] scrambled = new byte[blob.length];
			for (int i = 0; i < blob.length; i++)
				scrambled[i] = (byte)(blob[i] ^ DATA[i]);

			return scrambled;
		}
	}

	/**
	 * SecretKey wrapper for API18 devices.
	 *
	 * Uses an RSA public/private keypair from the Android KeyStore to wrap/unwrap key material.
	 * This should be secure, even for a rooted device, especially if it supports hardware-backed crypto.
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@SuppressWarnings("deprecation")
	@SuppressLint("GetInstance")
	private static class SecretKeyWrapperApi18 implements SecretKeyWrapper
	{
		private static final String KEYSTORE_TYPE = "AndroidKeyStore";
		private static final String KEYSTORE_ALIAS = "GeneXusRSAKey";
		private static final String KEY_ALGORITHM = "RSA";
		private static final String KEY_CIPHER_TRANSFORMATION = "RSA/ECB/PKCS1Padding";

		private final Cipher mCipher;
		private final KeyPair mPair;
		/**
		 * Create a wrapper using the public/private key pair with the given alias.
		 * If no pair with that alias exists, it will be generated.
		 */
		public SecretKeyWrapperApi18(Context context) throws GeneralSecurityException, IOException
		{
			mCipher = Cipher.getInstance(KEY_CIPHER_TRANSFORMATION);
			final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
			keyStore.load(null);

			if (!keyStore.containsAlias(KEYSTORE_ALIAS))
				generateKeyPair(context, KEYSTORE_ALIAS);

			// Even if we just generated the key, read it back to ensure we can read it successfully.
			final KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEYSTORE_ALIAS, null);
			mPair = new KeyPair(entry.getCertificate().getPublicKey(), entry.getPrivateKey());
		}

		@Override
		public @NonNull String getType()
		{
			return ENCRYPTION_TYPE_API18;
		}

		private static void generateKeyPair(Context context, String alias) throws GeneralSecurityException
		{
			final Calendar notBefore = new GregorianCalendar();
			final Calendar notAfter = new GregorianCalendar();
			notAfter.add(Calendar.YEAR, 100);

			final KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
					.setAlias(alias)
					.setSubject(new X500Principal("CN=" + context.getPackageName()))
					.setSerialNumber(BigInteger.ONE)
					.setStartDate(notBefore.getTime())
					.setEndDate(notAfter.getTime())
					.build();

			final KeyPairGenerator gen = KeyPairGenerator.getInstance(KEY_ALGORITHM, KEYSTORE_TYPE);
			gen.initialize(spec);
			gen.generateKeyPair();
		}

		@Override
		public @NonNull byte[] wrap(@NonNull SecretKey key) throws GeneralSecurityException
		{
			mCipher.init(Cipher.WRAP_MODE, mPair.getPublic());
			return mCipher.wrap(key);
		}

		@Override
		public @NonNull SecretKey unwrap(@NonNull byte[] blob) throws GeneralSecurityException
		{
			mCipher.init(Cipher.UNWRAP_MODE, mPair.getPrivate());
			return (SecretKey) mCipher.unwrap(blob, SECRET_KEY_ALGORITHM, Cipher.SECRET_KEY);
		}
	}

	public static class CryptoException extends Exception
	{
		public CryptoException(String detailMessage, Throwable throwable)
		{
			super(detailMessage, throwable);
		}
	}
}
