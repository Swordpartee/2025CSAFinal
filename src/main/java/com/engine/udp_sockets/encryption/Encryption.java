package com.engine.udp_sockets.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;



public class Encryption {
	public static byte[] getPacketBytes(DatagramPacket pkt) {
		return Arrays.copyOfRange(pkt.getData(), 0, pkt.getLength());
	}
	
	public static byte[] concatBytes(byte[]... b) {
        ByteBuffer byteBuf = ByteBuffer.allocate(Arrays.stream(b).mapToInt(arr -> arr.length).sum());
        Arrays.stream(b).forEach(byteBuf::put);
		return byteBuf.array();
	}
	
	//RSA Optimal Asymmetric Encryption Padding
	public static KeyPair generateRSAOAEPKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048); // Use 2048-bit keys for security
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    return keyPair;
	}

	public static SecretKey generateAESKey() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(256); // AES-256
    SecretKey aesKey = keyGen.generateKey();
    
//    byte[] a = encryptAES("CHeese", aesKey);
//    byte[] b = decryptAES(new String(a, 0, a.length), aesKey);
//    System.out.println("AES Try: " + new String(b, 0, b.length));
    
    return aesKey;
	}

	public static String bytesToBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}
	
	public static PublicKey bytesToRSAPublicKey(byte[] publicKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    return publicKey;
	}
	
	public static SecretKey bytesToAESKey(byte[] AESKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
    SecretKey secretKey = new SecretKeySpec(AESKeyBytes, "AES");
    return secretKey;
	}

  // Encrypt with RSA-OAEP
  public static byte[] encryptRSA(byte[] bytes, PublicKey publicKey) throws Exception {
      Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] encryptedBytes = cipher.doFinal(bytes);
      return encryptedBytes;
  }

  // Decrypt with RSA-OAEP
  public static byte[] decryptRSA(byte[] bytes, PrivateKey privateKey) throws Exception {
      Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] decryptedBytes = cipher.doFinal(bytes);
      return decryptedBytes;
  }
  
  public static byte[] encryptAES(byte[] bytes, SecretKey aesKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
  	Cipher cipher = Cipher.getInstance("AES");
  	cipher.init(Cipher.ENCRYPT_MODE, aesKey);
  	byte[] encryptedBytes = cipher.doFinal(bytes);
  	return encryptedBytes;
  }
  
  public static byte[] decryptAES(byte[] bytes, SecretKey aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
  	Cipher cipher = Cipher.getInstance("AES");
  	cipher.init(Cipher.DECRYPT_MODE, aesKey);
  	byte[] decryptedBytes = cipher.doFinal(bytes);
  	return decryptedBytes;
  }
  
  // Hashing
  public static String hashString(String password) {
    // Create an Argon2 instance
    Argon2 argon2 = Argon2Factory.create();

    String hash;
    
    try {
        // Hash the password with Argon2
        hash = argon2.hash(2, 65536, 1, password.getBytes());
        // System.out.println("Hashed password: " + hash);

        // Verify the password
        boolean isMatch = argon2.verify(hash, password.getBytes());
        // System.out.println("Password verification: " + isMatch);
    } finally {
        // Ensure memory is wiped for security
        argon2.wipeArray(password.toCharArray());
    }
  	
  	return hash;
  }
}
