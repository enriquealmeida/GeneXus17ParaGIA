package com.genexus.android.superapps.security

import android.content.Context
import com.genexus.android.core.base.metadata.loader.MetadataLoader
import com.genexus.android.core.base.services.Services
import org.bouncycastle.util.encoders.Base64
import org.bouncycastle.util.encoders.DecoderException
import org.bouncycastle.util.io.pem.PemReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.Signature
import java.security.SignatureException
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec

class SignatureVerifier(private val context: Context) {
    fun verify(file: File, signature: String): Boolean {
        if (signature.isEmpty())
            return false

        try {
            val inputStream = FileInputStream(file)
            return getKey(context)?.let { verify(inputStream, signature, it) } ?: false
        } catch (e: FileNotFoundException) {
            Services.Log.error(e)
        } catch (e: IOException) {
            Services.Log.error(e)
        } catch (e: NoSuchAlgorithmException) {
            Services.Log.error(e)
        } catch (e: InvalidKeySpecException) {
            Services.Log.error(e)
        } catch (e: InvalidKeyException) {
            Services.Log.error(e)
        } catch (e: SignatureException) {
            Services.Log.error(e)
        } catch (e: DecoderException) {
            Services.Log.error(e)
        }
        return false
    }

    private fun getKey(context: Context): PublicKey? {
        val inputStream = MetadataLoader.getFromResources(context, CRT_NAME) ?: return null
        val keyStream = InputStreamReader(inputStream)
        val keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM)
        val reader = PemReader(keyStream)
        val pubKey = reader.readPemObject().content
        val publicKeySpec = X509EncodedKeySpec(pubKey)
        return keyFactory.generatePublic(publicKeySpec)
    }

    private fun verify(message: InputStream, signature: String, publicKey: PublicKey): Boolean {
        val sig = Signature.getInstance(HASH_ENCRYPTION_ALGORITHM).apply {
            initVerify(publicKey)
            val buffer = ByteArray(8192)
            var n = message.read(buffer)
            while (n > 0) {
                update(buffer, 0, n)
                n = message.read(buffer)
            }
        }
        return sig.verify(Base64.decode(signature))
    }

    companion object {
        private const val CRT_NAME = "superapp_crt"
        private const val ENCRYPTION_ALGORITHM = "RSA"
        private const val HASH_ENCRYPTION_ALGORITHM = "SHA256withRSA"
    }
}
