package com.engine.network.encryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.engine.util.Functions;

public class HMACAuthenticator {
  // HMAC algorithm to use (HMAC-SHA256)
  private static final String HMAC_ALGORITHM = "HmacSHA256";

  /**
   * Generate an HMAC token for authentication
   * @param sessionKey : The secret key for the user's session
   * @param username : The username to include in the token
   * @param timestamp : Current timestamp
   * @return Base64 encoded HMAC token
   */
  public static byte[] generateHMACToken(String sessionKey, String username, long timestamp) {
    try {
      // Create the message to sign (username + timestamp)
      String message = username + ":" + timestamp;
      
      // Create secret key specification
      SecretKeySpec secretKeySpec = new SecretKeySpec(
          sessionKey.getBytes(), 
          HMAC_ALGORITHM
      );
      
      // Initialize HMAC with the secret key
      Mac mac = Mac.getInstance(HMAC_ALGORITHM);
      mac.init(secretKeySpec);
      
      // Generate the HMAC
      byte[] hmacBytes = mac.doFinal(message.getBytes());

      byte[] truncated = new byte[10];
      System.arraycopy(hmacBytes, 0, truncated, 0, 10);
      return truncated;
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      System.err.println("Error generating HMAC: " + e.getMessage());
      return null; // Return null if there was an error generating the HMAC 
    }
  }

  /**
   * Validate the HMAC token, by regenerating it with the values given and comparing it with the client's sent token
   * @param sessionKey : The key for the user's session
   * @param username : The username to include in the token
   * @param timestamp : The timestamp to include in the token
   * @param clientHMAC : The HMAC sent by the client
   * @param maxTimeDriftSeconds : The maximum time drift allowed in seconds
   * @return true if the token is valid, false otherwise
   */
  public static boolean validateHMACToken(String sessionKey, String username, long timestamp, byte[] clientHMAC, long maxTimeDriftSeconds) {
    try {
      // Check timestamp validity
      long currentTime = (long) (Functions.getTime() / 1000);
      if (Math.abs(currentTime - timestamp) > maxTimeDriftSeconds) {
        return false; // Token expired or time drifted too much
      }
      
      // Regenerate the HMAC
      byte[] regeneratedHMAC = generateHMACToken(sessionKey, username, timestamp);

      // System.out.println("Session Key: " + sessionKey + " Username: " + username + " Timestamp: " + timestamp);
      // System.out.println("Regenerated HMAC: " + regeneratedHMAC + " Client HMAC: " + clientHMAC);
      
      // Compare the regenerated HMAC with the client's HMAC
      return Arrays.equals(regeneratedHMAC, clientHMAC);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Generate a cryptographically secure session key
   * @return Base64 encoded session key
   */
  public static String generateSessionKey() {
      // Generate 32 bytes (256 bits) of random data
      byte[] randomBytes = new byte[32];
      new SecureRandom().nextBytes(randomBytes);
      
      // Base64 encode for easy storage and transmission
      return Base64.getEncoder().encodeToString(randomBytes);
  }
}
