/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.util.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;

/**
 *
 * @author Mikel
 */

public class KeyGenerator {

    private static final String RSA = "RSA";

    /**
     * Executes the encryption using the public key.
     *
     * @param plainText The plain text to encrypt.
     * @param publicKey The public key used to encrypt.
     * @return an array of bytes.
     * @throws Exception
     */
    public static byte[] do_RSAEncryption(
            String plainText,
            PublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainText.getBytes());
    }

    /**
     * Decrypts the RSA String using the private key.
     *
     * @param cipherText The text to decrypt.
     * @param privateKey The private key.
     * @return The String decrypted.
     * @throws Exception
     */
    public static String do_RSADecryption(
            byte[] cipherText,
            PrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(cipherText);

        return new String(result);
    }

}
