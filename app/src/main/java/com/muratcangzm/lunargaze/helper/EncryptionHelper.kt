package com.muratcangzm.lunargaze.helper

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionHelper(context: Context) {

    private val masterKeyAlias by lazy {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        MasterKeys.getOrCreate(keyGenParameterSpec)
    }

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "encrypted_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val secretKeyAlias = "my_secret_key"
    private val cipherTransformation = "AES/GCM/NoPadding"
    private val ivSize = 12
    private val tagSize = 128

    private fun getOrCreateSecretKey(): SecretKey {
        val existingKey = sharedPreferences.getString(secretKeyAlias, null)
        return if (existingKey != null) {
            decodeSecretKey(existingKey)
        } else {
            val newKey = generateSecretKey()
            sharedPreferences.edit()
                .putString(secretKeyAlias, encodeSecretKey(newKey))
                .apply()
            newKey
        }
    }

    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }

    private fun encodeSecretKey(secretKey: SecretKey): String {
        return Base64.getEncoder().encodeToString(secretKey.encoded)
    }

    private fun decodeSecretKey(encodedKey: String): SecretKey {
        val decodedKey = Base64.getDecoder().decode(encodedKey)
        return SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
    }

    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(cipherTransformation)
        val secretKey = getOrCreateSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(value.toByteArray())

        val combined = iv + encryptedBytes
        return Base64.getEncoder().encodeToString(combined)
    }

    fun decrypt(encryptedValue: String): String {
        val decodedBytes = Base64.getDecoder().decode(encryptedValue)

        if (decodedBytes.size < ivSize) {
            throw IllegalArgumentException("Invalid encrypted value: Not enough bytes to extract IV.")
        }

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
