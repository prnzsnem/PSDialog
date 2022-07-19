package com.ps.pslibrary;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AESUtils {
    static private final String ENCODING = "UTF-8";
    static private final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    static private final String AES = "AES";

    public static String encrypt(String inputText, String key) {
        String encryptedPlainText = "error_encrypted";
        byte[] encryptedByte = null;
        byte[] keyByte = null;
        Cipher cipher;
        SecretKeySpec secretKeySpec = null;
        IvParameterSpec ivParameterSpec = null;
        try {
            encryptedByte = inputText.getBytes(ENCODING);
            keyByte = getKeyBytes(key);
        } catch (NullPointerException | UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            return encryptedPlainText;
        }
        secretKeySpec = new SecretKeySpec(keyByte, AES);
        ivParameterSpec = new IvParameterSpec(keyByte);
        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            encryptedByte = cipher.doFinal(encryptedByte);
            encryptedPlainText = Base64.encodeToString(encryptedByte, Base64.DEFAULT);
            return encryptedPlainText;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println(e.getMessage());
            return encryptedPlainText;
        }
    }

    public static String decrypt(String encryptedText, String key) {
        String decryptedCypherText = "error_decrypted";
        byte[] encryptedByte;
        byte[] keyByte;
        try {
            encryptedByte = Base64.decode(encryptedText.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
            keyByte = getKeyBytes(key);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return decryptedCypherText;
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, AES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(keyByte);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            encryptedByte = cipher.doFinal(encryptedByte);
            decryptedCypherText = new String(encryptedByte, ENCODING);
            return decryptedCypherText;
        } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.out.println(e.getMessage());
            return decryptedCypherText;
        }
    }

    private static byte[] getKeyBytes(String key) {
        byte[] keyBytes = new byte[16];
        try {
            byte[] parameterKeyBytes = key.getBytes(ENCODING);
            System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
        } catch (UnsupportedEncodingException e) {
            System.out.println("[Error][AES][getKeyBytes][0]: " + e.getMessage());
        }
        return keyBytes;
    }
}