package com.engine.network.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;



public class Encryption {
    /**
     * Converts a packet into a byte array.
     * @param pkt : The packet to convert.
     * @return The converted byte array.
     */
	public static byte[] getPacketBytes(DatagramPacket pkt) {
		return Arrays.copyOfRange(pkt.getData(), 0, pkt.getLength());
	}
	
    /**
     * Concatenates multiple byte arrays into a single byte array. Useful for having a packet message with multiple parts.
     * @param b : The byte arrays to concatenate.
     * @return
     */
	public static byte[] concatBytes(byte[]... b) {
        ByteBuffer byteBuf = ByteBuffer.allocate(Arrays.stream(b).mapToInt(arr -> arr.length).sum());
        Arrays.stream(b).forEach(byteBuf::put);
		return byteBuf.array();
	}
	
    /**
     * Generates a RSAOAEP (RSA Optimal Asymmetric Encryption Padding) key pair for public key encryption.
     * @return the generated key pair
     * @throws NoSuchAlgorithmException
     */
	public static KeyPair generateRSAOAEPKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Use 2048-bit keys for security
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
	}

    /**
     * Generates a AES (Advanced Encryption Standard) key for symmetric encryption.
     * @return the generated AES key
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
	public static SecretKey generateAESKey() throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // AES-256
        SecretKey aesKey = keyGen.generateKey();
        return aesKey;
	}

    /**
     * Converts a byte array to a string using Base64 encoding.
     * @param bytes : the byte array to convert
     * @return the base64 encoded string
     */
	public static String bytesToBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}
	
    /**
     * Converts a byte array to a RSA public key that can be used for encryption.
     * @param publicKeyBytes : the byte array to convert
     * @return the public key
     * @throws Exception
     */
	public static PublicKey bytesToRSAPublicKey(byte[] publicKeyBytes) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        return publicKey;
	}
	
    /**
     * Converts a byte array to an AES key that can be used for symmetric encryption and decryption.
     * @param AESKeyBytes : the byte array to convert
     * @return the AES key
     * @throws Exception
     */
	public static SecretKey bytesToAESKey(byte[] AESKeyBytes) throws Exception {
        SecretKey secretKey = new SecretKeySpec(AESKeyBytes, "AES");
        return secretKey;
	}

    /**
     * Encrypts a byte array using RSA-OAEP with SHA-256 and MGF1 padding.
     * @param bytes : the byte array to encrypt
     * @param publicKey : the public key for encryption
     * @return the encrypted byte array
     * @throws Exception
     */
    public static byte[] encryptRSA(byte[] bytes, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(bytes);
        return encryptedBytes;
    }

    /**
     * Decrypts a byte array using RSA-OAEP with SHA-256 and MGF1 padding.
     * @param bytes : the byte array to decrypt
     * @param privateKey : the private key for decryption
     * @return the decrypted byte array
     * @throws Exception
     */
    public static byte[] decryptRSA(byte[] bytes, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(bytes);
        return decryptedBytes;
    }
  
    /**
     * Encrypts a byte array using an AES key.
     * @param bytes : the byte array to encrypt
     * @param aesKey : the AES key for encryption
     * @return the encrypted byte array
     * @throws Exception
     */
    public static byte[] encryptAES(byte[] bytes, SecretKey aesKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedBytes = cipher.doFinal(bytes);
        return encryptedBytes;
    }
  
    /**
     * Decrypts a byte array using an AES key.
     * @param bytes : the byte array to decrypt
     * @param aesKey : the AES key for decryption
     * @return the decrypted byte array
     * @throws Exception
     */
    public static byte[] decryptAES(byte[] bytes, SecretKey aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decryptedBytes = cipher.doFinal(bytes);
        return decryptedBytes;
    }
  
    /**
     * Hashes a password using Argon2 hashing algorithm.
     * @param password : the password to hash
     * @return the hashed password
     */
    public static String hashString(String password) {
        // Create an Argon2 instance
        Argon2 argon2 = Argon2Factory.create();

        String hash;

        try {
            // Hash the password with Argon2
            hash = argon2.hash(2, 65536, 1, password.getBytes());
            // System.out.println("Hashed password: " + hash);

            // Verify the password
            // boolean isMatch = argon2.verify(hash, password.getBytes());
            // System.out.println("Password verification: " + isMatch);
        } finally {
            // Ensure memory is wiped for security
            argon2.wipeArray(password.toCharArray());
        }

        return hash;
    }

    public static byte[][] desegmentPacket(byte[] packet) {
        
        ArrayList<byte[]> segments = new ArrayList<byte[]>();

        int index = 2;

        while (index < packet.length) {
            int length = Byte.toUnsignedInt(packet[index]);
            byte[] segment = Arrays.copyOfRange(packet, index + 1, index + length + 1);
            segments.add(segment);
            index += length + 1;
        }

        return segments.toArray(new byte[segments.size()][]);
    }
}
