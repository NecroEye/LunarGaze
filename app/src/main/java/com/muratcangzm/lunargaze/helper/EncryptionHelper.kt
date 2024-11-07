package com.muratcangzm.lunargaze.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionHelper(context: Context) {

    private var sharedPreferences: SharedPreferences

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    init {
        sharedPreferences = try {
            EncryptedSharedPreferences.create(
                "encrypted_prefs",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            // Handle corrupted preferences by clearing and reinitializing
            context.getSharedPreferences("encrypted_prefs", Context.MODE_PRIVATE).edit().clear().apply()
            EncryptedSharedPreferences.create(
                "encrypted_prefs",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    private val cipherTransformation = "AES/GCM/NoPadding"
    private val ivSize = 12
    private val tagSize = 128

    private fun getOrCreateSecretKey(): SecretKey {
        val existingKey = sharedPreferences.getString("my_secret_key", null)
        return if (existingKey != null) {
            // Decode the existing key if already created
            SecretKeySpec(Base64.getDecoder().decode(existingKey), "AES")
        } else {
            // Generate a new key if it doesn't exist
            val newKey = generateSecretKey()
            sharedPreferences.edit()
                .putString("my_secret_key", Base64.getEncoder().encodeToString(newKey.encoded))
                .apply()
            newKey
        }
    }

    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES").apply { init(256) }
        return keyGenerator.generateKey()
    }

    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(cipherTransformation)
        val secretKey = getOrCreateSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        // Combine IV and encrypted value
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(value.toByteArray())
        val combined = iv + encryptedBytes
        return Base64.getEncoder().encodeToString(combined)
    }

    fun decrypt(encryptedValue: String): String {
        val decodedBytes = Base64.getDecoder().decode(encryptedValue)

        // Extract IV and encrypted bytes
        val iv = decodedBytes.sliceArray(0 until ivSize)
        val encryptedBytes = decodedBytes.sliceArray(ivSize until decodedBytes.size)

        val cipher = Cipher.getInstance(cipherTransformation)
        val secretKey = getOrCreateSecretKey()
        val spec = GCMParameterSpec(tagSize, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}
