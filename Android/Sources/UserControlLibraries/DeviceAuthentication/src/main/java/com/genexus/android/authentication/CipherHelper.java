package com.genexus.android.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import androidx.annotation.RequiresApi;

import com.genexus.android.core.base.services.Services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import static com.genexus.util.Base64.decode;
import static com.genexus.util.Base64.encodeBytes;

public class CipherHelper {
    private static final String KEY_NAME = "GenexusFingerprintKey";
    private static final String LAST_USED_IV_SHARED_PREF_KEY = "GenexusFingerprint_LAST_USED_IV";
    private static final String FINGER_PRINT_HELPER = "GenexusFingerprintHelper";

    public static String encrypt(Cipher cipher, String inValue) {
        if (cipher == null)
            return null;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
            byte[] bytes = inValue.getBytes(Charset.defaultCharset());
            cipherOutputStream.write(bytes);
            cipherOutputStream.flush();
            cipherOutputStream.close();
            return encodeBytes(outputStream.toByteArray());
        } catch (IOException e) {
            Services.Log.error(e);
            return null;
        }
    }

    public static String decrypt(Cipher cipher, String inValue) {
        if (cipher == null)
            return null;

        try {
            byte[] decodedValue = decode(inValue);
            CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(decodedValue), cipher);

            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }
            cipherInputStream.close();

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < values.size(); i++) {
                bytes[i] = values.get(i);
            }

            return new String(bytes, Charset.defaultCharset());
        } catch (IOException e) {
            Services.Log.error(e);
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Cipher getCipher(Context applicationContext, int mode) {
        try {
            return getCipher(applicationContext, mode, generateKey(applicationContext));
        } catch (FingerprintException ignored) {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static KeyStore generateKey(Context applicationContext) throws FingerprintException {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            if (getLastIv(applicationContext) == null) {
                mKeyGenerator.init(new
                        KeyGenParameterSpec.Builder(KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(
                                KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
                mKeyGenerator.generateKey();
            }
            return keyStore;
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            throw new FingerprintException(exc);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static Cipher getCipher(Context applicationContext, int mode, KeyStore keyStore) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            if (mode == Cipher.ENCRYPT_MODE) {
                cipher.init(mode, key);
                byte[] iv = cipher.getIV();
                saveIv(applicationContext, iv);
            } else {
                byte[] lastIv = getLastIv(applicationContext);
                cipher.init(mode, key, new IvParameterSpec(lastIv));
            }
            return cipher;
        } catch (KeyPermanentlyInvalidatedException e) {
            return null;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException
                | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private static byte[] getLastIv(Context applicationContext) {
        SharedPreferences sharedPreferences = getSharedPreferences(applicationContext);
        if (sharedPreferences != null) {
            String ivString = sharedPreferences.getString(LAST_USED_IV_SHARED_PREF_KEY, null);
            if (ivString != null)
                return decode(ivString);
        }
        return null;
    }

    private static void saveIv(Context applicationContext, byte[] iv) {
        SharedPreferences.Editor edit = getSharedPreferences(applicationContext).edit();
        String string = encodeBytes(iv);
        edit.putString(LAST_USED_IV_SHARED_PREF_KEY, string);
        edit.commit();
    }

    private static SharedPreferences getSharedPreferences(Context applicationContext) {
        return applicationContext.getSharedPreferences(FINGER_PRINT_HELPER, 0);
    }

    private static class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }
}
