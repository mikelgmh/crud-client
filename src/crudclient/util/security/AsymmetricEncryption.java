/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.util.security;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Mikel
 */
public class AsymmetricEncryption {

    private String publicKey;

    public AsymmetricEncryption(String publicKeyAsString) {
        this.publicKey = publicKeyAsString;
    }

    /**
     * Encrypts a String using the public key.
     *
     * @param text
     * @return The text encrypted.
     */
    public String encryptString(String text) {
        try {
            byte[] cipherText
                    = KeyGenerator.do_RSAEncryption(
                            text,
                            this.getPublicKey());
            //String stringBytes = Base64.getEncoder().encodeToString(cipherText);
            String stringBytes = toHexString(cipherText);
            return stringBytes;
        } catch (Exception ex) {
            Logger.getLogger(AsymmetricEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Gets the public key used to encrypt.
     *
     * @return the PublicKey.
     */
    private PublicKey getPublicKey() {
        try {
            String filename = "public-key.key";
            //byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

            byte[] keyBytes = DatatypeConverter.parseHexBinary(this.getPublicKeyAsString());

            X509EncodedKeySpec spec
                    = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AsymmetricEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AsymmetricEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Converts a ByteArray into a HexString String
     *
     * @param array
     * @return the string in HexString format
     */
    private static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    /**
     * Converts a String into a ByteArray.
     *
     * @param s The String to convert into ByteArray
     * @return a ByteArray.
     */
    private static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    private String getPublicKeyAsString() {
        return this.publicKey;
    }
}
