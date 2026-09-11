package com.treasure.basic.utils

/**
 * Created by treasure_ct on 2026/03/09
 * Description:
 */
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSAUtils {

    /**
     * RSA算法
     */
    private const val RSA_ALGORITHM = "RSA"

    /**
     * 加密填充
     */
    private const val RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding"

    /**
     * RSA最大加密块 (2048位密钥)
     */
    private const val MAX_ENCRYPT_BLOCK = 245

    /**
     * RSA最大解密块
     */
    private const val MAX_DECRYPT_BLOCK = 256

    private val X = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl/JMY8Q5r30W3ClElJK73rOrlYmm/czXxR1jGG7Sll7H/4W9mU892EKKsiA4OlxZ/dkSXUz4Hkl0PJkH+bKoNfIVrk1YCLGIT4gW+YE0TBfp009EdDoJb8liR8S1Fc9go32vXpHYJ8akv1BD4hA1ZmP5zpeYAHKGqViNOj7y10c8gsPIkR+k9tF8cbjJf5BiQeLIuEf6cBi+Rqf/7chsFXxol+Gb/3aQeAMeCyeMhqJEj0ZiF2YReDhBRobkea8j30OY7jrK2cFLOmgpkrwgWzo0Sg9LdE9Gw9+4Ssjnmo7cfOqbEAIZHyrQjdJEYh88RQi+HSwaqoNVRDa2h7fLlwIDAQAB\n"


    /**
     * 公钥加密
     */
    fun encrypt(data: String, publicKeyStr: String = X): String {

        val publicKey = getPublicKey(publicKeyStr)

        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        val dataBytes = data.toByteArray(Charsets.UTF_8)
        val inputLen = dataBytes.size

        var offSet = 0
        var i = 0

        val out = ByteArrayOutputStream()

        while (inputLen - offSet > 0) {

            val cache = if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cipher.doFinal(dataBytes, offSet, MAX_ENCRYPT_BLOCK)
            } else {
                cipher.doFinal(dataBytes, offSet, inputLen - offSet)
            }

            out.write(cache, 0, cache.size)

            i++
            offSet = i * MAX_ENCRYPT_BLOCK
        }

        val encryptedData = out.toByteArray()
        out.close()

        return Base64.encodeToString(encryptedData, Base64.NO_WRAP)
    }


    /**
     * 私钥解密
     */
    fun decrypt(encryptedData: String, privateKeyStr: String): String {

        val privateKey = getPrivateKey(privateKeyStr)

        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        val encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT)

        val inputLen = encryptedBytes.size

        var offSet = 0
        var i = 0

        val out = ByteArrayOutputStream()

        while (inputLen - offSet > 0) {

            val cache = if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cipher.doFinal(encryptedBytes, offSet, MAX_DECRYPT_BLOCK)
            } else {
                cipher.doFinal(encryptedBytes, offSet, inputLen - offSet)
            }

            out.write(cache, 0, cache.size)

            i++
            offSet = i * MAX_DECRYPT_BLOCK
        }

        val decryptedData = out.toByteArray()
        out.close()

        return String(decryptedData, Charsets.UTF_8)
    }


    /**
     * 获取公钥
     */
    private fun getPublicKey(publicKey: String): PublicKey {

        val keyBytes = Base64.decode(publicKey, Base64.DEFAULT)

        val keySpec = X509EncodedKeySpec(keyBytes)

        val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)

        return keyFactory.generatePublic(keySpec)
    }


    /**
     * 获取私钥
     */
    private fun getPrivateKey(privateKey: String): PrivateKey {

        val keyBytes = Base64.decode(privateKey, Base64.DEFAULT)

        val keySpec = PKCS8EncodedKeySpec(keyBytes)

        val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)

        return keyFactory.generatePrivate(keySpec)
    }
}